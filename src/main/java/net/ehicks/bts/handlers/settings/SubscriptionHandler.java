package net.ehicks.bts.handlers.settings;

import net.ehicks.bts.beans.*;
import net.ehicks.bts.util.PdfCreator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SubscriptionHandler
{
    private SubscriptionRepository subscriptionRepository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private IssueTypeRepository issueTypeRepository;
    private ProjectRepository projectRepository;
    private StatusRepository statusRepository;
    private SeverityRepository severityRepository;

    public SubscriptionHandler(SubscriptionRepository subscriptionRepository, UserRepository userRepository,
                               GroupRepository groupRepository, IssueTypeRepository issueTypeRepository,
                               ProjectRepository projectRepository, StatusRepository statusRepository,
                               SeverityRepository severityRepository)
    {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.projectRepository = projectRepository;
        this.statusRepository = statusRepository;
        this.severityRepository = severityRepository;
    }

    @GetMapping("/settings/subscriptions/form")
    public ModelAndView showSubscriptions(@AuthenticationPrincipal User user)
    {
        return new ModelAndView("settings/subscriptions")
                .addObject("subscriptions", subscriptionRepository.findByUser_Id(user.getId()))
                .addObject("projects", projectRepository.findAll())
                .addObject("groups", groupRepository.findAll())
                .addObject("severities", severityRepository.findAll())
                .addObject("statuses", statusRepository.findAll())
                .addObject("users", userRepository.findAll()); // todo: recreate 'User.findAllVisible' security
    }

    @GetMapping("/settings/subscriptions/add")
    public ModelAndView addSubscription(
            @AuthenticationPrincipal User user,
            @RequestParam List<Long> statusIds,
            @RequestParam List<Long> severityIds,
            @RequestParam List<Long> projectIds,
            @RequestParam List<Long> groupIds,
            @RequestParam List<Long> issueTypeIds,
            @RequestParam List<Long> assigneeIds,
            @RequestParam List<Long> reporterIds
    )
    {
        Subscription subscription = new Subscription(0, user,
                groupRepository.findByIdIn(groupIds),
                issueTypeRepository.findByIdIn(issueTypeIds),
                projectRepository.findByIdIn(projectIds),
                userRepository.findByIdIn(assigneeIds),
                userRepository.findByIdIn(reporterIds),
                severityRepository.findByIdIn(severityIds),
                statusRepository.findByIdIn(statusIds)
        );
        subscriptionRepository.save(subscription);

        return new ModelAndView("redirect:/settings/subscriptions/form");
    }

    @GetMapping("/settings/subscriptions/delete")
    public ModelAndView deleteSubscription(@RequestParam Long subscriptionId)
    {
        subscriptionRepository.findById(subscriptionId)
                .ifPresent(subscription -> subscriptionRepository.delete(subscription));

        return new ModelAndView("redirect:/settings/subscriptions/form");
    }

    @GetMapping("/settings/subscriptions/print")
    @ResponseBody
    public ResponseEntity<byte[]> printSubscriptions(@AuthenticationPrincipal User user)
    {
        List<List<Object>> subscriptionData = new ArrayList<>();
        subscriptionData.add(Arrays.asList("Object Id", "User Id", "Description"));
        subscriptionData.addAll(subsToPrintable(subscriptionRepository.findByUser_Id(user.getId())));

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) PdfCreator.createPdf(user.getUsername(), "My Subscription Report", "", subscriptionData);

        return outputStream != null
                ? ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(outputStream.toByteArray())
                : ResponseEntity.notFound().build();
    }

    private List<List<Object>> subsToPrintable(List<Subscription> subs)
    {
        return subs.stream().map(this::toPrintable).collect(Collectors.toList());
    }

    private List<Object> toPrintable(Subscription subscription)
    {
        return Arrays.asList(subscription.getId(),
                subscription.getUser().getId(),
                subscription.getDescription());
    }
}
