package net.ehicks.bts.handlers;

import net.ehicks.bts.beans.*;
import net.ehicks.bts.model.IssueQueryLogic;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class IssueSearchHandler
{
    IssueFormRepository issueFormRepository;
    UserRepository userRepository;
    GroupRepository groupRepository;
    IssueTypeRepository issueTypeRepository;
    ProjectRepository projectRepository;
    StatusRepository statusRepository;
    SeverityRepository severityRepository;
    IssueQueryLogic issueQueryLogic;

    public IssueSearchHandler(IssueFormRepository issueFormRepository, UserRepository userRepository,
                              GroupRepository groupRepository, IssueTypeRepository issueTypeRepository,
                              ProjectRepository projectRepository, StatusRepository statusRepository,
                              SeverityRepository severityRepository, IssueQueryLogic issueQueryLogic)
    {
        this.issueFormRepository = issueFormRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.projectRepository = projectRepository;
        this.statusRepository = statusRepository;
        this.severityRepository = severityRepository;
        this.issueQueryLogic = issueQueryLogic;
    }

    @GetMapping("/search/form")
    public ModelAndView showIssues(@AuthenticationPrincipal User user,
                                   Model model,
                                   @RequestParam(required = false) Long issueFormId
//                                  , @RequestParam(required = false) IssueForm issueForm
    )
    {
        IssueForm issueForm = (IssueForm)  model.getAttribute("issueForm");
        if (issueForm == null && issueFormId != null)
            issueForm = issueFormRepository.findById(issueFormId).orElse(null);

        if (issueForm == null)
            issueForm = new IssueForm(0, user);

        issueForm.setSearchResult(issueQueryLogic.query(issueForm));
        return getIssueSearchModel(issueForm);
    }

    @NotNull
    private ModelAndView getIssueSearchModel(IssueForm issueForm)
    {
        // todo security: lock down some of these queries
        return new ModelAndView("issueSearch")
                .addObject("issueForm", issueForm)
                .addObject("users", userRepository.findAll())
                .addObject("projects", projectRepository.findAll())
                .addObject("groups", groupRepository.findAll())
                .addObject("severities", severityRepository.findAll())
                .addObject("statuses", statusRepository.findAll())
                .addObject("users", userRepository.findAll())
                .addObject("issueTypes", issueTypeRepository.findAll());
    }

    private IssueForm updateIssueFormFromRequest(@AuthenticationPrincipal User user,
                                                 @RequestParam(required = false) Long formId,
                                                 @RequestParam String formName,
                                                 @RequestParam String containsText,
                                                 @RequestParam String title,
                                                 @RequestParam String description,
                                                 @RequestParam List<Long> statusIds,
                                                 @RequestParam List<Long> severityIds,
                                                 @RequestParam List<Long> projectIds,
                                                 @RequestParam List<Long> groupIds,
                                                 @RequestParam List<Long> issueTypeIds,
                                                 @RequestParam List<Long> assigneeIds,
                                                 @RequestParam List<Long> reporterIds,
//                                                 @RequestParam LocalDateTime createdOn,
//                                                 @RequestParam LocalDateTime lastUpdatedOn,
                                                 @RequestParam Boolean onDash,
                                                 @RequestParam String sortColumn,
                                                 @RequestParam String sortDirection,
                                                 @RequestParam Integer page
                                                 )
    {
        IssueForm issueForm = formId == null ?
                new IssueForm(user) : issueFormRepository.findById(formId).orElse(new IssueForm(user));

        issueForm.setFormName(formName);
        issueForm.setContainsText(containsText);
        issueForm.setTitle(title);
        issueForm.setDescription(description);
        issueForm.setGroups(groupRepository.findByIdIn(groupIds));
        issueForm.setIssueTypes(issueTypeRepository.findByIdIn(issueTypeIds));
        issueForm.setProjects(projectRepository.findByIdIn(projectIds));
        issueForm.setAssignees(userRepository.findByIdIn(assigneeIds));
        issueForm.setReporters(userRepository.findByIdIn(reporterIds));
        issueForm.setSeverities(severityRepository.findByIdIn(severityIds));
        issueForm.setStatuses(statusRepository.findByIdIn(statusIds));
//        issueForm.setLastUpdatedOn(lastUpdatedOn);
        issueForm.setOnDash(onDash);
        issueForm.setSortColumn(sortColumn);
        issueForm.setSortDirection(sortDirection);
        issueForm.setPage(String.valueOf(page));

        // parse sorting fields
        if (sortColumn == null)
        {
            sortColumn = "id";
            sortDirection = "asc";
        }

        if (sortDirection == null)
            sortDirection = "asc";

        if (page == null || page == 0)
            page = 1;

        issueForm.setSortColumn(sortColumn);
        issueForm.setSortDirection(sortDirection);
        issueForm.setPage(String.valueOf(page));

        return issueForm;
    }

    @GetMapping("/search/ajaxGetPageOfResults")
    public ModelAndView ajaxGetPageOfResults(@AuthenticationPrincipal User user, @RequestParam Long issueFormId,
                                             @RequestParam String sortColumn, @RequestParam String sortDirection,
                                             @RequestParam(required = false) Integer page)
    {
        IssueForm issueForm = issueFormRepository.findById(issueFormId).orElse(new IssueForm(0, user));

        // we must be doing a resort
        if (sortColumn != null && sortDirection != null)
        {
            issueForm.setSortColumn(sortColumn);
            issueForm.setSortDirection(sortDirection);

            // we want to persist sorting preferences
            if (issueFormId > 0)
                issueFormRepository.save(issueForm);
        }

        if (page != null)
            issueForm.setPage(String.valueOf(page));

        issueForm.setSearchResult(issueQueryLogic.query(issueForm));

        return new ModelAndView("issueTable")
                .addObject("issueForm", issueForm);
    }

    @PostMapping("/search/search")
    public ModelAndView search(RedirectAttributes redirectAttrs,
                               @AuthenticationPrincipal User user,
                               @RequestParam(required = false) Long formId,
                               @RequestParam String formName,
                               @RequestParam String containsText,
//                               @RequestParam String title,
//                               @RequestParam String description,
                               @RequestParam(required = false) List<Long> groupIds,
                               @RequestParam(required = false) List<Long> issueTypeIds,
                               @RequestParam(required = false) List<Long> projectIds,
                               @RequestParam(required = false) List<Long> assigneeIds,
                               @RequestParam(required = false) List<Long> reporterIds,
                               @RequestParam(required = false) List<Long> severityIds,
                               @RequestParam(required = false) List<Long> statusIds,
//                                                 @RequestParam LocalDateTime createdOn,
//                                                 @RequestParam LocalDateTime lastUpdatedOn,
                               @RequestParam(required = false) Boolean onDash,
                               @RequestParam String sortColumn,
                               @RequestParam String sortDirection,
                               @RequestParam Integer page)
    {
        IssueForm issueForm = formId == null || formId == 0 ?
                new IssueForm(user) : issueFormRepository.findById(formId).orElse(new IssueForm(user));

        issueForm.setFormName(formName);
        issueForm.setContainsText(containsText);
//        issueForm.setTitle(title);
//        issueForm.setDescription(description);
        if (groupIds != null) issueForm.setGroups(groupRepository.findByIdIn(groupIds));
        if (issueTypeIds != null) issueForm.setIssueTypes(issueTypeRepository.findByIdIn(issueTypeIds));
        if (projectIds != null) issueForm.setProjects(projectRepository.findByIdIn(projectIds));
        if (assigneeIds != null) issueForm.setAssignees(userRepository.findByIdIn(assigneeIds));
        if (reporterIds != null) issueForm.setReporters(userRepository.findByIdIn(reporterIds));
        if (severityIds != null) issueForm.setSeverities(severityRepository.findByIdIn(severityIds));
        if (statusIds != null) issueForm.setStatuses(statusRepository.findByIdIn(statusIds));
//        issueForm.setLastUpdatedOn(lastUpdatedOn);
        if (onDash != null) issueForm.setOnDash(onDash);
        issueForm.setSortColumn(sortColumn);
        issueForm.setSortDirection(sortDirection);
        issueForm.setPage(String.valueOf(page));

        issueForm.setSearchResult(issueQueryLogic.query(issueForm));

        redirectAttrs.addFlashAttribute("issueForm", issueForm);
        return new ModelAndView("redirect:/search/form" + (formId != null && formId > 0 ? "?issueFormId=" + formId : ""));
    }

    @PostMapping("/search/saveIssueForm")
    public ModelAndView saveIssueForm(@AuthenticationPrincipal User user,
                                      @RequestParam(required = false) Long formId,
                                      @RequestParam String formName,
                                      @RequestParam String containsText,
//                                      @RequestParam String title,
//                                      @RequestParam String description,
                                      @RequestParam(required = false) List<Long> groupIds,
                                      @RequestParam(required = false) List<Long> issueTypeIds,
                                      @RequestParam(required = false) List<Long> projectIds,
                                      @RequestParam(required = false) List<Long> assigneeIds,
                                      @RequestParam(required = false) List<Long> reporterIds,
                                      @RequestParam(required = false) List<Long> severityIds,
                                      @RequestParam(required = false) List<Long> statusIds,
//                                                 @RequestParam LocalDateTime createdOn,
//                                                 @RequestParam LocalDateTime lastUpdatedOn,
                                      @RequestParam(required = false) Boolean onDash,
                                      @RequestParam String sortColumn,
                                      @RequestParam String sortDirection,
                                      @RequestParam Integer page)
    {
        IssueForm issueForm = formId == null ?
                new IssueForm(user) : issueFormRepository.findById(formId).orElse(new IssueForm(user));

        issueForm.setFormName(formName);
        issueForm.setContainsText(containsText);
//        issueForm.setTitle(title);
//        issueForm.setDescription(description);
        if (groupIds != null) issueForm.setGroups(groupRepository.findByIdIn(groupIds));
        if (issueTypeIds != null) issueForm.setIssueTypes(issueTypeRepository.findByIdIn(issueTypeIds));
        if (projectIds != null) issueForm.setProjects(projectRepository.findByIdIn(projectIds));
        if (assigneeIds != null) issueForm.setAssignees(userRepository.findByIdIn(assigneeIds));
        if (reporterIds != null) issueForm.setReporters(userRepository.findByIdIn(reporterIds));
        if (severityIds != null) issueForm.setSeverities(severityRepository.findByIdIn(severityIds));
        if (statusIds != null) issueForm.setStatuses(statusRepository.findByIdIn(statusIds));
//        issueForm.setLastUpdatedOn(lastUpdatedOn);
        issueForm.setOnDash(onDash != null);
        issueForm.setSortColumn(sortColumn);
        issueForm.setSortDirection(sortDirection);
        issueForm.setPage(String.valueOf(page));

        issueFormRepository.save(issueForm);

        return new ModelAndView("redirect:/search/form?issueFormId=" + issueForm.getId())
                .addObject("issueForm", issueForm);
    }

    @GetMapping("/search/addToDashboard")
    public ModelAndView addToDashboard(@RequestParam Long issueFormId)
    {
        issueFormRepository.findById(issueFormId).ifPresent(issueForm -> {
            issueForm.setOnDash(true);
            issueFormRepository.save(issueForm);
        });

        return new ModelAndView("redirect:/search/form?issueFormId=" + issueFormId);
    }
}
