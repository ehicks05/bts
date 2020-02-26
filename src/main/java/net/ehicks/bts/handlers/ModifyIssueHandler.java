package net.ehicks.bts.handlers;

import net.ehicks.bts.MyRevisionEntity;
import net.ehicks.bts.beans.*;
import net.ehicks.bts.mail.EmailAction;
import net.ehicks.bts.mail.MailClient;
import net.ehicks.bts.model.IssueAudit;
import net.ehicks.common.Common;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ModifyIssueHandler
{
    private static final Logger log = LoggerFactory.getLogger(ModifyIssueHandler.class);

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private IssueTypeRepository issueTypeRepository;
    private ProjectRepository projectRepository;
    private StatusRepository statusRepository;
    private SeverityRepository severityRepository;
    private IssueRepository issueRepository;
    private CommentRepository commentRepository;
    private EmailEventRepository emailEventRepository;
    private BtsSystemRepository btsSystemRepository;
    private MailClient mailClient;
    private EntityManager entityManager;

    public ModifyIssueHandler(UserRepository userRepository, GroupRepository groupRepository,
                              IssueTypeRepository issueTypeRepository, ProjectRepository projectRepository,
                              StatusRepository statusRepository, SeverityRepository severityRepository,
                              IssueRepository issueRepository, CommentRepository commentRepository,
                              EmailEventRepository emailEventRepository, BtsSystemRepository btsSystemRepository,
                              MailClient mailClient, EntityManager entityManager)
    {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.projectRepository = projectRepository;
        this.statusRepository = statusRepository;
        this.severityRepository = severityRepository;
        this.issueRepository = issueRepository;
        this.commentRepository = commentRepository;
        this.emailEventRepository = emailEventRepository;
        this.btsSystemRepository = btsSystemRepository;
        this.mailClient = mailClient;
        this.entityManager = entityManager;
    }

    @GetMapping("/issue/form")
    public ModelAndView showModifyIssue(@AuthenticationPrincipal User user, @RequestParam Long issueId)
    {
        ModelAndView mav = new ModelAndView("issueForm");
        issueRepository.findById(issueId).ifPresent(issue -> {
            List<User> users = userRepository.findAll();
            Set<Comment> comments = retainVisibleComments(issue.getComments(), user);
            mav.addObject("issue", issue)
                    .addObject("comments", comments)
                    .addObject("potentialWatchers", users.stream().filter(aUser -> !issue.getWatchers().contains(aUser)).collect(Collectors.toList()))
                    .addObject("potentialAssignees", users)
                    .addObject("potentialReporters", users)
                    .addObject("defaultAvatar", btsSystemRepository.findFirstBy().getDefaultAvatar())
                    .addObject("projects", projectRepository.findAll())
                    .addObject("groups", groupRepository.findAll())
                    .addObject("severities", severityRepository.findAll())
                    .addObject("statuses", statusRepository.findAll())
                    .addObject("issueTypes", issueTypeRepository.findAll());
        });

        return mav;
    }

    @GetMapping("/issue/ajaxGetChangeLog")
    @ResponseBody
    public ModelAndView ajaxGetChangeLog(@RequestParam Long issueId)
    {
        // todo get audit info

        List<IssueAudit> issueAudits = new ArrayList<>();

        List<Object[]> revisions = (List<Object[]>) AuditReaderFactory.get(entityManager).createQuery()
                .forRevisionsOfEntityWithChanges(Issue.class, true)
                .add(AuditEntity.id().eq(issueId))
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();

        Issue previous = null;
        for (Object[] revision : revisions)
        {
            DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) revision[1];
            MyRevisionEntity myRevisionEntity = AuditReaderFactory.get(entityManager).findRevision(MyRevisionEntity.class, revisionEntity.getId());

            User user = userRepository.findByUsername(myRevisionEntity.getUsername()).orElse(null);
            issueAudits.add(new IssueAudit(user, previous, (Issue) revision[0], myRevisionEntity, (RevisionType) revision[2], (HashSet<String>) revision[3]));
            previous = (Issue) revision[0];
        }

        return new ModelAndView("issueChangelog")
                .addObject("issueAudits", issueAudits);
    }

    private Set<Comment> retainVisibleComments(Set<Comment> comments, User user)
    {
        if (user.isAdmin() || user.isSupport())
            return comments;
        else
        {
            comments.removeIf(comment -> {
                Group visibleToGroup = comment.getVisibleToGroup();
                return visibleToGroup != null && !user.getGroups().contains(visibleToGroup);
            });
            return comments;
        }
    }

    @PostMapping("/issue/create")
    public ModelAndView createIssue(
            @AuthenticationPrincipal User user,
            @RequestParam Long createIssueProject,
            @RequestParam Long createIssueGroup,
            @RequestParam Long createIssueIssueType,
            @RequestParam Long createIssueSeverity,
            @RequestParam Long createIssueStatus,
            @RequestParam String createIssueTitle,
            @RequestParam String createIssueDescription
    )
    {
        Issue issue = new Issue(0, createIssueTitle, createIssueDescription,
                groupRepository.findById(createIssueGroup).get(),
                issueTypeRepository.findById(createIssueIssueType).get(),
                projectRepository.findById(createIssueProject).get(),
                user,
                user,
                severityRepository.findById(createIssueSeverity).get(),
                statusRepository.findById(createIssueStatus).get()
                );
        issue.getWatchers().add(user);
        issue = issueRepository.save(issue);

        return new ModelAndView("redirect:/issue/form?issueId=" + issue.getId());
    }

    @PostMapping("/issue/update")
    @ResponseBody
    public String updateIssue(@RequestParam Long issueId, @RequestParam String fldFieldName, @RequestParam String fldFieldValue)
    {
        Long projectId      = !fldFieldName.equals("fldProject") ? 0 : Common.stringToLong(fldFieldValue);
        Long issueTypeId    = !fldFieldName.equals("fldIssueType") ? 0 : Common.stringToLong(fldFieldValue);
        Long statusId       = !fldFieldName.equals("fldStatus") ? 0 : Common.stringToLong(fldFieldValue);
        Long severityId     = !fldFieldName.equals("fldSeverity") ? 0 : Common.stringToLong(fldFieldValue);
        Long groupId        = !fldFieldName.equals("fldGroup") ? 0 : Common.stringToLong(fldFieldValue);
        String title        = !fldFieldName.equals("fldTitle") ? "" : Common.getSafeString(fldFieldValue);
        String description  = !fldFieldName.equals("fldDescription") ? "" : Common.getSafeString(fldFieldValue);
        Long assigneeId     = !fldFieldName.equals("fldAssigneeId") ? 0 : Common.stringToLong(fldFieldValue);
        Long reporterId     = !fldFieldName.equals("fldReporterId") ? 0 : Common.stringToLong(fldFieldValue);

        String updateLog = "";
        Issue issue = issueRepository.findById(issueId).get();
        if (projectId != 0)
        {
            issue.setProject(projectRepository.findById(projectId).get());
            updateLog += "Project to " + issue.getProject().getName();
        }
        if (issueTypeId != 0)
        {
            issue.setIssueType(issueTypeRepository.findById(issueTypeId).get());
            updateLog += "Type to " + issue.getIssueType().getName();
        }
        if (statusId != 0)
        {
            issue.setStatus(statusRepository.findById(statusId).get());
            updateLog += "Status to " + issue.getStatus().getName();
        }
        if (severityId != 0)
        {
            issue.setSeverity(severityRepository.findById(severityId).get());
            updateLog += "Severity to " + issue.getSeverity().getName();
        }
        if (groupId != 0)
        {
            issue.setGroup(groupRepository.findById(groupId).get());
            updateLog += "Group to " + issue.getGroup().getName();
        }
        if (title.length() != 0)
        {
            issue.setTitle(title);
            updateLog += "Title";
        }
        if (description.length() != 0)
        {
            issue.setDescription(description);
            updateLog += "Description";
        }
        if (assigneeId != 0)
        {
            issue.setAssignee(userRepository.findById(assigneeId).get());
            updateLog += "Assignee to " + issue.getAssignee().getUsername();
        }
        if (reporterId != 0)
        {
            issue.setReporter(userRepository.findById(reporterId).get());
            updateLog += "Reporter to " + issue.getReporter().getUsername();
        }

        if (updateLog.length() > 0)
            issue.setLastUpdatedOn(LocalDateTime.now());

        issueRepository.save(issue);

        return "Updated " + updateLog;
    }

    @PostMapping("/issue/addComment")
    public ModelAndView addComment(@AuthenticationPrincipal User user, @RequestParam Long issueId,
                                   @RequestParam String fldContent, @RequestParam Long fldVisibility)
    {
        Issue issue = issueRepository.findById(issueId).get();
        Group group = fldVisibility == null ? issue.getGroup() : groupRepository.findById(fldVisibility).get();
        Comment comment = new Comment(0, issue, user, fldContent, group, LocalDateTime.now(), LocalDateTime.now());
        comment = commentRepository.save(comment);

        EmailEvent emailEvent = new EmailEvent();
        emailEvent.setUser(user);
        emailEvent.setIssue(issue);
        emailEvent.setActionId(EmailAction.ADD_COMMENT.getId());
        emailEvent.setComment(comment);
        emailEvent.setDescription(fldContent);
        emailEvent = emailEventRepository.save(emailEvent);

        mailClient.prepareAndSend(emailEvent);

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }

    @PostMapping("/issue/updateComment")
    @ResponseBody
    public String updateComment(@AuthenticationPrincipal User user, @RequestParam Long commentId,
                                      @RequestParam String fldFieldName, @RequestParam String fldFieldValue)
    {
        Comment comment = commentRepository.findById(commentId).get();
        if (user.getId() != comment.getAuthor().getId())
        {
            // url hack attempt?
            return "";
        }

        String content = !fldFieldName.equals("fldContent" + commentId) ? "" : Common.getSafeString(fldFieldValue);

        String previousContent = comment.getContent();
        if (content.length() != 0)
            comment.setContent(content);

        String result = "";
        if (!content.equals(previousContent))
        {
            comment.setLastUpdatedOn(LocalDateTime.now());
            commentRepository.save(comment);

            result = "Comment Updated";
        }

        EmailEvent emailEvent = new EmailEvent();
        emailEvent.setUser(user);
        emailEvent.setIssue(comment.getIssue());
        emailEvent.setActionId(EmailAction.EDIT_COMMENT.getId());
        emailEvent.setComment(comment);
        emailEvent.setDescription("");
        emailEvent.setPreviousValue(previousContent);
        emailEvent.setNewValue(content);
        emailEvent = emailEventRepository.save(emailEvent);

        mailClient.prepareAndSend(emailEvent);
        return result;
    }

    @GetMapping("/issue/addWatcher")
    public ModelAndView addWatcher(@RequestParam Long issueId, @RequestParam Long userId)
    {
        issueRepository.findById(issueId).ifPresent(issue ->
                userRepository.findById(userId).ifPresent(user -> {
                    issue.getWatchers().add(user);
                    issueRepository.save(issue);
                }));

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }

    @GetMapping("/issue/removeWatcher")
    public ModelAndView removeWatcher(@RequestParam Long issueId, @RequestParam Long userId)
    {
        issueRepository.findById(issueId).ifPresent(issue ->
                userRepository.findById(userId).ifPresent(user -> {
                    issue.getWatchers().remove(user);
                    issueRepository.save(issue);
                }));

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }
}
