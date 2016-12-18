package com.hicks.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "email_messages")
public class EmailMessage implements Serializable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "toAddress")
    private String toAddress = "";

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "issue_id")
    private Long issueId;

    @Column(name = "action")
    private String action = "";

    @Column(name = "action_source_id")
    private Long actionSourceId;

    @Column(name = "description", columnDefinition = "varchar2(32000 CHAR)")
    private String description = "";

    @Column(name = "status")
    private String status = "CREATED";

    @Column(name = "created_on")
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof EmailMessage)) return false;
        EmailMessage that = (EmailMessage) obj;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 17 * 37 * id.intValue();
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + ":" + id.toString();
    }

    // --------

    public static List<EmailMessage> getAll()
    {
        return EOI.executeQuery("select * from email_messages");
    }

    public static EmailMessage getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from email_messages where id=?", Arrays.asList(id));
    }

    public String getSubject()
    {
        if (action.equals("added a comment"))
        {
            User user = User.getByUserId(userId);
            Issue issue = Issue.getById(issueId);
            return user.getLogonId() + " " + action + " to " + issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle();
        }
        if (action.equals("test"))
            return "Test Email";

        return null;
    }

    public String getBody()
    {
        if (action.equals("added a comment"))
        {
            User user = User.getByUserId(userId);
            Issue issue = Issue.getById(issueId);
            String avatarId = String.valueOf(user.getAvatarId());
            if (avatarId.length() == 1)
                avatarId = "0" + avatarId;

            return "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<body>\n" +
                    "<table style=\"width: 100%;background-color: #eee;\">\n" +
                    "    <tr><td>\n" +
                    "        <table style=\"width: 500px;margin:auto;background-color: white\">\n" +
                    "            <tr>\n" +
                    "                <td style=\"padding: 10px;\"><h1><a href=\"http://192.168.1.100:8080/view?tab1=main&tab2=issue&action=form&issueId=" + issueId + "\">\n" +
                    "                    " + issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle() + "</a></h1></td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td style=\"padding: 10px;\">\n" +
                    "                    <h3>\n" +
                    "                        <img style=\"width: 32px;\" src=\"http://192.168.1.100:8080/images/avatars/png/avatar-" + avatarId + ".png\" />\n" +
                    "                        " + user.getLogonId() + " " + action + ".\n" +
                    "                    </h3>\n" +
                    "                    <p>" + description + "</p>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "        </table>\n" +
                    "    </td></tr>\n" +
                    "</table>\n" +
                    "</body>\n" +
                    "</html>";
        }
        if (action.equals("test"))
        {
            return "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<body>\n" +
                    "<table style=\"width: 100%;background-color: #eee;\">\n" +
                    "    <tr><td>\n" +
                    "        <table style=\"width: 500px;margin:auto;background-color: white\">\n" +
                    "            <tr>\n" +
                    "                <td style=\"padding: 10px;\"><h1><a href=\"http://192.168.1.100:8080/view?tab1=main&tab2=dashboard&action=form \">\n" +
                    "                    " + "Test Email" + "</a></h1></td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td style=\"padding: 10px;\">\n" +
                    "                    <h3>\n" +
                    "                        " + "This is a test email.\n" +
                    "                    </h3>\n" +
                    "                    <p>" + "Sent from bts..." + "</p>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "        </table>\n" +
                    "    </td></tr>\n" +
                    "</table>\n" +
                    "</body>\n" +
                    "</html>";
        }
        return null;
    }

    // -------- Getters / Setters ----------


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getToAddress()
    {
        return toAddress;
    }

    public void setToAddress(String toAddress)
    {
        this.toAddress = toAddress;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getIssueId()
    {
        return issueId;
    }

    public void setIssueId(Long issueId)
    {
        this.issueId = issueId;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public Long getActionSourceId()
    {
        return actionSourceId;
    }

    public void setActionSourceId(Long actionSourceId)
    {
        this.actionSourceId = actionSourceId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }
}
