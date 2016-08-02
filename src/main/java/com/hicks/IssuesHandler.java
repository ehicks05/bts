package com.hicks;

import com.hicks.beans.*;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.PSIngredients;
import net.ehicks.eoi.SQLGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class IssuesHandler
{
    public static String showIssues(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        SearchResult searchResult = (SearchResult) request.getSession().getAttribute("searchResult");
        if (searchResult == null)
        {
            // set some defaults
            IssueForm issueForm = new IssueForm(0L, 0L, "", "", "", "", 0L, 0L, null, null);
            request.getSession().setAttribute("issuesForm", issueForm);

            searchResult = performSearch(request, issueForm);
            request.getSession().setAttribute("searchResult", searchResult);
        }

        return "/WEB-INF/webroot/issuesList.jsp";
    }

    public static String showModifyIssue(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long issueId = Common.stringToLong(request.getParameter("issueId"));
        Issue issue = Issue.getById(issueId);

        request.setAttribute("issue", issue);
        List<Comment> comments = Comment.getByIssueId(issueId);
        request.setAttribute("comments", comments);
        List<WatcherMap> watcherMaps = WatcherMap.getByIssueId(issueId);
        request.setAttribute("watcherMaps", watcherMaps);

        List<User> potentialWatchers = User.getAllUsers();
        potentialWatchers.removeAll(WatcherMap.getWatchersForIssue(issueId));
        request.setAttribute("potentialWatchers", potentialWatchers);

        List<User> potentialAssignees = User.getAllUsers();
//        potentialAssignees.remove(issue.getAssignee());
        request.setAttribute("potentialAssignees", potentialAssignees);

        List<User> potentialReporters = User.getAllUsers();
//        potentialReporters.remove(issue.getReporter());
        request.setAttribute("potentialReporters", potentialReporters);

        return "/WEB-INF/webroot/issueForm.jsp";
    }

    public static void createIssue(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long projectId      = Common.stringToLong(request.getParameter("fldProject"));
        Long zoneId         = Common.stringToLong(request.getParameter("fldZone"));
        Long issueTypeId    = Common.stringToLong(request.getParameter("fldIssueType"));
        Long severityId     = Common.stringToLong(request.getParameter("fldSeverity"));
        String title        = Common.getSafeString(request.getParameter("fldTitle"));
        String description  = Common.getSafeString(request.getParameter("fldDescription"));

        Issue issue = new Issue();
        issue.setReporterUserId(userSession.getUserId());
        issue.setProjectId(projectId);
        issue.setZoneId(zoneId);
        issue.setIssueTypeId(issueTypeId);
        issue.setSeverityId(severityId);
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setCreatedOn(new Date());
        Long newIssueId = EOI.insert(issue);

        WatcherMap watcherMap = new WatcherMap();
        watcherMap.setIssueId(newIssueId);
        watcherMap.setUserId(userSession.getUserId());
        EOI.insert(watcherMap);

        response.sendRedirect("view?tab1=main&tab2=issue&action=form&issueId=" + newIssueId);
    }

    public static void updateIssue(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        Long issueId        = Common.stringToLong(request.getParameter("issueId"));
        String fieldName    = Common.getSafeString(request.getParameter("fldFieldName"));
        String fieldValue   = Common.getSafeString(request.getParameter("fldFieldValue"));

        Long projectId      = !fieldName.equals("fldProject") ? 0 : Common.stringToLong(fieldValue);
        Long issueTypeId    = !fieldName.equals("fldIssueType") ? 0 : Common.stringToLong(fieldValue);
        String status       = !fieldName.equals("fldStatus") ? "" : Common.getSafeString(fieldValue);
        Long severityId     = !fieldName.equals("fldSeverity") ? 0 : Common.stringToLong(fieldValue);
        Long zoneId         = !fieldName.equals("fldZone") ? 0 : Common.stringToLong(fieldValue);
        String title        = !fieldName.equals("fldTitle") ? "" : Common.getSafeString(fieldValue);
        String description  = !fieldName.equals("fldDescription") ? "" : Common.getSafeString(fieldValue);
        Long assigneeId     = !fieldName.equals("fldAssigneeId") ? 0 : Common.stringToLong(fieldValue);
        Long reporterId     = !fieldName.equals("fldReporterId") ? 0 : Common.stringToLong(fieldValue);

        String updateLog = "";
        Issue issue = Issue.getById(issueId);
        if (projectId != 0)
        {
            issue.setProjectId(projectId);
            updateLog += "ID to " + Project.getById(projectId).getName();
        }
        if (issueTypeId != 0)
        {
            issue.setIssueTypeId(issueTypeId);
            updateLog += "Type to " + IssueType.getById(issueTypeId).getName();
        }
        if (status.length() != 0)
        {
            issue.setStatus(status);
            updateLog += "Status to " + status;

        }
        if (severityId != 0)
        {
            issue.setSeverityId(severityId);
            updateLog += "Severity to " + Severity.getById(severityId).getName();
        }
        if (zoneId != 0)
        {
            issue.setZoneId(zoneId);
            updateLog += "Zone to " + Zone.getById(zoneId).getName();
        }
        if (title.length() != 0)
        {
            issue.setTitle(title);
            updateLog += "Title to " + Common.limit(title, 64);
        }
        if (description.length() != 0)
        {
            issue.setDescription(description);
            updateLog += "Description to " + Common.limit(description, 64);
        }
        if (assigneeId != 0)
        {
            issue.setAssigneeUserId(assigneeId);
            updateLog += "Assignee to " + User.getByUserId(assigneeId).getLogonId();
        }
        if (reporterId != 0)
        {
            issue.setReporterUserId(reporterId);
            updateLog += "Reporter to " + User.getByUserId(reporterId).getLogonId();
        }

        if (updateLog.length() > 0)
        {
            issue.setLastUpdatedOn(new Date());
        }

        EOI.update(issue);

        String toastMessage = "Updated " + updateLog;
        response.getWriter().println(toastMessage);
    }

    public static void search(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        Long id             = Common.stringToLong(request.getParameter("id"));
        String containsText = Common.getSafeString(request.getParameter("containsText"));
        String title        = Common.getSafeString(request.getParameter("title"));
        String description  = Common.getSafeString(request.getParameter("description"));
        String status       = Common.getSafeString(request.getParameter("status"));
        Long severity       = Common.stringToLong(request.getParameter("severity"));
        if (severity == 0) severity = null;
        Long zoneId         = Common.stringToLong(request.getParameter("zoneId"));
        Date createdOn      = Common.stringToDate(request.getParameter("createdOn"));
        Date lastUpdatedOn  = Common.stringToDate(request.getParameter("lastUpdatedOn"));

        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        IssueForm issueForm = new IssueForm(id, userSession.getUserId(), containsText, title, description, status, severity, zoneId, createdOn, lastUpdatedOn);
        if (Common.getSafeString(request.getParameter("resetPage")).equals("yes"))
            issueForm.setPage("1");

        SearchResult searchResult = performSearch(request, issueForm);
        request.getSession().setAttribute("issuesForm", issueForm);
        request.getSession().setAttribute("searchResult", searchResult);

        response.sendRedirect("view?action=form");
    }

    public static void saveIssueForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        Long id             = Common.stringToLong(request.getParameter("id"));
        String containsText = Common.getSafeString(request.getParameter("containsText"));
        String title        = Common.getSafeString(request.getParameter("title"));
        String description  = Common.getSafeString(request.getParameter("description"));
        String status       = Common.getSafeString(request.getParameter("status"));
        Long severity       = Common.stringToLong(request.getParameter("severity"));
        if (severity == 0) severity = null;
        Long zoneId         = Common.stringToLong(request.getParameter("zoneId"));
        Date createdOn      = Common.stringToDate(request.getParameter("createdOn"));
        Date lastUpdatedOn  = Common.stringToDate(request.getParameter("lastUpdatedOn"));

        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        IssueForm issuesForm = new IssueForm(id, userSession.getUserId(), containsText, title, description, status, severity, zoneId, createdOn, lastUpdatedOn);
        EOI.insert(issuesForm);

        response.sendRedirect("view?action=form");
    }

    public static void loadIssueForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        Long issueFormId = Common.stringToLong(request.getParameter("issueFormId"));

        IssueForm issueForm = IssueForm.getById(issueFormId);
        SearchResult searchResult = performSearch(request, issueForm);
        request.getSession().setAttribute("issuesForm", issueForm);
        request.getSession().setAttribute("searchResult", searchResult);

        response.sendRedirect("view?action=form");
    }

    public static SearchResult performSearch(HttpServletRequest request, IssueForm issueForm) throws ParseException, IOException
    {
        // parse sorting fields
        String sortColumn = issueForm.getSortColumn();
        String sortDirection = issueForm.getSortDirection();
        if (sortColumn == null)
        {
            if (request == null || request.getParameter("sortColumn") == null)
            {
                sortColumn = "id";
                sortDirection = "asc";
            }
            else
                sortColumn = request.getParameter("sortColumn");
        }

        String directionParam = request == null ? null : request.getParameter("sortDirection");
        if (sortDirection == null) sortDirection = directionParam == null ? "asc" : directionParam;

        String page = issueForm.getPage();
        if (page == null || page.length() == 0)
        {
            page = request == null ? null : request.getParameter("page");
            if (page == null) page = "1";
        }

        issueForm.setSortColumn(sortColumn);
        issueForm.setSortDirection(sortDirection);
        issueForm.setPage(page);

        long resultsPerPage = 10;
        PSIngredients filmQuery = buildFilmSQLQuery(issueForm, sortColumn, sortDirection, page, resultsPerPage);
        String countVersionOfQuery = SQLGenerator.getCountVersionOfQuery(filmQuery.query);

        List countResult = EOI.executeQueryOneResult(countVersionOfQuery, filmQuery.args);
        long resultSize = (Long) countResult.get(0);
        List<Issue> filteredIssues = EOI.executeQuery(filmQuery.query, filmQuery.args);

        return new SearchResult(page, filteredIssues, resultSize, resultsPerPage);
    }

    public static void addComment(HttpServletRequest request,  HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long issueId    = Common.stringToLong(request.getParameter("issueId"));
        Long zoneId     = Common.stringToLong(request.getParameter("fldZone"));
        String content  = Common.getSafeString(request.getParameter("fldContent"));

        Comment comment = new Comment();
        comment.setIssueId(issueId);
        comment.setZoneId(zoneId);
        comment.setCreatedByUserId(userSession.getUserId());
        comment.setCreatedOn(new Date());
        comment.setContent(content);
        EOI.insert(comment);

        response.sendRedirect("view?tab1=main&tab2=issue&action=form&issueId=" + issueId);
    }


    public static void updateComment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        Long commentId        = Common.stringToLong(request.getParameter("commentId"));
        String fieldName    = Common.getSafeString(request.getParameter("fldFieldName"));
        String fieldValue   = Common.getSafeString(request.getParameter("fldFieldValue"));

        String content       = !fieldName.equals("fldContent" + commentId) ? "" : Common.getSafeString(fieldValue);

        String updateLog = "";
        Comment comment = Comment.getById(commentId);
        if (content.length() != 0)
        {
            comment.setContent(content);
            updateLog += "Content to " + Common.limit(content, 64);
        }

        if (updateLog.length() > 0)
        {
            comment.setLastUpdatedOn(new Date());
        }

        EOI.update(comment);

        String toastMessage = "Updated " + updateLog;
        response.getWriter().println(toastMessage);
    }

    public static void addWatcher(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long issueId    = Common.stringToLong(request.getParameter("issueId"));
        Long userId    = Common.stringToLong(request.getParameter("userId"));

        WatcherMap watcherMap = new WatcherMap();
        watcherMap.setIssueId(issueId);
        watcherMap.setUserId(userId);
        EOI.insert(watcherMap);

        response.sendRedirect("view?tab1=main&tab2=issue&action=form&issueId=" + issueId);
    }

    public static void removeWatcher(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long issueId    = Common.stringToLong(request.getParameter("issueId"));
        Long watcherMapId    = Common.stringToLong(request.getParameter("watcherMapId"));

        WatcherMap watcherMap = WatcherMap.getById(watcherMapId);
        EOI.executeDelete(watcherMap);

        response.sendRedirect("view?tab1=main&tab2=issue&action=form&issueId=" + issueId);
    }

    private static PSIngredients buildFilmSQLQuery(IssueForm issueForm, String sortColumn, String sortDirection, String page, long resultsPerPage)
    {
        List<Object> args = new ArrayList<>();
        String selectClause = "select * from issues where ";
        String whereClause = "";

        if (issueForm.getIssueId() != null && issueForm.getIssueId() != 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(id) like ? ";
            args.add(issueForm.getIssueId());
        }

        if (issueForm.getContainsText().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += "( ";
            whereClause += " lower(id) like ? ";
            whereClause += " or lower(title) like ? ";
            whereClause += " or lower(description) like ? ";
            whereClause += ") ";
            args.add("%" + issueForm.getContainsText().toLowerCase().replaceAll("\\*","%") + "%");
            args.add("%" + issueForm.getContainsText().toLowerCase().replaceAll("\\*","%") + "%");
            args.add("%" + issueForm.getContainsText().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (issueForm.getTitle().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(title) like ? ";
            args.add("%" + issueForm.getTitle().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (issueForm.getDescription().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(description) like ? ";
            args.add("%" + issueForm.getDescription().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (issueForm.getSeverityId() != null && issueForm.getSeverityId() != 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " severity_id like ? ";
            args.add(issueForm.getSeverityId().toString());
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
