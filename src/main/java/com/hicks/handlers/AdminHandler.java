package com.hicks.handlers;

import com.hicks.UserSession;
import com.hicks.beans.DBFile;
import com.hicks.beans.IssueForm;
import com.hicks.beans.Role;
import com.hicks.beans.User;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            EOI.executeDelete(User.getByUserId(userId));

        response.sendRedirect("view?tab1=admin&tab2=users&action=form");
    }

    public static String showModifyUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        request.setAttribute("user", user);
        request.setAttribute("roles", new ArrayList<>(Arrays.asList("user", "admin")));
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

            List<String> selectedRoles = Arrays.asList(request.getParameterValues("roles"));
            // remove existing roles that weren't selected
            for (Role role : Role.getByUserId(userId))
                if (!selectedRoles.contains(role.getRoleName()))
                    EOI.executeDelete(role);
            // add new roles that were selected but didn't already exist
            for (String roleName : selectedRoles)
            {
                Role role = Role.getByUserIdAndRoleName(userId, roleName);
                if (role == null)
                {
                    role = new Role();
                    role.setLogonId(user.getLogonId());
                    role.setUserId(user.getId());
                    role.setRoleName(roleName);
                    EOI.insert(role);
                }
            }
        }

        response.sendRedirect("view?tab1=admin&tab2=users&tab3=modify&action=form&userId=" + userId);
    }
}
