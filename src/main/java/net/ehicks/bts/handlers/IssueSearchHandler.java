package net.ehicks.bts.handlers;

import net.ehicks.bts.beans.IssueForm;
import net.ehicks.bts.beans.IssueFormRepository;
import net.ehicks.bts.beans.User;
import net.ehicks.bts.model.IssueQueryLogic;
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
public class IssueSearchHandler
{
    IssueFormRepository issueFormRepository;
    IssueQueryLogic issueQueryLogic;

    public IssueSearchHandler(IssueFormRepository issueFormRepository, IssueQueryLogic issueQueryLogic)
    {
        this.issueFormRepository = issueFormRepository;
        this.issueQueryLogic = issueQueryLogic;
    }

    @GetMapping("/search/form")
    public ModelAndView showIssues(@AuthenticationPrincipal User user,
                                   Model model,
                                   @RequestParam(required = false) Long issueFormId)
    {
        IssueForm issueForm = (IssueForm)  model.getAttribute("issueForm");
        if (issueForm == null && issueFormId != null)
            issueForm = issueFormRepository.findById(issueFormId).orElse(null);

        // access control
        if (issueForm != null && issueForm.getUser().getId() != user.getId())
            return new ModelAndView("error");

        if (issueForm == null)
            issueForm = new IssueForm(0, user);

        issueForm.setSearchResult(issueQueryLogic.query(issueForm));

        return new ModelAndView("issueSearch")
                .addObject("issueForm", issueForm);
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
                               @ModelAttribute IssueForm issueForm)
    {
        if (issueForm.getUser().getId() != user.getId())
            return new ModelAndView("error");

        String query = "";
        if (issueForm.getId() != 0)
            query = "?issueFormId=" + issueForm.getId();


        redirectAttrs.addFlashAttribute("issueForm", issueForm);
        return new ModelAndView("redirect:/search/form" + query);
    }

    @PostMapping("/search/saveIssueForm")
    public ModelAndView saveIssueForm(@AuthenticationPrincipal User user,
                                      @ModelAttribute IssueForm issueForm)
    {
        if (issueForm.getUser().getId() == user.getId())
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
