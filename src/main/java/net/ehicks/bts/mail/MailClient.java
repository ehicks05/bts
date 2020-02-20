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
    private EmailEventRepository emailEventRepository;

    public MailClient(JavaMailSender mailSender, TaskExecutor taskExecutor, MailContentBuilder mailContentBuilder,
                      SubscriptionRepository subscriptionRepository, BtsSystemRepository btsSystemRepository,
                      EmailEventRepository emailEventRepository)
    {
        this.mailSender = mailSender;
        this.taskExecutor = taskExecutor;
        this.mailContentBuilder = mailContentBuilder;
        this.subscriptionRepository = subscriptionRepository;
        this.btsSystemRepository = btsSystemRepository;
        this.emailEventRepository = emailEventRepository;
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

    public void prepareAndSend(EmailEvent emailEvent) {
        BtsSystem btsSystem = btsSystemRepository.findFirstBy();

        String[] recipients = determineRecipients(emailEvent);
        if (recipients.length > 0)
            emailEvent.setStatus("WAITING");
        else
            emailEvent.setStatus("FAILED");
        emailEventRepository.save(emailEvent);

        if (recipients.length == 0)
            return;

        String content = mailContentBuilder.buildContent(emailEvent);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(btsSystem.getEmailFromAddress());
            messageHelper.setTo(recipients);
            messageHelper.setSubject(emailEvent.getSubject());
            messageHelper.setText(content, true);
        };
        try {
            taskExecutor.execute(new EmailSender(mailSender, messagePreparator));
//            mailSender.send(messagePreparator);
            emailEvent.setStatus("SENT");
            emailEventRepository.save(emailEvent);
        } catch (MailException e) {
            log.error(e.getLocalizedMessage());
            emailEvent.setStatus("FAILED");
            emailEventRepository.save(emailEvent);
        }
    }

    private String[] determineRecipients(EmailEvent emailEvent)
    {
        Set<String> recipients = new HashSet<>();

        if (emailEvent.getIssue() != null || emailEvent.getComment() != null)
        {
            Issue issue = emailEvent.getIssue();
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

        if (emailEvent.getActionId() == EmailAction.TEST.getId())
        {
            recipients.add(emailEvent.getToAddress());
        }

        String[] recipientArray = new String[recipients.size()];
        return recipients.toArray(recipientArray);
    }
}