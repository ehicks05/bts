package com.hicks;

import com.hicks.beans.Comment;
import com.hicks.beans.Issue;
import com.hicks.beans.User;
import com.hicks.beans.WatcherMap;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.PSIngredients;
import net.ehicks.eoi.SQLGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class IssuesHandler
{
    public static String showIssues(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        SearchResult searchResult = (SearchResult) request.getSession().getAttribute("searchResult");
        if (searchResult == null)
        {
            // set some defaults
            IssuesForm issuesForm = new IssuesForm("", "", "", "", "", 0L, 0L);
            request.getSession().setAttribute("issuesForm", issuesForm);

            searchResult = performSearch(request, issuesForm);
            request.getSession().setAttribute("searchResult", searchResult);
        }

        request.setAttribute("uniqueLanguages", null);
        request.setAttribute("uniqueGenres", null);

        return "/WEB-INF/webroot/issuesList.jsp";
    }

    public static String showModifyIssue(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long issueId = Common.stringToLong(request.getParameter("issueId"));
        Issue issue = Issue.getById(issueId);

        request.setAttribute("issue", issue);
        List<Comment> comments = Comment.getByIssueId(issueId);
        request.setAttribute("comments", comments);
        List<User> watchers = WatcherMap.getWatchersForIssue(issueId);
        request.setAttribute("watchers", watchers);

        return "/WEB-INF/webroot/issueForm.jsp";
    }

    public static void createIssue(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        Long projectId      = Common.stringToLong(request.getParameter("fldProject"));
        Long zoneId         = Common.stringToLong(request.getParameter("fldZone"));
        Long issueTypeId    = Common.stringToLong(request.getParameter("fldIssueType"));
        String title        = Common.getSafeString(request.getParameter("fldTitle"));

        Issue issue = new Issue();
        issue.setProjectId(projectId);
        issue.setZoneId(zoneId);
        issue.setIssueTypeId(issueTypeId);
        issue.setTitle(title);
        Long newKey = EOI.insert(issue);

        response.sendRedirect("view?tab1=main&tab2=issue&action=form&issueId=" + newKey);
    }

    public static void updateIssue(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        Long projectId      = Common.stringToLong(request.getParameter("fldProject"));
        Long zoneId         = Common.stringToLong(request.getParameter("fldZone"));
        Long issueTypeId    = Common.stringToLong(request.getParameter("fldIssueType"));
        String title        = Common.getSafeString(request.getParameter("fldTitle"));

        Issue issue = new Issue();
        issue.setProjectId(projectId);
        issue.setZoneId(zoneId);
        issue.setIssueTypeId(issueTypeId);
        issue.setTitle(title);
        Long newKey = EOI.insert(issue);

        response.sendRedirect("view?tab1=main&tab2=issue&action=form&issueId=" + newKey);
    }

    public static void search(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        String id           = Common.getSafeString(request.getParameter("id"));
        String title        = Common.getSafeString(request.getParameter("title"));
        String description  = Common.getSafeString(request.getParameter("description"));
        String severity     = Common.getSafeString(request.getParameter("severity"));
        String status       = Common.getSafeString(request.getParameter("status"));
        Long bucketId       = Common.stringToLong(request.getParameter("bucketId"));
        Long zoneId         = Common.stringToLong(request.getParameter("zoneId"));

        IssuesForm issuesForm = new IssuesForm(id, title, description, severity, status, bucketId, zoneId);
        if (Common.getSafeString(request.getParameter("resetPage")).equals("yes"))
            issuesForm.setPage("1");

        SearchResult searchResult = performSearch(request, issuesForm);
        request.getSession().setAttribute("issuesForm", issuesForm);
        request.getSession().setAttribute("searchResult", searchResult);

        response.sendRedirect("view?action=form");
    }

    private static SearchResult performSearch(HttpServletRequest request, IssuesForm issuesForm) throws ParseException, IOException
    {
        // parse sorting fields
        String sortColumn = issuesForm.getSortColumn();
        String sortDirection = issuesForm.getSortDirection();
        if (sortColumn == null)
        {
            if (request.getParameter("sortColumn") == null)
            {
                sortColumn = "id";
                sortDirection = "asc";
            }
            else
                sortColumn = request.getParameter("sortColumn");
        }

        String directionParam = request.getParameter("sortDirection");
        if (sortDirection == null) sortDirection = directionParam == null ? "asc" : directionParam;

        String page = issuesForm.getPage();
        if (page == null)
        {
            page = request.getParameter("page");
            if (page == null) page = "1";
        }

        long resultsPerPage = 10;
        PSIngredients filmQuery = buildFilmSQLQuery(issuesForm, sortColumn, sortDirection, page, resultsPerPage);
        String countVersionOfQuery = SQLGenerator.getCountVersionOfQuery(filmQuery.query);

        List result = EOI.executeQueryOneResult(countVersionOfQuery, filmQuery.args);
        long resultSize = (Long) result.get(0);
        List<Issue> filteredIssues = EOI.executeQuery(filmQuery.query, filmQuery.args);

        return new SearchResult(page, filteredIssues, sortColumn, sortDirection, resultSize, resultsPerPage);
    }

    private static PSIngredients buildFilmSQLQuery(IssuesForm issuesForm, String sortColumn, String sortDirection, String page, long resultsPerPage)
    {
        List<Object> args = new ArrayList<>();
        String selectClause = "select * from issues where ";
        String whereClause = "";

        if (issuesForm.getId().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(id) like ? ";
            args.add(issuesForm.getId().toLowerCase().replaceAll("\\*","%"));
        }

        if (issuesForm.getTitle().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(title) like ? ";
            args.add("%" + issuesForm.getTitle().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (issuesForm.getDescription().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(description) like ? ";
            args.add("%" + issuesForm.getDescription().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (args.size() == 0) selectClause = selectClause.replace("where", "");

        String orderByClause = "";
        if (sortColumn.length() > 0)
        {
            orderByClause += " order by " + sortColumn + " " + sortDirection + ", id nulls last " ;
        }

        long limit = resultsPerPage;
        String offset = String.valueOf((Integer.valueOf(page) - 1) * limit);
        String paginationClause = " limit " + limit + " offset " + offset;

        String completeQuery = selectClause + whereClause + orderByClause + paginationClause;
        return new PSIngredients(completeQuery, args);
    }
}
