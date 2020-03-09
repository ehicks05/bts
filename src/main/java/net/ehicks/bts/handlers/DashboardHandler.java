package net.ehicks.bts.handlers;

import kotlin.ranges.IntRange;
import net.ehicks.bts.beans.IssueForm;
import net.ehicks.bts.beans.IssueFormRepository;
import net.ehicks.bts.beans.User;
import net.ehicks.bts.model.IssueQueryLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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
                .findByUserIdAndOnDashTrueOrderByIndex(user.getId());

        dashBoardIssueForms.forEach(issueForm -> issueForm.setSearchResult(issueQueryLogic.query(issueForm)));
        int minIndex = dashBoardIssueForms.stream().filter(IssueForm::getOnDash).mapToInt(IssueForm::getIndex).min().orElse(0);
        int maxIndex = dashBoardIssueForms.stream().filter(IssueForm::getOnDash).mapToInt(IssueForm::getIndex).max().orElse(0);

        return new ModelAndView("dashboard")
                .addObject("dashBoardIssueForms", dashBoardIssueForms)
                .addObject("minIndex", minIndex)
                .addObject("maxIndex", maxIndex);
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

    @GetMapping("/dashboard/move")
    public ModelAndView moveIssueForm(@AuthenticationPrincipal User user, @RequestParam Long issueFormId,
                                        @RequestParam String direction)
    {
        int offset = direction.equals("up") ? -1 : 1;
        issueFormRepository.findById(issueFormId).ifPresent(issueForm -> {
            List<IssueForm> issueForms = issueFormRepository.findByUserIdOrderByIndex(user.getId());
            int fromIndex = issueForms.indexOf(issueForm);
            int toIndex = -1;
            for (int i = fromIndex + offset; i >= 0 && i < issueForms.size(); i += offset)
                if (issueForms.get(i).getOnDash())
                {
                    toIndex = i;
                    break;
                }

            if (toIndex != -1)
            {
                issueForm.setIndex(toIndex);
                issueFormRepository.save(issueForm);

                List<IssueForm> issueFormsModified = new ArrayList<>();
                int fromRange;
                int toRange;
                if (fromIndex < toIndex)
                {
                    fromRange = fromIndex + 1;
                    toRange = toIndex;
                }
                else
                {
                    fromRange = toIndex;
                    toRange = fromIndex - 1;
                }
                new IntRange(fromRange, toRange).forEach(index -> {
                    IssueForm issueForm1 = issueForms.get(index);
                    issueForm1.setIndex(issueForm1.getIndex() - offset);
                    issueFormsModified.add(issueForm1);
                });
                issueFormRepository.saveAll(issueFormsModified);
            }
        });

        return new ModelAndView("redirect:/dashboard#issueForm" + issueFormId);
    }
}
