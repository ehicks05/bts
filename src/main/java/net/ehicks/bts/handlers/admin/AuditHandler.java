package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.beans.IssueEventForm;
import net.ehicks.bts.beans.IssueEventFormRepository;
import net.ehicks.bts.beans.User;
import net.ehicks.bts.model.AuditQueryLogic;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuditHandler
{
    IssueEventFormRepository issueEventFormRepository;
    AuditQueryLogic auditQueryLogic;

    public AuditHandler(IssueEventFormRepository issueEventFormRepository, AuditQueryLogic auditQueryLogic)
    {
        this.issueEventFormRepository = issueEventFormRepository;
        this.auditQueryLogic = auditQueryLogic;
    }

    @GetMapping("/admin/audit/form")
    public ModelAndView showAuditRecords(@AuthenticationPrincipal User user,
                                         Model model,
                                         @RequestParam(required = false) Long issueEventFormId)
    {
        IssueEventForm issueEventForm = (IssueEventForm) model.getAttribute("searchForm");
        if (issueEventForm == null && issueEventFormId != null)
            issueEventForm = issueEventFormRepository.findById(issueEventFormId).orElse(null);

        // access control
        if (issueEventForm != null && issueEventForm.getUser().getId() != user.getId())
            return new ModelAndView("error");

        if (issueEventForm == null)
            issueEventForm = new IssueEventForm(0, user, null, null);

        issueEventForm.setSearchResult(auditQueryLogic.query(issueEventForm));

        return new ModelAndView("admin/audit")
                .addObject("searchForm", issueEventForm);
    }

    @GetMapping("/admin/audit/ajaxGetPageOfResults")
    public ModelAndView ajaxGetPageOfResults(@AuthenticationPrincipal User user, @RequestParam Long issueFormId,
                                             @RequestParam String sortColumn, @RequestParam String sortDirection,
                                             @RequestParam(required = false) Integer page)
    {
        IssueEventForm issueEventForm = issueEventFormRepository.findById(issueFormId)
                .orElse(new IssueEventForm(0, user, null, null));

        // we must be doing a resort
        if (sortColumn != null && sortDirection != null)
        {
            issueEventForm.setSortColumn(sortColumn);
            issueEventForm.setSortDirection(sortDirection);

            // we want to persist sorting preferences
            if (issueFormId > 0)
                issueEventFormRepository.save(issueEventForm);
        }

        if (page != null)
            issueEventForm.setPage(String.valueOf(page));

        issueEventForm.setSearchResult(auditQueryLogic.query(issueEventForm));

        return new ModelAndView("auditTable")
                .addObject("searchForm", issueEventForm);
    }

    @PostMapping("/admin/audit/search")
    public ModelAndView search(RedirectAttributes redirectAttrs,
                               @AuthenticationPrincipal User user,
                               @ModelAttribute IssueEventForm issueEventForm)
    {
        if (issueEventForm.getUser().getId() != user.getId())
            return new ModelAndView("error");

        String query = "";
        if (issueEventForm.getId() != 0)
            query = "?issueEventFormId=" + issueEventForm.getId();


        redirectAttrs.addFlashAttribute("searchForm", issueEventForm);
        return new ModelAndView("redirect:/admin/audit/form" + query);
    }

    @PostMapping("/admin/audit/saveIssueEventForm")
    public ModelAndView saveIssueEventForm(@AuthenticationPrincipal User user,
                                           @ModelAttribute IssueEventForm issueEventForm)
    {
        if (issueEventForm.getUser().getId() == user.getId())
            issueEventFormRepository.save(issueEventForm);

        return new ModelAndView("redirect:/admin/audit/form?issueFormId=" + issueEventForm.getId())
                .addObject("issueForm", issueEventForm);
    }
}
