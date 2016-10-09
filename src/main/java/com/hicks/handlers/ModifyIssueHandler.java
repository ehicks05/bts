package com.hicks.handlers;

import com.hicks.EmailEngine;
import com.hicks.UserSession;
import com.hicks.beans.*;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ModifyIssueHandler
{
    public static String showModifyIssue(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long issueId = Common.stringToLong(request.getParameter("issueId"));

        request.setAttribute("issue", Issue.getById(issueId));
        request.setAttribute("comments", Comment.getByIssueId(issueId));
        request.setAttribute("watcherMaps", WatcherMap.getByIssueId(issueId));

        List<User> potentialWatchers = User.getAll();
        potentialWatchers.removeAll(WatcherMap.getWatchersForIssue(issueId));
        request.setAttribute("potentialWatchers", potentialWatchers);

        request.setAttribute("potentialAssignees", User.getAll());
        request.setAttribute("potentialReporters", User.getAll());

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
        Long statusId       = !fieldName.equals("fldStatus") ? 0 : Common.stringToLong(fieldValue);
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
        if (statusId != 0)
        {
            issue.setStatusId(statusId);
            updateLog += "Status to " + Status.getById(statusId).getName();
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

    public static void addComment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
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
        long commentId = EOI.insert(comment);

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setUserId(userSession.getUserId());
        emailMessage.setIssueId(issueId);
        emailMessage.setAction("added a comment");
        emailMessage.setActionSourceId(commentId);
        emailMessage.setDescription(content);
        EOI.insert(emailMessage);

        EmailEngine.sendEmail(emailMessage);

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
}
