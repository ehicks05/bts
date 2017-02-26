package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.*;
import net.ehicks.bts.beans.*;
import net.ehicks.bts.util.PasswordUtil;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.EOICache;
import net.ehicks.eoi.Metrics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class AdminHandler
{
    @Route(tab1 = "admin", tab2 = "", tab3 = "", action = "")
    @Route(tab1 = "admin", tab2 = "", tab3 = "", action = "form")
    public static void redirect(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        response.sendRedirect("view?tab1=admin&tab2=overview&action=form");
    }

    @Route(tab1 = "admin", tab2 = "overview", tab3 = "", action = "form")
    public static String showOverview(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<IssueForm> issueForms = IssueForm.getByUserId(userSession.getUserId());
        request.setAttribute("issueForms", issueForms);

        return "/WEB-INF/webroot/admin/overview.jsp";
    }

    @Route(tab1 = "admin", tab2 = "cache", tab3 = "", action = "form")
    public static String showCacheInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("size", EOICache.cache.size());
        request.setAttribute("hits", EOICache.hits);
        request.setAttribute("misses", EOICache.misses);
        request.setAttribute("keyHitObjectMiss", EOICache.keyHitObjectMiss);

        return "/WEB-INF/webroot/admin/cacheInfo.jsp";
    }

    @Route(tab1 = "admin", tab2 = "cache", tab3 = "", action = "clearCache")
    public static void clearCache(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        EOICache.cache.clear();

        response.sendRedirect("view?tab1=admin&tab2=cache&action=form");
    }

    @Route(tab1 = "admin", tab2 = "system", tab3 = "", action = "form")
    public static String showSystemInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<Object> dbInfo = EOI.executeQuery("SELECT NAME, VALUE FROM INFORMATION_SCHEMA.SETTINGS");
        Map<String, String> cpInfo = Metrics.getMetrics();
        request.setAttribute("dbInfo", dbInfo);
        request.setAttribute("cpInfo", cpInfo);

        List<UserSession> userSessions = SessionListener.getSessions();
        userSessions.sort(Comparator.comparing(UserSession::getLastActivity).reversed());
        request.setAttribute("userSessions", userSessions);

        return "/WEB-INF/webroot/admin/systemInfo.jsp";
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "form")
    public static String showManageUsers(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("users", User.getAll());

        return "/WEB-INF/webroot/admin/users.jsp";
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "create")
    public static void createUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String logonId = Common.getSafeString(request.getParameter("fldLogonId"));
        User user = new User();
        user.setLogonId(logonId);
        long userId = EOI.insert(user, userSession);

        response.sendRedirect("view?tab1=admin&tab2=users&action=form");
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "delete")
    public static void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        if (user != null)
            EOI.executeDelete(user, userSession);

        response.sendRedirect("view?tab1=admin&tab2=users&action=form");
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "print")
    public static void printUsers(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        List<List> userData = new ArrayList<>();
        userData.add(Arrays.asList("Object Id", "Logon Id", "Last", "First", "Created On"));
        for (User user : User.getAll())
            userData.add(Arrays.asList(user.getId(), user.getLogonId(), user.getLastName(), user.getFirstName(), user.getCreatedOn()));
        File file = PdfCreator.createPdf("Me", "Users Report", "", userData);

        CommonIO.sendFileInResponse(response, file, true);
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "modify", action = "form")
    public static String showModifyUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        request.setAttribute("user", user);
        request.setAttribute("avatars", Avatar.getAll());

        return "/WEB-INF/webroot/admin/modifyUser.jsp";
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "modify", action = "modify")
    public static void modifyUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        if (user != null)
        {
            String logonId = Common.getSafeString(request.getParameter("logonId"));
            Long avatarId = Common.stringToLong(request.getParameter("avatarId"));
            String firstName = Common.getSafeString(request.getParameter("firstName"));
            String lastName = Common.getSafeString(request.getParameter("lastName"));
            boolean enabled = request.getParameter("enabled") != null;
            user.setLogonId(logonId);
            user.setAvatarId(avatarId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(enabled);
            EOI.update(user, userSession);

            List<Long> selectedGroupIds = Arrays.stream(request.getParameterValues("groups")).map(Long::valueOf).collect(Collectors.toList());

            // remove existing groups that weren't selected
            for (GroupMap groupMap : GroupMap.getByUserId(userId))
                if (!selectedGroupIds.contains(groupMap.getGroupId()))
                    EOI.executeDelete(groupMap, userSession);
            // add new groups that were selected but didn't already exist
            for (Long groupId : selectedGroupIds)
            {
                GroupMap groupMap = GroupMap.getByUserIdAndGroupId(userId, groupId);
                if (groupMap == null)
                {
                    groupMap = new GroupMap();
                    groupMap.setUserId(user.getId());
                    groupMap.setGroupId(groupId);
                    EOI.insert(groupMap, userSession);
                }
            }

            List<Long> selectedProjectIds = Arrays.stream(request.getParameterValues("projects")).map(Long::valueOf).collect(Collectors.toList());

            // remove existing projects that weren't selected
            for (ProjectMap projectMap : ProjectMap.getByUserId(userId))
                if (!selectedProjectIds.contains(projectMap.getProjectId()))
                    EOI.executeDelete(projectMap, userSession);
            // add new projects that were selected but didn't already exist
            for (Long projectId : selectedProjectIds)
            {
                ProjectMap projectMap = ProjectMap.getByUserIdAndProjectId(userId, projectId);
                if (projectMap == null)
                {
                    projectMap = new ProjectMap();
                    projectMap.setUserId(user.getId());
                    projectMap.setProjectId(projectId);
                    EOI.insert(projectMap, userSession);
                }
            }
        }

        response.sendRedirect("view?tab1=admin&tab2=users&tab3=modify&action=form&userId=" + userId);
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "changePassword")
    public static void changePassword(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        if (user != null)
        {
            String password = Common.getSafeString(request.getParameter("password"));
            if (password.length() > 0)
            {
                user.setPassword(PasswordUtil.digestPassword(password));
                EOI.update(user, userSession);
            }
        }

        response.sendRedirect("view?tab1=admin&tab2=users&tab3=modify&action=form&userId=" + userId);
    }

    @Route(tab1 = "admin", tab2 = "projects", tab3 = "", action = "form")
    public static String showManageProjects(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("projects", Project.getAll());

        return "/WEB-INF/webroot/admin/projects.jsp";
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

        return "/WEB-INF/webroot/admin/modifyProject.jsp";
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

        return "/WEB-INF/webroot/admin/groups.jsp";
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

        return "/WEB-INF/webroot/admin/modifyGroup.jsp";
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

        return "/WEB-INF/webroot/admin/emails.jsp";
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
        Long emailId = Common.stringToLong(request.getParameter("emailId"));
        EmailMessage emailMessage = EmailMessage.getById(emailId);
        request.setAttribute("emailMessage", emailMessage);

        return "/WEB-INF/webroot/admin/modifyEmail.jsp";
    }

    @Route(tab1 = "admin", tab2 = "email", tab3 = "modify", action = "modify")
    public static void modifyEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long emailId = Common.stringToLong(request.getParameter("emailId"));
        EmailMessage emailMessage = EmailMessage.getById(emailId);
        if (emailMessage != null)
        {
            String name = Common.getSafeString(request.getParameter("name"));
            EOI.update(emailMessage, userSession);
        }

        response.sendRedirect("view?tab1=admin&tab2=email&tab3=modify&action=form&emailId=" + emailId);
    }
}
