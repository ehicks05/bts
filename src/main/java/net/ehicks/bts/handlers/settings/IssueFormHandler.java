package net.ehicks.bts.handlers.settings;

import net.ehicks.bts.beans.IssueFormRepository;
import net.ehicks.bts.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IssueFormHandler
{
    private static final Logger log = LoggerFactory.getLogger(IssueFormHandler.class);

    private IssueFormRepository issueFormRepository;

    public IssueFormHandler(IssueFormRepository issueFormRepository)
    {
        this.issueFormRepository = issueFormRepository;
    }

    @GetMapping("/settings/savedSearches/form")
    public ModelAndView showIssueForms(@AuthenticationPrincipal User user)
    {
        return new ModelAndView("settings/manageIssueFilters")
                .addObject("issueForms", issueFormRepository.findByUserId(user.getId()));
    }

    @GetMapping("/settings/savedSearches/delete")
    public ModelAndView deleteIssueForm(@RequestParam Long issueFormId)
    {
        issueFormRepository.findById(issueFormId)
                .ifPresent(issueForm -> issueFormRepository.delete(issueForm));

        return new ModelAndView("redirect:/settings/savedSearches/form");
    }
}
