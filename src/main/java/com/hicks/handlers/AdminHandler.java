package com.hicks.handlers;

import com.hicks.UserSession;
import com.hicks.beans.*;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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
            user.setLogonId(logonId);
            user.setAvatarId(avatarId);
            EOI.update(user);

            List<Long> selectedGroupIds = Arrays.stream(request.getParameterValues("groups")).map(Long::valueOf).collect(Collectors.toList());

            // remove existing roles that weren't selected
            for (GroupMap groupMap : GroupMap.getByUserId(userId))
                if (!selectedGroupIds.contains(groupMap.getGroupId()))
                    EOI.executeDelete(groupMap);
            // add new roles that were selected but didn't already exist
            for (Long groupId : selectedGroupIds)
            {
                GroupMap groupMap = GroupMap.getByGroupIdAndUserId(userId, groupId);
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
}
