package net.ehicks.bts.handlers.settings;

import net.ehicks.bts.beans.*;
import net.ehicks.bts.util.PdfCreator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
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
    public ModelAndView showSubscriptions(@AuthenticationPrincipal User user, @RequestParam(required = false) Long subscriptionId)
    {
        Subscription subscription = new Subscription(0, user);
        if (subscriptionId != null)
            subscription = subscriptionRepository.findById(subscriptionId).orElse(new Subscription(0, user));

        return new ModelAndView("settings/subscriptions")
                .addObject("subscription", subscription)
                .addObject("subscriptions", subscriptionRepository.findByUser_Id(user.getId()));
    }

    @PostMapping("/settings/subscriptions/save")
    public ModelAndView saveSubscription(@AuthenticationPrincipal User user, @ModelAttribute Subscription subscription)
    {
        subscriptionRepository.save(subscription);

        return new ModelAndView("redirect:/settings/subscriptions/form?subscriptionId=" + subscription.getId());
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
