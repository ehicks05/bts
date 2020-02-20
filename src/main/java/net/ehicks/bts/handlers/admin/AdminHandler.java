package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.beans.*;
import net.ehicks.bts.mail.EmailAction;
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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Controller
public class AdminHandler
{
    private static final Logger log = LoggerFactory.getLogger(AdminHandler.class);

    private ProjectRepository projectRepository;
    private GroupRepository groupRepository;
    private EmailEventRepository emailEventRepository;
    private BtsSystemRepository btsSystemRepository;
    private MailClient mailClient;
    private EntityManager entityManager;
    private SessionRegistry sessionRegistry;

    public AdminHandler(ProjectRepository projectRepository, GroupRepository groupRepository,
                        EmailEventRepository emailEventRepository, BtsSystemRepository btsSystemRepository,
                        MailClient mailClient, EntityManager entityManager, SessionRegistry sessionRegistry)
    {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.emailEventRepository = emailEventRepository;
        this.btsSystemRepository = btsSystemRepository;
        this.mailClient = mailClient;
        this.entityManager = entityManager;
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
        Map<String, String> dbInfoMap = new LinkedHashMap<>();

        Query query = entityManager.createNativeQuery("select * from pg_stat_database where datname='puffin'");
        List<Object> dbInfo = query.getResultList();

        Object[] results = (Object[]) dbInfo.get(0);
        List<String> headers = Arrays.asList("datid","datname","numbackends","xact_commit",
                "xact_rollback","blks_read","blks_hit","tup_returned","tup_fetched",
                "tup_inserted","tup_updated","tup_deleted","conflicts","temp_files",
                "temp_bytes","deadlocks","blk_read_time","blk_write_time","stats_reset");
        for (int i = 0; i < headers.size(); i++)
        {
            dbInfoMap.put(headers.get(i), results[i] != null ? results[i].toString() : "");
        }

        List<SessionInformation> sessions = new ArrayList<>();
        sessionRegistry.getAllPrincipals()
                .forEach(principal -> sessions.addAll(sessionRegistry.getAllSessions(principal, true)));

        return new ModelAndView("admin/systemInfo")
                .addObject("dbInfo", dbInfo)
                .addObject("dbInfoMap", dbInfoMap)
                .addObject("sessions", sessions);
    }

    @GetMapping("/admin/projects/form")
    public ModelAndView showManageProjects()
    {
        return new ModelAndView("admin/projects")
                        .addObject("projects", projectRepository.findAll());
    }

    @PostMapping("/admin/projects/create")
    public ModelAndView createProject(@RequestParam String fldName, @RequestParam String fldPrefix)
    {
        projectRepository.save(new Project(0, fldName, fldPrefix));
        return new ModelAndView("redirect:/admin/projects/form");
    }

    @GetMapping("/admin/projects/delete")
    public ModelAndView deleteProject(@RequestParam Long projectId)
    {
        projectRepository.findById(projectId)
                .ifPresent(project -> projectRepository.delete(project));

        return new ModelAndView("redirect:/admin/projects/form");
    }

    @GetMapping("/admin/projects/modify/form")
    public ModelAndView showModifyProject(@RequestParam Long projectId)
    {
        return new ModelAndView("admin/modifyProject")
                .addObject("project", projectRepository.findById(projectId).orElse(null));
    }

    @PostMapping("/admin/projects/modify/modify")
    public ModelAndView modifyProject(@RequestParam Long projectId, @RequestParam String name,
                                      @RequestParam String prefix)
    {
        projectRepository.findById(projectId).ifPresent(project -> {
            project.setPrefix(prefix);
            project.setName(name);
            projectRepository.save(project);
        });

        return new ModelAndView("redirect:/admin/projects/modify/form?projectId=" + projectId);
    }

    @GetMapping("/admin/groups/form")
    public ModelAndView showManageGroups()
    {
        return new ModelAndView("admin/groups")
                .addObject("groups", groupRepository.findAll());
    }

    @PostMapping("/admin/groups/create")
    public ModelAndView createGroup(@RequestParam String fldName)
    {
        groupRepository.save(new Group(0, fldName, false, false));
        return new ModelAndView("redirect:/admin/groups/form");
    }

    @GetMapping("/admin/groups/delete")
    public ModelAndView deleteGroup(@RequestParam Long groupId)
    {
        groupRepository.findById(groupId).ifPresent(group -> groupRepository.delete(group));
        return new ModelAndView("redirect:/admin/groups/form");
    }

    @GetMapping("/admin/groups/modify/form")
    public ModelAndView showModifyGroup(@RequestParam Long groupId)
    {
        return new ModelAndView("admin/modifyGroup")
                .addObject("group", groupRepository.findById(groupId).orElse(null));
    }

    @PostMapping("/admin/groups/modify/modify")
    public ModelAndView modifyGroup(@RequestParam Long groupId, @RequestParam String name)
    {
        groupRepository.findById(groupId).ifPresent(group -> {
            group.setName(name);
            groupRepository.save(group);
        });

        return new ModelAndView("redirect:/admin/groups/modify/form?groupId=" + groupId);
    }

    @GetMapping("/admin/email/form")
    public ModelAndView showManageEmails()
    {
        return new ModelAndView("admin/emails")
                .addObject("emails", emailEventRepository.findAll());
    }

    @GetMapping("/admin/email/preview/form")
    public ModelAndView previewEmail(@RequestParam Long emailId)
    {
        return new ModelAndView("admin/previewEmail")
                .addObject("email", emailEventRepository.findById(emailId).orElse(null));
    }

    @PostMapping("/admin/email/sendTest")
    public ModelAndView sendTestEmail(@RequestParam String fldTo)
    {
        EmailEvent emailEvent = new EmailEvent();
        emailEvent.setActionId(EmailAction.TEST.getId());
        emailEvent.setToAddress(fldTo);
        emailEvent = emailEventRepository.save(emailEvent);

        mailClient.prepareAndSend(emailEvent);

        return new ModelAndView("redirect:/admin/email/form");
    }

    @GetMapping("/admin/email/delete")
    public ModelAndView deleteEmail(@RequestParam Long emailId)
    {
        emailEventRepository.findById(emailId).ifPresent(emailMessage ->
                emailEventRepository.delete(emailMessage));

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
            @RequestParam String instanceName,
            @RequestParam String logonMessage,
            @RequestParam Avatar defaultAvatar,
            @RequestParam String theme,
            @RequestParam String emailFromName,
            @RequestParam String emailFromAddress
            )
    {
        BtsSystem btsSystem = btsSystemRepository.findFirstBy();
        btsSystem.setInstanceName(instanceName);
        btsSystem.setLogonMessage(logonMessage);
        btsSystem.setDefaultAvatar(defaultAvatar);
        btsSystem.setTheme(theme);
        btsSystem.setEmailFromName(emailFromName);
        btsSystem.setEmailFromAddress(emailFromAddress);
        btsSystemRepository.save(btsSystem);

        return new ModelAndView("redirect:/admin/system/modify/form");
    }
}
