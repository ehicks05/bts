package net.ehicks.bts.handlers;

import net.ehicks.bts.routing.Route;
import net.ehicks.bts.SearchResult;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.IssueForm;
import net.ehicks.bts.beans.User;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.PSIngredients;
import net.ehicks.eoi.SQLGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class IssueSearchHandler
{
    @Route(tab1 = "search", tab2 = "", tab3 = "", action = "form")
    public static String showIssues(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long issueFormId = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(issueFormId);

        // check the session
        if (issueForm == null)
            issueForm = (IssueForm) request.getSession().getAttribute("issueForm");

        if (issueForm == null)
        {
            issueForm = new IssueForm();
            issueForm.setUserId(userSession.getUserId());
            issueForm = updateIssueFormFromRequest(issueForm, request);

            request.getSession().setAttribute("issueForm", issueForm);
        }

        SearchResult searchResult = issueForm.getSearchResult();

        request.setAttribute("users", User.getAllVisible(userSession.getUserId()));
        request.setAttribute("issueForm", issueForm);
        request.setAttribute("searchResult", searchResult);

        return "/webroot/issuesList.jsp";
    }

    @Route(tab1 = "search", tab2 = "", tab3 = "", action = "ajaxGetPageOfResults")
    public static void ajaxGetPageOfResults(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, ServletException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long issueFormId = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(issueFormId);

        // check the session
        if (issueForm == null)
            issueForm = (IssueForm) request.getSession().getAttribute("issueForm");

        if (issueForm == null)
            issueForm = new IssueForm();

        // parse sorting fields
        String sortColumn = request.getParameter("sortColumn");
        String sortDirection = request.getParameter("sortDirection");

        // we must be doing a resort
        if (sortColumn != null && sortDirection != null)
        {
            if (sortColumn.equals(issueForm.getSortColumn()))
            {
                if (sortDirection.equals("asc"))
                    sortDirection = "desc";
                else
                    sortDirection = "asc";
            }

            issueForm.setSortColumn(sortColumn);
            issueForm.setSortDirection(sortDirection);

            // we want to persist sorting preferences
            if (issueFormId > 0)
                EOI.update(issueForm, userSession);
        }

        String page = request.getParameter("page");
        if (page != null)
            issueForm.setPage(page);

        SearchResult searchResult = issueForm.getSearchResult();

        request.setAttribute("issueForm", issueForm);
        request.setAttribute("searchResult", searchResult);

        if (issueFormId == 0)
        {
            request.getSession().setAttribute("issueForm", issueForm);
            request.getSession().setAttribute("searchResult", searchResult);
        }
        request.getRequestDispatcher("/webroot/issueTable.jsp").forward(request, response);
    }

    @Route(tab1 = "search", tab2 = "", tab3 = "", action = "search")
    public static void search(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        Long issueFormId = Common.stringToLong(request.getParameter("filterId"));
        IssueForm issueForm = IssueForm.getById(issueFormId);
        if (issueForm == null)
            issueForm = new IssueForm();

        issueForm = updateIssueFormFromRequest(issueForm, request);

        if (issueFormId == 0)
            request.getSession().setAttribute("issueForm", issueForm);
        else
            request.setAttribute("issueForm", issueForm);

        response.sendRedirect("view?tab1=search&action=form&issueFormId=" + issueFormId);
    }

    private static IssueForm updateIssueFormFromRequest(IssueForm issueForm, HttpServletRequest request)
    {
        String filterName       = Common.getSafeString(request.getParameter("filterName"));
        String containsText     = Common.getSafeString(request.getParameter("containsText"));
        String title            = Common.getSafeString(request.getParameter("title"));
        String description      = Common.getSafeString(request.getParameter("description"));
        String statusIds        = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("statusIds")));
        String severityIds      = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("severityIds")));
        String projectIds       = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("projectIds")));
        String groupIds         = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("groupIds")));
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

        issueForm.updateFields(filterName, userSession.getUserId(), containsText, title, description, statusIds, severityIds, projectIds, groupIds, assigneeIds, createdOn, lastUpdatedOn);

        issueForm.setSortColumn(sortColumn);
        issueForm.setSortDirection(sortDirection);
        issueForm.setPage(page);

        return issueForm;
    }

    @Route(tab1 = "search", tab2 = "", tab3 = "", action = "saveIssueForm")
    public static void saveIssueForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long issueFormId = Common.stringToLong(request.getParameter("filterId"));
        IssueForm issueForm = IssueForm.getById(issueFormId);
        if (issueForm == null)
        {
            issueForm = new IssueForm();
            issueForm = updateIssueFormFromRequest(issueForm, request);
            issueFormId = EOI.insert(issueForm, userSession);
        }
        else
        {
            issueForm = updateIssueFormFromRequest(issueForm, request);
            EOI.update(issueForm, userSession);
        }

        response.sendRedirect("view?tab1=search&action=form&issueFormId=" + issueFormId);
    }

    @Route(tab1 = "search", tab2 = "", tab3 = "", action = "addToDashboard")
    public static void addToDashboard(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        long id = Common.stringToLong(request.getParameter("issueFormId"));
        IssueForm issueForm = IssueForm.getById(id);
        if (issueForm != null)
        {
            issueForm.setOnDash(true);
            EOI.update(issueForm, userSession);
        }

        response.sendRedirect("view?tab1=search&action=form&issueFormId=" + id);
    }

    public static SearchResult performSearch(IssueForm issueForm) throws ParseException, IOException
    {
        long resultsPerPage = 20;

        if (issueForm.getSortColumn().length() == 0) issueForm.setSortColumn("id");
        if (issueForm.getSortDirection().length() == 0) issueForm.setSortDirection("asc");
        if (issueForm.getPage().length() == 0) issueForm.setPage("1");

        PSIngredients filmQuery = IssueForm.buildSQLQuery(issueForm, resultsPerPage);
        String countVersionOfQuery = SQLGenerator.getCountVersionOfQuery(filmQuery.query);

        List countResult = EOI.executeQueryOneResult(countVersionOfQuery, filmQuery.args);
        long resultSize = (Long) countResult.get(0);
        List<Object> filteredIssues = EOI.executeQuery(filmQuery.query, filmQuery.args);

        return new SearchResult(issueForm.getPage(), filteredIssues, resultSize, resultsPerPage);
    }
}
