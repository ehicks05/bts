package net.ehicks.bts.handlers;

import com.sksamuel.diffpatch.DiffMatchPatch;
import net.ehicks.bts.*;
import net.ehicks.bts.beans.*;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ModifyIssueHandler
{
    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "form")
    public static String showModifyIssue(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long issueId = Common.stringToLong(request.getParameter("issueId"));

        if (Issue.getById(issueId) == null)
            return "/webroot/error.jsp";
        
        // security
        if (!userSession.getUser().hasAccess(Issue.getById(issueId)))
            return "/webroot/error.jsp";

        List<Comment> comments = Comment.getByIssueId(issueId);
        retainVisibleComments(comments, (UserSession) request.getSession().getAttribute("userSession"));

        request.setAttribute("issue", Issue.getById(issueId));
        request.setAttribute("comments", comments);
        request.setAttribute("watcherMaps", WatcherMap.getByIssueId(issueId));

        List<User> potentialWatchers = User.getAll();
        potentialWatchers.removeAll(WatcherMap.getWatchersForIssue(issueId));
        request.setAttribute("potentialWatchers", potentialWatchers);

        request.setAttribute("potentialAssignees", User.getAll());
        request.setAttribute("potentialReporters", User.getAll());

        return "/webroot/issueForm.jsp";
    }

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "ajaxGetChangeLog")
    public static String ajaxGetChangeLog(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Long issueId = Common.stringToLong(request.getParameter("issueId"));

        request.setAttribute("issueAudits", IssueAudit.getByIssueId(issueId));

        return "/webroot/issueChangelog.jsp";
    }

    private static List<Comment> retainVisibleComments(List<Comment> comments, UserSession userSession)
    {
        if (userSession.getUser().isAdmin() || userSession.getUser().isSupport())
            return comments;
        else
        {
            List<Group> userGroups = Group.getByUserId(userSession.getUserId());
            List<Long> userGroupIds = userGroups.stream().map(Group::getId).collect(Collectors.toList());
            for (Iterator<Comment> i = comments.iterator(); i.hasNext();)
            {
                Comment comment = i.next();
                Long visibleToGroup = comment.getVisibleToGroupId();
                if (visibleToGroup != 0 && !userGroupIds.contains(comment.getCreatedByUserId()))
                    i.remove();
            }
            return comments;
        }
    }

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "create")
    public static void createIssue(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long projectId      = Common.stringToLong(request.getParameter("createIssueProject"));
        Long groupId        = Common.stringToLong(request.getParameter("createIssueGroup"));
        Long issueTypeId    = Common.stringToLong(request.getParameter("createIssueIssueType"));
        Long severityId     = Common.stringToLong(request.getParameter("createIssueSeverity"));
        Long statusId       = Common.stringToLong(request.getParameter("createIssueStatus"));
        String title        = Common.getSafeString(request.getParameter("createIssueTitle"));
        String description  = Common.getSafeString(request.getParameter("createIssueDescription"));

        Issue issue = new Issue();
        issue.setReporterUserId(userSession.getUserId());
        issue.setAssigneeUserId(userSession.getUserId());
        issue.setProjectId(projectId);
        issue.setGroupId(groupId);
        issue.setIssueTypeId(issueTypeId);
        issue.setSeverityId(severityId);
        issue.setStatusId(statusId);
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setCreatedOn(new Date());
        Long newIssueId = EOI.insert(issue, userSession);
        issue = Issue.getById(newIssueId);

        WatcherMap watcherMap = new WatcherMap();
        watcherMap.setIssueId(newIssueId);
        watcherMap.setUserId(userSession.getUserId());
        EOI.insert(watcherMap, userSession);

        IssueAudit issueAudit = new IssueAudit(newIssueId, userSession, "added", issue.toString());
        EOI.insert(issueAudit, userSession);

        response.sendRedirect("view?tab1=issue&action=form&issueId=" + newIssueId);
    }

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "update")
    public static void updateIssue(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long issueId        = Common.stringToLong(request.getParameter("issueId"));
        String fieldName    = Common.getSafeString(request.getParameter("fldFieldName"));
        String fieldValue   = Common.getSafeString(request.getParameter("fldFieldValue"));

        Long projectId      = !fieldName.equals("fldProject") ? 0 : Common.stringToLong(fieldValue);
        Long issueTypeId    = !fieldName.equals("fldIssueType") ? 0 : Common.stringToLong(fieldValue);
        Long statusId       = !fieldName.equals("fldStatus") ? 0 : Common.stringToLong(fieldValue);
        Long severityId     = !fieldName.equals("fldSeverity") ? 0 : Common.stringToLong(fieldValue);
        Long groupId        = !fieldName.equals("fldGroup") ? 0 : Common.stringToLong(fieldValue);
        String title        = !fieldName.equals("fldTitle") ? "" : Common.getSafeString(fieldValue);
        String description  = !fieldName.equals("fldDescription") ? "" : Common.getSafeString(fieldValue);
        Long assigneeId     = !fieldName.equals("fldAssigneeId") ? 0 : Common.stringToLong(fieldValue);
        Long reporterId     = !fieldName.equals("fldReporterId") ? 0 : Common.stringToLong(fieldValue);

        String updateLog = "";
        Issue issue = Issue.getById(issueId);
        String oldValue = "";
        String newValue = "";
        if (projectId != 0)
        {
            oldValue = Project.getById(issue.getProjectId()).getName();
            newValue = Project.getById(projectId).getName();

            issue.setProjectId(projectId);
            updateLog += "Project to " + Project.getById(projectId).getName();
        }
        if (issueTypeId != 0)
        {
            oldValue = IssueType.getById(issue.getIssueTypeId()).getName();
            newValue = IssueType.getById(issueTypeId).getName();

            issue.setIssueTypeId(issueTypeId);
            updateLog += "Type to " + IssueType.getById(issueTypeId).getName();
        }
        if (statusId != 0)
        {
            oldValue = Status.getById(issue.getStatusId()).getName();
            newValue = Status.getById(statusId).getName();

            issue.setStatusId(statusId);
            updateLog += "Status to " + Status.getById(statusId).getName();
        }
        if (severityId != 0)
        {
            oldValue = Severity.getById(issue.getSeverityId()).getName();
            newValue = Severity.getById(severityId).getName();

            issue.setSeverityId(severityId);
            updateLog += "Severity to " + Severity.getById(severityId).getName();
        }
        if (groupId != 0)
        {
            oldValue = Group.getById(issue.getGroupId()).getName();
            newValue = Group.getById(groupId).getName();

            issue.setGroupId(groupId);
            updateLog += "Group to " + Group.getById(groupId).getName();
        }
        if (title.length() != 0)
        {
            oldValue = issue.getTitle();
            newValue = title;

            issue.setTitle(title);
            updateLog += "Title";
        }
        if (description.length() != 0)
        {
            oldValue = issue.getDescription();
            newValue = description;

            issue.setDescription(description);
            updateLog += "Description";
        }
        if (assigneeId != 0)
        {
            oldValue = issue.getAssignee().getLogonId();
            newValue = User.getByUserId(assigneeId).getLogonId();

            issue.setAssigneeUserId(assigneeId);
            updateLog += "Assignee to " + User.getByUserId(assigneeId).getLogonId();
        }
        if (reporterId != 0)
        {
            oldValue = issue.getReporter().getLogonId();
            newValue = User.getByUserId(reporterId).getLogonId();

            issue.setReporterUserId(reporterId);
            updateLog += "Reporter to " + User.getByUserId(reporterId).getLogonId();
        }

        if (updateLog.length() > 0)
        {
            issue.setLastUpdatedOn(new Date());
        }

        EOI.update(issue, userSession);

        IssueAudit issueAudit = new IssueAudit(issueId, userSession, "changed", issue.toString(), fieldName.replace("fld", ""), oldValue, newValue);
        EOI.insert(issueAudit, userSession);

        String toastMessage = "Updated " + updateLog;
        response.getWriter().println(toastMessage);
    }

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "addComment")
    public static void addComment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long issueId    = Common.stringToLong(request.getParameter("issueId"));
        String content  = Common.getSafeString(request.getParameter("fldContent"));
        Long visibility = Common.stringToLong(request.getParameter("fldVisibility"));

        Comment comment = new Comment();
        comment.setIssueId(issueId);
        comment.setCreatedByUserId(userSession.getUserId());
        comment.setCreatedOn(new Date());
        comment.setContent(content);
        comment.setVisibleToGroupId(visibility);
        long commentId = EOI.insert(comment, userSession);
        comment = Comment.getById(commentId); // comment should now have id field populated

        IssueAudit issueAudit = new IssueAudit(issueId, userSession, "added", comment.toString());
        EOI.insert(issueAudit, userSession);

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setUserId(userSession.getUserId());
        emailMessage.setIssueId(issueId);
        emailMessage.setActionId(EmailAction.ADD_COMMENT.getId());
        emailMessage.setCommentId(commentId);
        emailMessage.setDescription(content);
        long emailId = EOI.insert(emailMessage, userSession);
        emailMessage = EmailMessage.getById(emailId);

        EmailEngine.sendEmail(emailMessage);

        response.sendRedirect("view?tab1=issue&action=form&issueId=" + issueId);
    }

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "updateComment")
    public static void updateComment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long commentId        = Common.stringToLong(request.getParameter("commentId"));
        Comment comment = Comment.getById(commentId);
        if (!userSession.getUserId().equals(comment.getCreatedByUserId()))
        {
            // url hack attempt?
            return;
        }
        
        String fieldName    = Common.getSafeString(request.getParameter("fldFieldName"));
        String fieldValue   = Common.getSafeString(request.getParameter("fldFieldValue"));

        String content       = !fieldName.equals("fldContent" + commentId) ? "" : Common.getSafeString(fieldValue);

        String previousContent = comment.getContent();
        if (content.length() != 0)
            comment.setContent(content);

        if (!content.equals(previousContent))
        {
            comment.setLastUpdatedOn(new Date());
            EOI.update(comment, userSession);

            String toastMessage = "Comment Updated";
            response.getWriter().println(toastMessage);

            IssueAudit issueAudit = new IssueAudit(comment.getIssueId(), userSession, "changed", comment.toString(), "content", previousContent, content);
            EOI.insert(issueAudit, userSession);
        }

        DiffMatchPatch myDiff = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diffs = myDiff.diff_main(previousContent, content);
        myDiff.diff_cleanupSemantic(diffs);
        String prettyDiff = myDiff.diff_prettyHtml(diffs);

        // todo this is for yahoo mail
        prettyDiff = prettyDiff.replaceAll("<ins style=\"background:#e6ffe6;\">", "<u style=\"background:#e6ffe6;\">");
        prettyDiff = prettyDiff.replaceAll("</ins>", "</u>");

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setUserId(userSession.getUserId());
        emailMessage.setIssueId(comment.getIssueId());
        emailMessage.setActionId(EmailAction.EDIT_COMMENT.getId());
        emailMessage.setCommentId(commentId);
        emailMessage.setDescription(prettyDiff);
        long emailId = EOI.insert(emailMessage, userSession);
        emailMessage = EmailMessage.getById(emailId);

        EmailEngine.sendEmail(emailMessage);
    }

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "addWatcher")
    public static void addWatcher(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long issueId    = Common.stringToLong(request.getParameter("issueId"));
        Long userId    = Common.stringToLong(request.getParameter("userId"));

        WatcherMap watcherMap = new WatcherMap();
        watcherMap.setIssueId(issueId);
        watcherMap.setUserId(userId);
        EOI.insert(watcherMap, userSession);

        response.sendRedirect("view?tab1=issue&action=form&issueId=" + issueId);
    }

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "removeWatcher")
    public static void removeWatcher(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long issueId    = Common.stringToLong(request.getParameter("issueId"));
        Long watcherMapId    = Common.stringToLong(request.getParameter("watcherMapId"));

        WatcherMap watcherMap = WatcherMap.getById(watcherMapId);
        EOI.executeDelete(watcherMap, userSession);

        response.sendRedirect("view?tab1=issue&action=form&issueId=" + issueId);
    }
}
