package com.hicks;

import com.hicks.beans.*;
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

        // create email
        try
        {
            sendHtmlEmail(emailMessage, recipients);
        }
        catch (MalformedURLException | EmailException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void sendHtmlEmail(EmailMessage emailMessage, List<String> recipients) throws MalformedURLException, EmailException
    {
        User user = User.getByUserId(emailMessage.getUserId());
        String avatarId = String.valueOf(user.getAvatarId());
        if (avatarId.length() == 1)
            avatarId = "0" + avatarId;
        Issue issue = Issue.getById(emailMessage.getIssueId());
        // load your HTML email template
        String htmlEmailTemplate =
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<body>\n" +
                        "<table style=\"width: 100%;background-color: #eee;\">\n" +
                        "    <tr><td>\n" +
                        "        <table style=\"width: 500px;margin:auto;background-color: white\">\n" +
                        "            <tr>\n" +
                        "                <td style=\"padding: 10px;\"><h1><a href=\"http://192.168.1.100:8080/view?tab1=main&tab2=issue&action=form&issueId=" + emailMessage.getIssueId() + "\">\n" +
                        "                    " + issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle() + "</a></h1></td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"padding: 10px;\">\n" +
                        "                    <h3>\n" +
                        "                        <img style=\"width: 32px;\" src=\"http://192.168.1.100:8080/images/avatars/png/avatar-" + avatarId + ".png\" />\n" +
                        "                        " + user.getLogonId() + " " + emailMessage.getAction() + ".\n" +
                        "                    </h3>\n" +
                        "                    <p>" + emailMessage.getDescription() + "</p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </table>\n" +
                        "    </td></tr>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>";

        // create the email message
        ImageHtmlEmail email = new ImageHtmlEmail();
        setConnectionFields(email);

        for (String recipient : recipients)
            email.addTo(recipient, recipient);

        email.setSubject(user.getLogonId() + " " + emailMessage.getAction() + " to " + issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle());

        email.setHtmlMsg(htmlEmailTemplate);

        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");
        email.send();
    }

    private static void setConnectionFields(ImageHtmlEmail email) throws MalformedURLException, EmailException
    {
        email.setHostName(SystemInfo.getEmailHost());
        email.setSmtpPort(SystemInfo.getEmailPort());
        email.setAuthenticator(new DefaultAuthenticator(SystemInfo.getEmailUser(), SystemInfo.getEmailPassword()));
        email.setStartTLSRequired(true);
        email.setFrom(SystemInfo.getEmailFromAddress(), SystemInfo.getEmailFromName());
        // define you base URL to resolve relative resource locations
        URL url = new URL("http://www.apache.org");
        email.setDataSourceResolver(new DataSourceUrlResolver(url));}
}
