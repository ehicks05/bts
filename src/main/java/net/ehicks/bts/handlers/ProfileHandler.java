package net.ehicks.bts.handlers;

import net.ehicks.bts.Route;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.User;
import net.ehicks.common.Common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class ProfileHandler
{
    @Route(tab1 = "profile", tab2 = "", tab3 = "", action = "form")
    public static String showProfile(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);

        if (!User.getAllForUser(userSession.getUserId()).contains(user))
            return "/WEB-INF/webroot/error.jsp";

        request.setAttribute("user", user);

        return "/WEB-INF/webroot/userForm.jsp";
    }
}
