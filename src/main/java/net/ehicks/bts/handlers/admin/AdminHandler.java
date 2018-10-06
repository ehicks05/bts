package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.*;
import net.ehicks.bts.beans.*;
import net.ehicks.bts.routing.Route;
import net.ehicks.common.Common;
import net.ehicks.eoi.ConnectionInfo;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.EOICache;
import net.ehicks.eoi.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class AdminHandler
{
    private static final Logger log = LoggerFactory.getLogger(AdminHandler.class);

    @Route(tab1 = "admin", tab2 = "", tab3 = "", action = "form")
    public static String showOverview(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<IssueForm> issueForms = IssueForm.getByUserId(userSession.getUserId());
        request.setAttribute("issueForms", issueForms);

        request.setAttribute("adminSubscreens", SystemInfo.INSTANCE.getAdminSubscreens());

        return "/webroot/admin/overview.jsp";
    }

    @Route(tab1 = "admin", tab2 = "cache", tab3 = "", action = "form")
    public static String showCacheInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("size", EOICache.cache.size());
        request.setAttribute("hits", EOICache.hits);
        request.setAttribute("misses", EOICache.misses);
        request.setAttribute("keyHitObjectMiss", EOICache.keyHitObjectMiss);

        return "/webroot/admin/cacheInfo.jsp";
    }

    @Route(tab1 = "admin", tab2 = "cache", tab3 = "", action = "clearCache")
    public static void clearCache(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        EOICache.cache.clear();

        response.sendRedirect("view?tab1=admin&tab2=cache&action=form");
    }

    @Route(tab1 = "admin", tab2 = "system", tab3 = "info", action = "form")
    public static String showSystemInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<Object> dbInfo;
        Map<String, String> dbInfoMap = new LinkedHashMap<>();
        if (SystemInfo.INSTANCE.getDbConnectionInfo().getDbMode().equals(ConnectionInfo.DbMode.POSTGRESQL.toString()))
        {
            dbInfo = EOI.executeQuery("select * from pg_stat_database where datname='bts'");
            Object[] results = (Object[]) dbInfo.get(0);
            List<String> headers = Arrays.asList("datid","datname","numbackends","xact_commit","xact_rollback","blks_read",
                    "blks_hit","tup_returned","tup_fetched","tup_inserted","tup_updated","tup_deleted","conflicts","temp_files",
                    "temp_bytes","deadlocks","blk_read_time","blk_write_time","stats_reset");
            for (int i = 0; i < headers.size(); i++)
            {
                dbInfoMap.put(headers.get(i), results[i].toString());
            }
            request.setAttribute("dbInfoMap", dbInfoMap);
        }
        else
            dbInfo = EOI.executeQuery("SELECT NAME, VALUE FROM INFORMATION_SCHEMA.SETTINGS");
        
        Map<String, String> cpInfo = Metrics.getMetrics();
        request.setAttribute("dbInfo", dbInfo);
        request.setAttribute("cpInfo", cpInfo);
        request.setAttribute("connectionInfo", SystemInfo.INSTANCE.getDbConnectionInfo());

        List<UserSession> userSessions = SessionListener.getSessions();
        userSessions.sort(Comparator.comparing(UserSession::getLastActivity).reversed());
        request.setAttribute("userSessions", userSessions);

        return "/webroot/admin/systemInfo.jsp";
    }

    @Route(tab1 = "admin", tab2 = "projects", tab3 = "", action = "form")
    public static String showManageProjects(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("projects", Project.getAll());

        return "/webroot/admin/projects.jsp";
    }

    @Route(tab1 = "admin", tab2 = "projects", tab3 = "", action = "create")
    public static void createProject(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String name = Common.getSafeString(request.getParameter("fldName"));
        String prefix = Common.getSafeString(request.getParameter("fldPrefix"));
        Project project = new Project();
        project.setName(name);
        project.setPrefix(prefix);
        long projectId = EOI.insert(project, userSession);

        response.sendRedirect("view?tab1=admin&tab2=projects&action=form");
    }

    @Route(tab1 = "admin", tab2 = "projects", tab3 = "", action = "delete")
    public static void deleteProject(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long projectId = Common.stringToLong(request.getParameter("projectId"));
        Project project = Project.getById(projectId);
        if (project != null)
            EOI.executeDelete(project, userSession);

        response.sendRedirect("view?tab1=admin&tab2=projects&action=form");
    }

    @Route(tab1 = "admin", tab2 = "projects", tab3 = "modify", action = "form")
    public static String showModifyProject(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long projectId = Common.stringToLong(request.getParameter("projectId"));
        Project project = Project.getById(projectId);
        request.setAttribute("project", project);

        return "/webroot/admin/modifyProject.jsp";
    }

    @Route(tab1 = "admin", tab2 = "projects", tab3 = "modify", action = "modify")
    public static void modifyProject(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long projectId = Common.stringToLong(request.getParameter("projectId"));
        Project project = Project.getById(projectId);
        if (project != null)
        {
            String name = Common.getSafeString(request.getParameter("name"));
            String prefix = Common.getSafeString(request.getParameter("prefix"));
            project.setName(name);
            project.setPrefix(prefix);
            EOI.update(project, userSession);
        }

        response.sendRedirect("view?tab1=admin&tab2=projects&tab3=modify&action=form&projectId=" + projectId);
    }

    @Route(tab1 = "admin", tab2 = "groups", tab3 = "", action = "form")
    public static String showManageGroups(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("groups", Group.getAll());

        return "/webroot/admin/groups.jsp";
    }

    @Route(tab1 = "admin", tab2 = "groups", tab3 = "", action = "create")
    public static void createGroup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String name = Common.getSafeString(request.getParameter("fldName"));
        Group group = new Group();
        group.setName(name);
        long groupId = EOI.insert(group, userSession);

        response.sendRedirect("view?tab1=admin&tab2=groups&action=form");
    }

    @Route(tab1 = "admin", tab2 = "groups", tab3 = "", action = "delete")
    public static void deleteGroup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long groupId = Common.stringToLong(request.getParameter("groupId"));
        Group group = Group.getById(groupId);
        if (group != null)
            EOI.executeDelete(group, userSession);

        response.sendRedirect("view?tab1=admin&tab2=groups&action=form");
    }

    @Route(tab1 = "admin", tab2 = "groups", tab3 = "modify", action = "form")
    public static String showModifyGroup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long groupId = Common.stringToLong(request.getParameter("groupId"));
        Group group = Group.getById(groupId);
        request.setAttribute("group", group);

        return "/webroot/admin/modifyGroup.jsp";
    }

    @Route(tab1 = "admin", tab2 = "groups", tab3 = "modify", action = "modify")
    public static void modifyGroup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long groupId = Common.stringToLong(request.getParameter("groupId"));
        Group group = Group.getById(groupId);
        if (group != null)
        {
            String name = Common.getSafeString(request.getParameter("name"));
            group.setName(name);
            EOI.update(group, userSession);
        }

        response.sendRedirect("view?tab1=admin&tab2=groups&tab3=modify&action=form&groupId=" + groupId);
    }

    @Route(tab1 = "admin", tab2 = "email", tab3 = "", action = "form")
    public static String showManageEmails(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("emails", EmailMessage.getAll());

        return "/webroot/admin/emails.jsp";
    }

    @Route(tab1 = "admin", tab2 = "email", tab3 = "preview", action = "form")
    public static String previewEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long emailId = Common.stringToLong(request.getParameter("emailId"));
        EmailMessage emailMessage = EmailMessage.getById(emailId);
        if (emailMessage != null)
            request.setAttribute("email", EmailMessage.getById(emailId));

        return "/webroot/admin/previewEmail.jsp";
    }

    @Route(tab1 = "admin", tab2 = "email", tab3 = "", action = "sendTest")
    public static void sendTestEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String to = Common.getSafeString(request.getParameter("fldTo"));
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setActionId(EmailAction.TEST.getId());
        emailMessage.setToAddress(to);
        long emailId = EOI.insert(emailMessage, userSession);
        emailMessage = EmailMessage.getById(emailId);

        EmailEngine.sendEmail(emailMessage);

        response.sendRedirect("view?tab1=admin&tab2=email&action=form");
    }

    @Route(tab1 = "admin", tab2 = "email", tab3 = "", action = "delete")
    public static void deleteEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long emailId = Common.stringToLong(request.getParameter("emailId"));
        EmailMessage emailMessage = EmailMessage.getById(emailId);
        if (emailMessage != null)
            EOI.executeDelete(emailMessage, userSession);

        response.sendRedirect("view?tab1=admin&tab2=email&action=form");
    }

    @Route(tab1 = "admin", tab2 = "email", tab3 = "modify", action = "form")
    public static String showModifyEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        request.setAttribute("btsSystem", BtsSystem.getSystem());

        return "/webroot/admin/modifyEmail.jsp";
    }

    @Route(tab1 = "admin", tab2 = "email", tab3 = "modify", action = "modify")
    public static void modifyEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        BtsSystem btsSystem = BtsSystem.getSystem();
        if (btsSystem != null)
        {
            btsSystem.setEmailHost(Common.getSafeString(request.getParameter("emailHost")));
            btsSystem.setEmailUser(Common.getSafeString(request.getParameter("emailUser")));
            btsSystem.setEmailPassword(Common.getSafeString(request.getParameter("emailPassword")));
            btsSystem.setEmailPort(Common.stringToInt(request.getParameter("emailPort")));
            btsSystem.setEmailFromAddress(Common.getSafeString(request.getParameter("emailFromAddress")));
            btsSystem.setEmailFromName(Common.getSafeString(request.getParameter("emailFromName")));
            EOI.update(btsSystem, userSession);
        }

        response.sendRedirect("view?tab1=admin&tab2=email&tab3=modify&action=form");
    }

    @Route(tab1 = "admin", tab2 = "system", tab3 = "modify", action = "form")
    public static String showModifySystem(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        request.setAttribute("btsSystem", BtsSystem.getSystem());
        request.setAttribute("themes", Arrays.asList("default","cosmo","flatly","journal","lux","pulse","simplex","superhero","united","yeti"));
        return "/webroot/admin/modifySystem.jsp";
    }

    @Route(tab1 = "admin", tab2 = "system", tab3 = "modify", action = "modify")
    public static void modifySystem(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        BtsSystem btsSystem = BtsSystem.getSystem();
        if (btsSystem != null)
        {
            btsSystem.setInstanceName(Common.getSafeString(request.getParameter("instanceName")));
            btsSystem.setLogonMessage(Common.getSafeString(request.getParameter("logonMessage")));
            btsSystem.setDefaultAvatar(Common.stringToLong(request.getParameter("defaultAvatar")));
            btsSystem.setTheme(Common.getSafeString(request.getParameter("theme")));
            EOI.update(btsSystem, userSession);

            request.getServletContext().setAttribute("btsSystem", BtsSystem.getSystem());
        }

        response.sendRedirect("view?tab1=admin&tab2=system&tab3=modify&action=form");
    }
}
