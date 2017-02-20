package net.ehicks.bts;

import net.ehicks.bts.beans.*;
import net.ehicks.eoi.EOI;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmailEngine
{
    private static final Logger log = LoggerFactory.getLogger(EmailEngine.class);

    public static void sendEmail(EmailMessage emailMessage)
    {
        // determine recipients
        Set<String> recipients = determineRecipients(emailMessage);
        if (recipients.size() > 0)
            emailMessage.setStatus("WAITING");
        else
            emailMessage.setStatus("FAILED");
        EOI.update(emailMessage, SystemTask.EMAIL_ENGINE);

        // create email
        
        if (recipients.size() > 0)
            EmailThreadPool.getPool().submit(() -> sendHtmlEmail(emailMessage, recipients));
    }

    private static Set<String> determineRecipients(EmailMessage emailMessage)
    {
        Set<String> recipients = new HashSet<>();

        if (emailMessage.getIssueId() > 0 || emailMessage.getCommentId() > 0)
        {
            Issue issue = null;
            if (emailMessage.getIssueId() > 0)
            {
                issue = Issue.getById(emailMessage.getIssueId());
            }
            if (emailMessage.getCommentId() > 0)
            {
                Comment comment = Comment.getById(emailMessage.getCommentId());
                issue = Issue.getById(comment.getIssueId());
            }

            User reporter = issue.getReporter();
            User assignee = issue.getAssignee();
            recipients.add(reporter.getLogonId());
            recipients.add(assignee.getLogonId());
            List<WatcherMap> watchers = WatcherMap.getByIssueId(issue.getId());
            for (WatcherMap watcher : watchers)
                recipients.add(watcher.getWatcher().getLogonId());

            List<Subscription> subscriptions = Subscription.getAll();
            for (Subscription subscription : subscriptions)
            {
                // todo streamline this
                boolean addIt = false;
                if (subscription.getReporterUserIdsAsList().size() > 0 && subscription.getReporterUserIdsAsList().contains(reporter.getId().toString()))
                    addIt = true;
                if (subscription.getAssigneeUserIdsAsList().size() > 0 && subscription.getAssigneeUserIdsAsList().contains(assignee.getId().toString()))
                    addIt = true;
                if (subscription.getGroupIdsAsList().size() > 0 && subscription.getGroupIdsAsList().contains(issue.getGroupId().toString()))
                    addIt = true;
                if (subscription.getProjectIdsAsList().size() > 0 && subscription.getProjectIdsAsList().contains(issue.getProjectId().toString()))
                    addIt = true;
                if (subscription.getSeverityIdsAsList().size() > 0 && subscription.getSeverityIdsAsList().contains(issue.getSeverityId().toString()))
                    addIt = true;
                if (subscription.getStatusIdsAsList().size() > 0 && subscription.getStatusIdsAsList().contains(issue.getStatusId().toString()))
                    addIt = true;

                if (addIt)
                    recipients.add(User.getByUserId(subscription.getUserId()).getLogonId());
            }
        }

        if (emailMessage.getActionId() == EmailAction.TEST.getId())
        {
            recipients.add(emailMessage.getToAddress());
        }
        return recipients;
    }

    private static void sendHtmlEmail(EmailMessage emailMessage, Set<String> recipients)
    {
        try
        {
            // create the email message
            ImageHtmlEmail email = new ImageHtmlEmail();
            setConnectionFields(email);

            for (String recipient : recipients)
                if (recipient.equals("***REMOVED***")) // todo remove this safeguard
                    email.addTo(recipient, recipient);

            email.setSubject(emailMessage.getSubject());
            email.setHtmlMsg(emailMessage.getBody());

            // set the alternative message
            email.setTextMsg("Your email client does not support HTML messages");
            email.send();

            emailMessage.setStatus("SENT");
            EOI.update(emailMessage, SystemTask.EMAIL_ENGINE);
        }
        catch (MalformedURLException | EmailException e)
        {
            emailMessage.setStatus("FAILED");
            EOI.update(emailMessage, SystemTask.EMAIL_ENGINE);

            log.error(e.getMessage(), e);
        }
    }

    private static void setConnectionFields(ImageHtmlEmail email) throws MalformedURLException, EmailException
    {
        email.setHostName(SystemInfo.INSTANCE.getEmailHost());
        email.setSmtpPort(SystemInfo.INSTANCE.getEmailPort());
        email.setAuthenticator(new DefaultAuthenticator(SystemInfo.INSTANCE.getEmailUser(), SystemInfo.INSTANCE.getEmailPassword()));
        email.setStartTLSRequired(true);
        email.setFrom(SystemInfo.INSTANCE.getEmailFromAddress(), SystemInfo.INSTANCE.getEmailFromName());
        // define your base URL to resolve relative resource locations
        URL url = new URL("http://www.apache.org");
        email.setDataSourceResolver(new DataSourceUrlResolver(url));}
}
