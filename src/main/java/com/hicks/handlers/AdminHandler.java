package com.hicks.handlers;

import com.hicks.UserSession;
import com.hicks.beans.IssueForm;
import com.hicks.beans.User;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
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
}
