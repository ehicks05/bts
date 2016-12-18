package com.hicks;

import com.hicks.beans.*;
import net.ehicks.eoi.EOI;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EmailEngine
{
    public static void sendEmail(EmailMessage emailMessage)
    {
        // determine recipients
        List<String> recipients = new ArrayList<>();
        if (emailMessage.getAction().equals("added a comment"))
        {
            Comment actionSource = Comment.getById(emailMessage.getActionSourceId());
            List<WatcherMap> watchers = WatcherMap.getByIssueId(actionSource.getIssueId());
            for (WatcherMap watcher : watchers)
                recipients.add(watcher.getWatcher().getLogonId());
        }
        if (emailMessage.getAction().equals("test"))
        {
            recipients.add(emailMessage.getToAddress());
        }

        emailMessage.setStatus("WAITING");
        EOI.update(emailMessage);

        // create email
        new Thread(() -> sendHtmlEmail(emailMessage, recipients)).start();
    }

    private static void sendHtmlEmail(EmailMessage emailMessage, List<String> recipients)
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
            EOI.update(emailMessage);
        }
        catch (MalformedURLException | EmailException e)
        {
            emailMessage.setStatus("FAILED");
            EOI.update(emailMessage);

            System.out.println(e.getMessage());
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
