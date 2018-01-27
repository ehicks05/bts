package net.ehicks.bts.handlers;

import net.ehicks.bts.Route;
import net.ehicks.bts.SystemTask;
import net.ehicks.bts.beans.BtsSystem;
import net.ehicks.bts.beans.Role;
import net.ehicks.bts.beans.User;
import net.ehicks.bts.util.PasswordUtil;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class SignUpHandler
{
    @Route(tab1 = "registration", tab2 = "", tab3 = "", action = "register")
    public static void register(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String emailAddress = Common.getSafeString(request.getParameter("fldEmailAddress"));
        String password = Common.getSafeString(request.getParameter("fldPassword"));
        String password2 = Common.getSafeString(request.getParameter("fldPassword2"));

        String error = "";
        if (emailAddress.isEmpty() || password.isEmpty() || password2.isEmpty())
            error += "\nIncomplete form";
        if (User.getByLogonId(emailAddress) != null)
            error += "\nUsername unavailable";
        if (!password.equals(password2))
            error += "\nPassword did not match";

        if (error.isEmpty())
        {
            User user = new User();
            user.setLogonId(emailAddress);
            user.setPassword(PasswordUtil.digestPassword(password));
            user.setEnabled(true);
            user.setAvatarId(BtsSystem.getSystem().getDefaultAvatar());
            user.setCreatedOn(new Date());
            user.setUpdatedOn(new Date());

            Long userId = EOI.insert(user, SystemTask.REGISTRATION_HANDLER);

            Role role = new Role();
            role.setLogonId(user.getLogonId());
            role.setUserId(userId);
            role.setRoleName("user");
            EOI.insert(role, SystemTask.REGISTRATION_HANDLER);

            request.getSession().setAttribute("signUpResultMessage", "Sign up successful! Please sign in.");
            request.getSession().setAttribute("signUpResultClass", "is-success");
        }
        else
        {
            request.getSession().setAttribute("signUpResultMessage", error);
            request.getSession().setAttribute("signUpResultClass", "is-danger");
        }

        response.sendRedirect("view?tab1=dashboard&action=form");
    }
}
