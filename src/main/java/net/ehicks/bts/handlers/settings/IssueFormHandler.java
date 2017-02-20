package net.ehicks.bts.handlers.settings;

import net.ehicks.bts.Route;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.IssueForm;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class IssueFormHandler
{
    private static final Logger log = LoggerFactory.getLogger(IssueFormHandler.class);

    @Route(tab1 = "settings", tab2 = "savedSearches", tab3 = "", action = "form")
    public static String showIssueForms(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<IssueForm> issueForms = IssueForm.getByUserId(userSession.getUserId());
        request.setAttribute("issueForms", issueForms);

        return "/WEB-INF/webroot/manageIssueFilters.jsp";
    }

    @Route(tab1 = "settings", tab2 = "savedSearches", tab3 = "", action = "delete")
    public static void deleteIssueForm(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        long id = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(id);
        if (issueForm != null)
        {
            int result = EOI.executeDelete(issueForm, userSession);
            log.info(String.valueOf(result));
        }

        response.sendRedirect("view?tab1=settings&tab2=savedSearches&action=form");
    }
}
