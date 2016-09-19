package com.hicks.handlers;

import com.hicks.UserSession;
import com.hicks.beans.IssueForm;
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
    public static String showDashboard(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<IssueForm> issueForms = IssueForm.getByUserId(userSession.getUserId());
        List<IssueForm> dashBoardIssueForms = issueForms.stream().filter(issueForm -> issueForm.getOnDash()).collect(Collectors.toList());

        request.setAttribute("dashBoardIssueForms", dashBoardIssueForms);

        return "/WEB-INF/webroot/dashboard.jsp";
    }

    public static void removeIssueForm(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        long id = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(id);
        if (issueForm != null)
        {
            issueForm.setOnDash(false);
            EOI.update(issueForm);
        }

        response.sendRedirect("view?tab1=main&tab2=dashboard&action=form");
    }
}
