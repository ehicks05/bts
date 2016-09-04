package com.hicks.handlers;

import com.hicks.SearchResult;
import com.hicks.UserSession;
import com.hicks.beans.*;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.PSIngredients;
import net.ehicks.eoi.SQLGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class IssueSearchHandler
{
    public static String showIssues(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long issueFormId = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(issueFormId);

        if (issueForm == null)
            issueForm = new IssueForm();

        SearchResult searchResult = issueForm.getSearchResult();

        request.setAttribute("issueForm", issueForm);
        request.setAttribute("searchResult", searchResult);

        return "/WEB-INF/webroot/issuesList.jsp";
    }

    public static void ajaxGetPageOfResults(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, ServletException
    {
        Long issueFormId = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(issueFormId);

        if (issueForm == null)
            issueForm = new IssueForm();

        // parse sorting fields
        String sortColumn = request.getParameter("sortColumn");
        String sortDirection = request.getParameter("sortDirection");
        if (sortColumn == null)
        {
            sortColumn = "id";
            sortDirection = "asc";
        }

        if (sortDirection == null)
            sortDirection = "asc";

        String page = request.getParameter("page");
        if (page == null || page.length() == 0)
            page = "1";

        issueForm.setSortColumn(sortColumn);
        issueForm.setSortDirection(sortDirection);
        issueForm.setPage(page);

        SearchResult searchResult = issueForm.getSearchResult();

        request.setAttribute("issueForm", issueForm);
        request.setAttribute("searchResult", searchResult);

        request.getRequestDispatcher("/WEB-INF/webroot/issueTable.jsp").forward(request, response);
    }

    public static void search(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        IssueForm issueForm = getIssueFormFromRequest(request);
        SearchResult searchResult = performSearch(issueForm);

        request.setAttribute("issueForm", issueForm);
        request.setAttribute("searchResult", searchResult);

        response.sendRedirect("view?action=form");
    }

    private static IssueForm getIssueFormFromRequest(HttpServletRequest request)
    {
        Long id                 = Common.stringToLong(request.getParameter("id"));
        String containsText     = Common.getSafeString(request.getParameter("containsText"));
        String title            = Common.getSafeString(request.getParameter("title"));
        String description      = Common.getSafeString(request.getParameter("description"));
        String statusIds        = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("status")));
        String severityIds      = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("severity")));
        String zoneIds          = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("zoneIds")));
        String assigneeIds      = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("assigneeIds")));
        Date createdOn          = Common.stringToDate(request.getParameter("createdOn"));
        Date lastUpdatedOn      = Common.stringToDate(request.getParameter("lastUpdatedOn"));

        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        // parse sorting fields
        String sortColumn = request.getParameter("sortColumn");
        String sortDirection = request.getParameter("sortDirection");
        if (sortColumn == null)
        {
            sortColumn = "id";
            sortDirection = "asc";
        }

        if (sortDirection == null)
            sortDirection = "asc";

        String page = request.getParameter("page");
        if (page == null || page.length() == 0)
            page = "1";

        IssueForm issueForm = new IssueForm(id, userSession.getUserId(), containsText, title, description, statusIds, severityIds, zoneIds, assigneeIds, createdOn, lastUpdatedOn);
        issueForm.setSortColumn(sortColumn);
        issueForm.setSortDirection(sortDirection);
        issueForm.setPage(page);

        return issueForm;
    }

    public static void saveIssueForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        IssueForm issueForm = getIssueFormFromRequest(request);
        long issueFormId = EOI.insert(issueForm);

        response.sendRedirect("view?action=form&issueFormId=" + issueFormId);
    }

    public static SearchResult performSearch(IssueForm issueForm) throws ParseException, IOException
    {
        long resultsPerPage = 10;

        if (issueForm.getSortColumn().length() == 0) issueForm.setSortColumn("id");
        if (issueForm.getSortDirection().length() == 0) issueForm.setSortDirection("asc");
        if (issueForm.getPage().length() == 0) issueForm.setPage("1");

        PSIngredients filmQuery = IssueForm.buildFilmSQLQuery(issueForm, resultsPerPage);
        String countVersionOfQuery = SQLGenerator.getCountVersionOfQuery(filmQuery.query);

        List countResult = EOI.executeQueryOneResult(countVersionOfQuery, filmQuery.args);
        long resultSize = (Long) countResult.get(0);
        List<Issue> filteredIssues = EOI.executeQuery(filmQuery.query, filmQuery.args);

        return new SearchResult(issueForm.getPage(), filteredIssues, resultSize, resultsPerPage);
    }
}
