package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.RequestStats;
import net.ehicks.bts.RequestStatsRepository;
import net.ehicks.bts.beans.*;
import net.ehicks.bts.mail.MailClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminHandler
{
    private static final Logger log = LoggerFactory.getLogger(AdminHandler.class);

    private IssueEventRepository issueEventRepository;
    private BtsSystemRepository btsSystemRepository;
    private MailClient mailClient;
    private RequestStatsRepository requestStatsRepository;
    private SessionRegistry sessionRegistry;

    public AdminHandler(IssueEventRepository issueEventRepository, BtsSystemRepository btsSystemRepository,
                        MailClient mailClient, RequestStatsRepository requestStatsRepository,
                        SessionRegistry sessionRegistry)
    {
        this.issueEventRepository = issueEventRepository;
        this.btsSystemRepository = btsSystemRepository;
        this.mailClient = mailClient;
        this.requestStatsRepository = requestStatsRepository;
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/admin/form")
    public ModelAndView showOverview(@AuthenticationPrincipal User user)
    {
        return new ModelAndView("admin/overview")
                .addObject("btsSystem", btsSystemRepository.findFirstBy())
                .addObject("adminSubscreens", getAdminSubscreens());
    }


    /** url, icon name, label, tab2 of the url */
    private List<List<String>> getAdminSubscreens()
    {
        return Arrays.asList(
                Arrays.asList("/admin/system/modify/form", "server", "Manage System", "system"),
                Arrays.asList("/admin/users/form", "user", "Manage Users", "users"),
                Arrays.asList("/admin/groups/form", "users", "Manage Groups", "groups"),
                Arrays.asList("/admin/projects/form", "folder", "Manage Projects", "projects"),
                Arrays.asList("/admin/email/form", "envelope", "Manage Email", "email"),
                Arrays.asList("/admin/backups/form", "cloud-upload-alt", "Backups", "backups"),
                Arrays.asList("/admin/system/info/form", "chart-bar", "System Info", "system"),
                Arrays.asList("/admin/dbInfo/form", "chart-bar", "Database Info", "dbInfo"),
                Arrays.asList("/admin/audit/form", "history", "Audit Records", "audit")
        );
    }

    @GetMapping("/admin/system/info/form")
    public ModelAndView showSystemInfo()
    {
        List<SessionInformation> sessions = new ArrayList<>();
        sessionRegistry.getAllPrincipals()
                .forEach(principal -> sessions.addAll(sessionRegistry.getAllSessions(principal, true)));

        List<RequestStats> requestStats = new ArrayList<>();

        try
        {
            requestStats = requestStatsRepository.findAll().stream()
                    .sorted(Comparator.comparing(RequestStats::getRequestStart).reversed())
                    .collect(Collectors.toList());
        }
        catch (Exception e)
        {

        }

        return new ModelAndView("admin/systemInfo")
                .addObject("sessions", sessions)
                .addObject("requests", requestStats);
    }

    @GetMapping("/admin/email/form")
    public ModelAndView showManageEmails()
    {
        return new ModelAndView("admin/emails")
                .addObject("emails", issueEventRepository.findAll());
    }

    @GetMapping("/admin/email/preview/form")
    public ModelAndView previewEmail(@RequestParam Long emailId)
    {
        return new ModelAndView("admin/previewEmail")
                .addObject("email", issueEventRepository.findById(emailId).orElse(null));
    }

    @PostMapping("/admin/email/sendTest")
    public ModelAndView sendTestEmail(@RequestParam String fldTo)
    {
        mailClient.prepareAndSendTest(fldTo);

        return new ModelAndView("redirect:/admin/modifySystem");
    }

    @GetMapping("/admin/email/delete")
    public ModelAndView deleteEmail(@RequestParam Long emailId)
    {
        issueEventRepository.findById(emailId).ifPresent(emailMessage ->
                issueEventRepository.delete(emailMessage));

        return new ModelAndView("redirect:/admin/email/form");
    }

    @GetMapping("/admin/system/modify/form")
    public ModelAndView showModifySystem()
    {
        return new ModelAndView("admin/modifySystem")
                .addObject("btsSystem", btsSystemRepository.findFirstBy())
                .addObject("themes", Arrays.asList("default", "cosmo", "flatly", "journal", "lux",
                        "pulse", "simplex", "superhero", "united", "yeti"));
    }

    @PostMapping("/admin/system/modify/modify")
    public ModelAndView modifySystem(
            @RequestParam String siteName,
            @RequestParam String logonMessage,
            @RequestParam Avatar defaultAvatar,
            @RequestParam String theme,
            @RequestParam String emailFromName,
            @RequestParam String emailFromAddress
            )
    {
        BtsSystem btsSystem = btsSystemRepository.findFirstBy();
        btsSystem.setSiteName(siteName);
        btsSystem.setLogonMessage(logonMessage);
        btsSystem.setDefaultAvatar(defaultAvatar);
        btsSystem.setTheme(theme);
        btsSystem.setEmailFromName(emailFromName);
        btsSystem.setEmailFromAddress(emailFromAddress);
        btsSystemRepository.save(btsSystem);

        return new ModelAndView("redirect:/admin/system/modify/form");
    }
}
