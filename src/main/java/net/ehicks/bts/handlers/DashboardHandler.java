package net.ehicks.bts.handlers;

import net.ehicks.bts.beans.IssueForm;
import net.ehicks.bts.beans.IssueFormRepository;
import net.ehicks.bts.beans.IssueRepository;
import net.ehicks.bts.beans.User;
import net.ehicks.bts.model.IssueQueryLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Controller
public class DashboardHandler
{
    private static final Logger log = LoggerFactory.getLogger(DashboardHandler.class);

    private IssueFormRepository issueFormRepository;
    private IssueQueryLogic issueQueryLogic;

    public DashboardHandler(IssueFormRepository issueFormRepository, IssueQueryLogic issueQueryLogic)
    {
        this.issueFormRepository = issueFormRepository;
        this.issueQueryLogic = issueQueryLogic;
    }

    @GetMapping("/dashboard")
    public ModelAndView showDashboard(@AuthenticationPrincipal User user)
    {
        List<IssueForm> dashBoardIssueForms = issueFormRepository
                .findByUserIdAndOnDashTrue(user.getId());

        dashBoardIssueForms.forEach(issueForm -> issueForm.setSearchResult(issueQueryLogic.query(issueForm)));

        return new ModelAndView("dashboard")
                .addObject("dashBoardIssueForms", dashBoardIssueForms);
    }

    @GetMapping("/dashboard/remove")
    public ModelAndView removeIssueForm(@RequestParam Long issueFormId)
    {
        issueFormRepository.findById(issueFormId).ifPresent(issueForm -> {
            issueForm.setOnDash(false);
            issueFormRepository.save(issueForm);
        });

        return new ModelAndView("redirect:/dashboard");
    }
}
