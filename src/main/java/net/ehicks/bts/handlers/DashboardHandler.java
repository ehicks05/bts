package net.ehicks.bts.handlers;

import net.ehicks.bts.Route;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.IssueForm;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardHandler
{
    @Route(tab1 = "dashboard", tab2 = "", tab3 = "", action = "form")
    public static String showDashboard(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<IssueForm> issueForms = IssueForm.getByUserId(userSession.getUserId());
        List<IssueForm> dashBoardIssueForms = issueForms.stream().filter(issueForm -> issueForm.getOnDash()).collect(Collectors.toList());

        request.setAttribute("dashBoardIssueForms", dashBoardIssueForms);

        return "/webroot/dashboard.jsp";
    }

    @Route(tab1 = "dashboard", tab2 = "", tab3 = "", action = "remove")
    public static void removeIssueForm(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        long id = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(id);
        if (issueForm != null)
        {
            issueForm.setOnDash(false);
            EOI.update(issueForm, userSession);
        }

        response.sendRedirect("view?tab1=dashboard&action=form");
    }
}
