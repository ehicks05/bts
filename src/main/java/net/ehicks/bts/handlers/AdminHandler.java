package net.ehicks.bts.handlers;

import net.ehicks.bts.EmailEngine;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.*;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.EOICache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminHandler
{
    public static String showOverview(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<IssueForm> issueForms = IssueForm.getByUserId(userSession.getUserId());
        request.setAttribute("issueForms", issueForms);

        return "/WEB-INF/webroot/admin/overview.jsp";
    }

    public static String showCacheInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("size", EOICache.cache.size());
        request.setAttribute("hits", EOICache.hits);
        request.setAttribute("misses", EOICache.misses);
        request.setAttribute("keyHitObjectMiss", EOICache.keyHitObjectMiss);

        return "/WEB-INF/webroot/admin/cacheInfo.jsp";
    }

    public static void clearCache(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        EOICache.cache.clear();

        response.sendRedirect("view?tab1=admin&tab2=cache&action=form");
    }

    public static String showSystemInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<Object> dbInfo = EOI.executeQuery("SELECT NAME, VALUE FROM INFORMATION_SCHEMA.SETTINGS");
        List<String> cpInfo = EOI.getCPInfo();
        request.setAttribute("dbInfo", dbInfo);
        request.setAttribute("cpInfo", cpInfo);

        return "/WEB-INF/webroot/admin/systemInfo.jsp";
    }

    public static String showManageUsers(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("users", User.getAll());

        return "/WEB-INF/webroot/admin/users.jsp";
    }

    public static void createUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String logonId = Common.getSafeString(request.getParameter("fldLogonId"));
        User user = new User();
        user.setLogonId(logonId);
        long userId = EOI.insert(user);

        response.sendRedirect("view?tab1=admin&tab2=users&action=form");
    }

    public static void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        if (user != null)
            EOI.executeDelete(user);

        response.sendRedirect("view?tab1=admin&tab2=users&action=form");
    }

    public static String showModifyUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        request.setAttribute("user", user);
        request.setAttribute("avatars", DBFile.getAll());

        return "/WEB-INF/webroot/admin/modifyUser.jsp";
    }

    public static void modifyUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        if (user != null)
        {
            String logonId = Common.getSafeString(request.getParameter("logonId"));
            Long avatarId = Common.stringToLong(request.getParameter("avatarId"));
            String firstName = Common.getSafeString(request.getParameter("firstName"));
            String lastName = Common.getSafeString(request.getParameter("lastName"));
            user.setLogonId(logonId);
            user.setAvatarId(avatarId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            EOI.update(user);

            List<Long> selectedGroupIds = Arrays.stream(request.getParameterValues("groups")).map(Long::valueOf).collect(Collectors.toList());

            // remove existing roles that weren't selected
            for (GroupMap groupMap : GroupMap.getByUserId(userId))
                if (!selectedGroupIds.contains(groupMap.getGroupId()))
                    EOI.executeDelete(groupMap);
            // add new roles that were selected but didn't already exist
            for (Long groupId : selectedGroupIds)
            {
                GroupMap groupMap = GroupMap.getByUserIdAndGroupId(userId, groupId);
                if (groupMap == null)
                {
                    groupMap = new GroupMap();
                    groupMap.setUserId(user.getId());
                    groupMap.setGroupId(groupId);
                    EOI.insert(groupMap);
                }
            }
        }

        response.sendRedirect("view?tab1=admin&tab2=users&tab3=modify&action=form&userId=" + userId);
    }

    public static String showManageProjects(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("projects", Project.getAll());

        return "/WEB-INF/webroot/admin/projects.jsp";
    }

    public static void createProject(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String name = Common.getSafeString(request.getParameter("fldName"));
        String prefix = Common.getSafeString(request.getParameter("fldPrefix"));
        Project project = new Project();
        project.setName(name);
        project.setPrefix(prefix);
        long projectId = EOI.insert(project);

        response.sendRedirect("view?tab1=admin&tab2=projects&action=form");
    }

    public static void deleteProject(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long projectId = Common.stringToLong(request.getParameter("projectId"));
        Project project = Project.getById(projectId);
        if (project != null)
            EOI.executeDelete(project);

        response.sendRedirect("view?tab1=admin&tab2=projects&action=form");
    }

    public static String showModifyProject(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long projectId = Common.stringToLong(request.getParameter("projectId"));
        Project project = Project.getById(projectId);
        request.setAttribute("project", project);

        return "/WEB-INF/webroot/admin/modifyProject.jsp";
    }

    public static void modifyProject(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long projectId = Common.stringToLong(request.getParameter("projectId"));
        Project project = Project.getById(projectId);
        if (project != null)
        {
            String name = Common.getSafeString(request.getParameter("name"));
            String prefix = Common.getSafeString(request.getParameter("prefix"));
            project.setName(name);
            project.setPrefix(prefix);
            EOI.update(project);
        }

        response.sendRedirect("view?tab1=admin&tab2=projects&tab3=modify&action=form&projectId=" + projectId);
    }

    public static String showManageGroups(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("groups", Group.getAll());

        return "/WEB-INF/webroot/admin/groups.jsp";
    }

    public static void createGroup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String name = Common.getSafeString(request.getParameter("fldName"));
        Group group = new Group();
        group.setName(name);
        long groupId = EOI.insert(group);

        response.sendRedirect("view?tab1=admin&tab2=groups&action=form");
    }

    public static void deleteGroup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long groupId = Common.stringToLong(request.getParameter("groupId"));
        Group group = Group.getById(groupId);
        if (group != null)
            EOI.executeDelete(group);

        response.sendRedirect("view?tab1=admin&tab2=groups&action=form");
    }

    public static String showModifyGroup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long groupId = Common.stringToLong(request.getParameter("groupId"));
        Group group = Group.getById(groupId);
        request.setAttribute("group", group);

        return "/WEB-INF/webroot/admin/modifyGroup.jsp";
    }

    public static void modifyGroup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long groupId = Common.stringToLong(request.getParameter("groupId"));
        Group group = Group.getById(groupId);
        if (group != null)
        {
            String name = Common.getSafeString(request.getParameter("name"));
            group.setName(name);
            EOI.update(group);
        }

        response.sendRedirect("view?tab1=admin&tab2=groups&tab3=modify&action=form&groupId=" + groupId);
    }

    public static String showManageEmails(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("emails", EmailMessage.getAll());

        return "/WEB-INF/webroot/admin/emails.jsp";
    }

    public static void sendTestEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String to = Common.getSafeString(request.getParameter("fldTo"));
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setAction("test");
        emailMessage.setToAddress(to);
        long emailId = EOI.insert(emailMessage);
        emailMessage = EmailMessage.getById(emailId);

        EmailEngine.sendEmail(emailMessage);

        response.sendRedirect("view?tab1=admin&tab2=email&action=form");
    }

    public static void deleteEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long emailId = Common.stringToLong(request.getParameter("emailId"));
        EmailMessage emailMessage = EmailMessage.getById(emailId);
        if (emailMessage != null)
            EOI.executeDelete(emailMessage);

        response.sendRedirect("view?tab1=admin&tab2=email&action=form");
    }

    public static String showModifyEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long emailId = Common.stringToLong(request.getParameter("emailId"));
        EmailMessage emailMessage = EmailMessage.getById(emailId);
        request.setAttribute("emailMessage", emailMessage);

        return "/WEB-INF/webroot/admin/modifyEmail.jsp";
    }

    public static void modifyEmail(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long emailId = Common.stringToLong(request.getParameter("emailId"));
        EmailMessage emailMessage = EmailMessage.getById(emailId);
        if (emailMessage != null)
        {
            String name = Common.getSafeString(request.getParameter("name"));
            EOI.update(emailMessage);
        }

        response.sendRedirect("view?tab1=admin&tab2=email&tab3=modify&action=form&emailId=" + emailId);
    }
}
