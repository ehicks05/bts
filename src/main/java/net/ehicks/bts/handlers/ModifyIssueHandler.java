package net.ehicks.bts.handlers;

import net.ehicks.bts.beans.*;
import net.ehicks.bts.mail.MailClient;
import net.ehicks.bts.model.AuditQueryLogic;
import net.ehicks.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    private IssueEventRepository issueEventRepository;
    private BtsSystemRepository btsSystemRepository;
    private MailClient mailClient;
    private AuditQueryLogic auditQueryLogic;

    public ModifyIssueHandler(UserRepository userRepository, GroupRepository groupRepository,
                              IssueTypeRepository issueTypeRepository, ProjectRepository projectRepository,
                              StatusRepository statusRepository, SeverityRepository severityRepository,
                              IssueRepository issueRepository, CommentRepository commentRepository,
                              IssueEventRepository issueEventRepository, BtsSystemRepository btsSystemRepository,
                              MailClient mailClient, EntityManager entityManager, AuditQueryLogic auditQueryLogic)
    {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.projectRepository = projectRepository;
        this.statusRepository = statusRepository;
        this.severityRepository = severityRepository;
        this.issueRepository = issueRepository;
        this.commentRepository = commentRepository;
        this.issueEventRepository = issueEventRepository;
        this.btsSystemRepository = btsSystemRepository;
        this.mailClient = mailClient;
        this.auditQueryLogic = auditQueryLogic;
    }

    @GetMapping("/issue/form")
    public ModelAndView showModifyIssue(@AuthenticationPrincipal User user, @RequestParam Long issueId, Model model)
    {
        ModelAndView mav = new ModelAndView("issueForm");
        issueRepository.findById(issueId).ifPresent(issue -> {
            List<User> users = userRepository.findAll();
            Set<Comment> comments = retainVisibleComments(issue.getComments(), user);
            IssueEventForm searchForm = new IssueEventForm(0, user, issueId);
            searchForm.setEndpoint("/issue/ajaxGetChangeLog?issueId=" + issueId);

            mav.addObject("issue", issue)
                    .addObject("user", user)
                    .addObject("comments", comments)
                    .addObject("potentialWatchers", users.stream().filter(aUser -> !issue.getWatchers().contains(aUser)).collect(Collectors.toList()))
                    .addObject("potentialAssignees", users)
                    .addObject("potentialReporters", users)
                    .addObject("groups", groupRepository.findAll())
                    .addObject("searchForm", searchForm);
        });

        return mav;
    }

    @GetMapping("/issue/ajaxGetChangeLog")
    @ResponseBody
    public ModelAndView ajaxGetChangeLog(@AuthenticationPrincipal User user, @RequestParam Long issueId,
                                         @RequestParam(required = false) String sortColumn,
                                         @RequestParam(required = false) String sortDirection,
                                         @RequestParam(required = false) Integer page)
    {
        IssueEventForm issueEventForm = new IssueEventForm(0, user, issueId);
        issueEventForm.setEndpoint("/issue/ajaxGetChangeLog?issueId=" + issueId);

        // we must be doing a resort
        if (sortColumn != null && sortDirection != null)
        {
            issueEventForm.setSortColumn(sortColumn);
            issueEventForm.setSortDirection(sortDirection);
        }

        if (page != null)
            issueEventForm.setPage(String.valueOf(page));

        issueEventForm.setSearchResult(auditQueryLogic.query(issueEventForm));

        return new ModelAndView("auditTable")
                .addObject("searchForm", issueEventForm);
    }

    @GetMapping("/issue/ajaxCreateIssueForm")
    @ResponseBody
    public ModelAndView ajaxCreateIssueForm(@AuthenticationPrincipal User user)
    {
        return new ModelAndView("createIssueForm");
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
    public String updateIssue(@AuthenticationPrincipal User user, @RequestParam Long issueId,
                              @RequestParam String fldFieldName, @RequestParam String fldFieldValue)
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
//        Issue oldIssue = new Issue();
//        BeanUtils.copyProperties(issue, oldIssue);

        String propertyName = "";
        String oldValue = "";
        String newValue = "";

        if (projectId != 0)
        {
            propertyName = "project";
            oldValue = issue.getProject().getName();
            issue.setProject(projectRepository.findById(projectId).get());
            newValue = issue.getProject().getName();
            updateLog += "Project to " + issue.getProject().getName();
        }
        if (issueTypeId != 0)
        {
            propertyName = "issue type";
            oldValue = issue.getIssueType().getName();
            issue.setIssueType(issueTypeRepository.findById(issueTypeId).get());
            newValue = issue.getIssueType().getName();
            updateLog += "Type to " + issue.getIssueType().getName();
        }
        if (statusId != 0)
        {
            propertyName = "status";
            oldValue = issue.getStatus().getName();
            issue.setStatus(statusRepository.findById(statusId).get());
            newValue = issue.getStatus().getName();
            updateLog += "Status to " + issue.getStatus().getName();
        }
        if (severityId != 0)
        {
            propertyName = "severity";
            oldValue = issue.getSeverity().getName();
            issue.setSeverity(severityRepository.findById(severityId).get());
            newValue = issue.getSeverity().getName();
            updateLog += "Severity to " + issue.getSeverity().getName();
        }
        if (groupId != 0)
        {
            propertyName = "group";
            oldValue = issue.getGroup().getName();
            issue.setGroup(groupRepository.findById(groupId).get());
            newValue = issue.getGroup().getName();
            updateLog += "Group to " + issue.getGroup().getName();
        }
        if (title.length() != 0)
        {
            propertyName = "title";
            oldValue = issue.getTitle();
            issue.setTitle(title);
            newValue = issue.getTitle();
            updateLog += "Title";
        }
        if (description.length() != 0)
        {
            propertyName = "description";
            oldValue = issue.getDescription();
            issue.setDescription(description);
            newValue = issue.getDescription();
            updateLog += "Description";
        }
        if (assigneeId != 0)
        {
            propertyName = "assignee";
            oldValue = issue.getAssignee().getName();
            issue.setAssignee(userRepository.findById(assigneeId).get());
            newValue = issue.getAssignee().getName();
            updateLog += "Assignee to " + issue.getAssignee().getUsername();
        }
        if (reporterId != 0)
        {
            propertyName = "reporter";
            oldValue = issue.getReporter().getName();
            issue.setReporter(userRepository.findById(reporterId).get());
            newValue = issue.getReporter().getName();
            updateLog += "Reporter to " + issue.getReporter().getUsername();
        }

        if (updateLog.length() > 0)
            issue.setLastUpdatedOn(LocalDateTime.now());

//        Diff diff = javers.compare(oldIssue, issue);
        issueRepository.save(issue);

        IssueEvent issueEvent = issueEventRepository.save(new IssueEvent(0, user, issue, EventType.UPDATE,
                propertyName, oldValue, newValue));

        mailClient.prepareAndSend(issueEvent);

        return "Updated " + updateLog;
    }

    @PostMapping("/issue/addComment")
    public ModelAndView addComment(@AuthenticationPrincipal User user, @RequestParam Long issueId,
                                   @RequestParam String fldContent, @RequestParam Long fldVisibility)
    {
        issueRepository.findById(issueId).ifPresent(issue -> {
            Group group = fldVisibility == null ? issue.getGroup() : groupRepository.findById(fldVisibility).orElse(null);
            if (group == null)
                return;

            // access control
            if (user.isAdmin() || user.getGroups().contains(group))
            {
                Comment comment = new Comment(0, issue, user, fldContent, group, LocalDateTime.now(), LocalDateTime.now());
                comment = commentRepository.save(comment);

                IssueEvent issueEvent = issueEventRepository.save(new IssueEvent(0, user, issue, EventType.ADD,
                        "comment", "", fldContent, comment.getId()));

                mailClient.prepareAndSend(issueEvent);
            }
        });

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }

    @PostMapping("/issue/updateComment")
    @ResponseBody
    public String updateComment(@AuthenticationPrincipal User user, @RequestParam Long commentId,
                                      @RequestParam String fldFieldName, @RequestParam String fldFieldValue)
    {
        StringBuilder result = new StringBuilder();
        commentRepository.findById(commentId).ifPresent(comment -> {

            // access control
            boolean access = user.isAdmin() || user.getId() != comment.getAuthor().getId();
            if (!access)
            {
                return;
            }

            String content = !fldFieldName.equals("fldContent" + commentId) ? "" : Common.getSafeString(fldFieldValue);

            String oldContent = comment.getContent();
            if (content.length() != 0)
                comment.setContent(content);

            if (!content.equals(oldContent))
            {
                comment.setLastUpdatedOn(LocalDateTime.now());
                commentRepository.save(comment);

                IssueEvent issueEvent = issueEventRepository.save(new IssueEvent(0, user, comment.getIssue(), EventType.UPDATE,
                        "comment", oldContent, content, comment.getId()));

                mailClient.prepareAndSend(issueEvent);

                result.append("Comment Updated");
            }
        });

        return result.toString();
    }

    @PostMapping("/issue/removeComment")
    public ModelAndView removeComment(@AuthenticationPrincipal User user, @RequestParam Long issueId,
                                      @RequestParam Long commentId)
    {
        issueRepository.findById(issueId).ifPresent(issue -> {
            commentRepository.findById(commentId).ifPresent(comment -> {

                // access control
                if (user.isAdmin() || user.getId() == comment.getAuthor().getId())
                {
                    commentRepository.delete(comment);

                    IssueEvent issueEvent = issueEventRepository.save(new IssueEvent(0, user, issue, EventType.REMOVE,
                            "comment", comment.getContent(), ""));

                    mailClient.prepareAndSend(issueEvent);
                }
            });
        });

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }

    @GetMapping("/issue/addWatcher")
    public ModelAndView addWatcher(@AuthenticationPrincipal User user, @RequestParam Long issueId, @RequestParam Long userId)
    {
        issueRepository.findById(issueId).ifPresent(issue ->
                userRepository.findById(userId).ifPresent(watcher -> {
                    issue.getWatchers().add(watcher);
                    issueRepository.save(issue);

                    IssueEvent issueEvent = issueEventRepository.save(new IssueEvent(0, user, issue, EventType.ADD, "watcher", "", watcher.getUsername()));
                    mailClient.prepareAndSend(issueEvent);
                }));

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }

    @GetMapping("/issue/removeWatcher")
    public ModelAndView removeWatcher(@AuthenticationPrincipal User user, @RequestParam Long issueId, @RequestParam Long userId)
    {
        issueRepository.findById(issueId).ifPresent(issue ->
                userRepository.findById(userId).ifPresent(watcher -> {
                    issue.getWatchers().remove(watcher);
                    issueRepository.save(issue);

                    IssueEvent issueEvent = issueEventRepository.save(new IssueEvent(0, user, issue, EventType.REMOVE, "watcher", watcher.getUsername()));
                    mailClient.prepareAndSend(issueEvent);
                }));

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }
}
