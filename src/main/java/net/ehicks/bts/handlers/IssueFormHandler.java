package net.ehicks.bts.handlers;

import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.IssueForm;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class IssueFormHandler
{
    public static String showIssueForms(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<IssueForm> issueForms = IssueForm.getByUserId(userSession.getUserId());
        request.setAttribute("issueForms", issueForms);

        return "/WEB-INF/webroot/manageIssueFilters.jsp";
    }

    public static void deleteIssueForm(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        long id = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(id);
        if (issueForm != null)
        {
            int result = EOI.executeDelete(issueForm);
            System.out.println(result);
        }

        response.sendRedirect("view?tab1=main&tab2=issueForm&action=form");
    }

    public static void addToDashboard(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        long id = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(id);
        if (issueForm != null)
        {
            issueForm.setOnDash(true);
            EOI.update(issueForm);
        }

        response.sendRedirect("view?tab1=main&tab2=issueForm&action=form");
    }
}
