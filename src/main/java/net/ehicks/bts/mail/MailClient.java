package net.ehicks.bts.mail;

import net.ehicks.bts.beans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MailClient {
    private static final Logger log = LoggerFactory.getLogger(MailClient.class);

    private JavaMailSender mailSender;
    private TaskExecutor taskExecutor;
    private MailContentBuilder mailContentBuilder;
    private SubscriptionRepository subscriptionRepository;
    private BtsSystemRepository btsSystemRepository;
    private IssueEventRepository issueEventRepository;

    public MailClient(JavaMailSender mailSender, TaskExecutor taskExecutor, MailContentBuilder mailContentBuilder,
                      SubscriptionRepository subscriptionRepository, BtsSystemRepository btsSystemRepository,
                      IssueEventRepository issueEventRepository)
    {
        this.mailSender = mailSender;
        this.taskExecutor = taskExecutor;
        this.mailContentBuilder = mailContentBuilder;
        this.subscriptionRepository = subscriptionRepository;
        this.btsSystemRepository = btsSystemRepository;
        this.issueEventRepository = issueEventRepository;
    }

    private static class EmailSender implements Runnable
    {
        private JavaMailSender mailSender;
        MimeMessagePreparator messagePreparator;

        public EmailSender(JavaMailSender mailSender, MimeMessagePreparator messagePreparator)
        {
            this.mailSender = mailSender;
            this.messagePreparator = messagePreparator;
        }

        public void run() {
            mailSender.send(messagePreparator);

        }
    }

    public void prepareAndSend(IssueEvent issueEvent) {
        BtsSystem btsSystem = btsSystemRepository.findFirstBy();

        String[] recipients = determineRecipients(issueEvent);
        if (recipients.length > 0)
            issueEvent.setStatus("WAITING");
        else
            issueEvent.setStatus("FAILED");
        issueEventRepository.save(issueEvent);

        if (recipients.length == 0)
            return;

        String content = mailContentBuilder.buildContent(issueEvent);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(btsSystem.getEmailFromAddress());
            messageHelper.setTo(recipients);
            messageHelper.setSubject(issueEvent.getSubject());
            messageHelper.setText(content, true);
        };
        try {
            taskExecutor.execute(new EmailSender(mailSender, messagePreparator));
//            mailSender.send(messagePreparator);
            issueEvent.setStatus("SENT");
            issueEventRepository.save(issueEvent);
        } catch (MailException e) {
            log.error(e.getLocalizedMessage());
            issueEvent.setStatus("FAILED");
            issueEventRepository.save(issueEvent);
        }
    }

    public void prepareAndSendTest(String toAddress) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(btsSystemRepository.findFirstBy().getEmailFromAddress());
            messageHelper.setTo(toAddress);
            messageHelper.setSubject("Test Message");
            messageHelper.setText("Mail works");
        };
        try {
            taskExecutor.execute(new EmailSender(mailSender, messagePreparator));
        } catch (MailException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private String[] determineRecipients(IssueEvent issueEvent)
    {
        Set<String> recipients = new HashSet<>();

        if (issueEvent.getIssue() != null)
        {
            Issue issue = issueEvent.getIssue();
            recipients.add(issue.getReporter().getUsername());
            recipients.add(issue.getAssignee().getUsername());
            for (User watcher : issue.getWatchers())
                recipients.add(watcher.getUsername());

            for (Subscription subscription : subscriptionRepository.findAll())
            {
                // todo streamline this
                boolean addIt = false;
                if (subscription.getReporters().size() > 0 && subscription.getReporters().contains(issue.getReporter()))
                    addIt = true;
                if (subscription.getAssignees().size() > 0 && subscription.getAssignees().contains(issue.getAssignee()))
                    addIt = true;
                if (subscription.getGroups().size() > 0 && subscription.getGroups().contains(issue.getGroup()))
                    addIt = true;
                if (subscription.getProjects().size() > 0 && subscription.getProjects().contains(issue.getProject()))
                    addIt = true;
                if (subscription.getSeverities().size() > 0 && subscription.getSeverities().contains(issue.getSeverity()))
                    addIt = true;
                if (subscription.getStatuses().size() > 0 && subscription.getStatuses().contains(issue.getStatus()))
                    addIt = true;

                if (addIt)
                    recipients.add(subscription.getUser().getUsername());
            }
        }

        String[] recipientArray = new String[recipients.size()];
        return recipients.toArray(recipientArray);
    }
}