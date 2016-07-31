package com.hicks;

import com.hicks.beans.User;
import net.ehicks.common.Common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class UsersHandler
{
    public static String showUsers(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        request.setAttribute("users", User.getAllUsers());

        return "/WEB-INF/webroot/usersList.jsp";
    }

    public static String showModifyUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);

        request.setAttribute("user", user);

        return "/WEB-INF/webroot/userForm.jsp";
    }
}
