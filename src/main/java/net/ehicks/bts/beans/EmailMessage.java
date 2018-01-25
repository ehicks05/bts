package net.ehicks.bts.beans;

import net.ehicks.bts.EmailAction;
import net.ehicks.bts.SystemInfo;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

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

    @Column(name = "action_id")
    private Long actionId;

    @Column(name = "comment_id")
    private Long commentId;

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

    public EmailAction getEmailAction()
    {
        return EmailAction.getById(actionId);
    }

    public String getSubject()
    {
        if (actionId == EmailAction.ADD_COMMENT.getId())
        {
            User user = User.getByUserId(userId);
            Issue issue = Issue.getById(issueId);
            return user.getLogonId() + " added a comment to " + issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle();
        }
        if (actionId == EmailAction.EDIT_COMMENT.getId())
        {
            User user = User.getByUserId(userId);
            Issue issue = Issue.getById(issueId);
            return user.getLogonId() + " edited a comment on " + issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle();
        }
        if (actionId == EmailAction.TEST.getId())
            return "Test Email";

        return null;
    }

    public String getBody()
    {
        String emailContext = "http://localhost:8080/bts"; // todo figure this out
        if (actionId == EmailAction.ADD_COMMENT.getId())
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
                    "                <td style=\"padding: 10px;\"><h1><a href=\"" + emailContext + "/view?tab1=issue&action=form&issueId=" + issueId + "\">\n" +
                    "                    " + issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle() + "</a></h1></td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td style=\"padding: 10px;\">\n" +
                    "                    <h3>\n" +
                    "                        <img style=\"width: 32px;\" src=\"" + emailContext + "/images/avatars/png/avatar-" + avatarId + ".png\" />\n" +
                    "                        " + user.getLogonId() + " " + EmailAction.getById(actionId).getVerb() + ".\n" +
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
        if (actionId == EmailAction.EDIT_COMMENT.getId())
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
                    "                <td style=\"padding: 10px;\"><h1><a href=\"" + emailContext + "/view?tab1=issue&action=form&issueId=" + issueId + "\">\n" +
                    "                    " + issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle() + "</a></h1></td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td style=\"padding: 10px;\">\n" +
                    "                    <h3>\n" +
                    "                        <img style=\"width: 32px;\" src=\"" + emailContext + "/images/avatars/png/avatar-" + avatarId + ".png\" />\n" +
                    "                        " + user.getLogonId() + " " + EmailAction.getById(actionId).getVerb() + ".\n" +
                    "                    </h3>\n" +
                    "                    <blockquote>" + description + "</blockquote>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "        </table>\n" +
                    "    </td></tr>\n" +
                    "</table>\n" +
                    "</body>\n" +
                    "</html>";
        }
        if (actionId == EmailAction.TEST.getId())
        {
            return "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<body>\n" +
                    "<table style=\"width: 100%;background-color: #eee;\">\n" +
                    "    <tr><td>\n" +
                    "        <table style=\"width: 500px;margin:auto;background-color: white\">\n" +
                    "            <tr>\n" +
                    "                <td style=\"padding: 10px;\"><h1><a href=\"" + emailContext + "/view?tab1=dashboard&action=form \">\n" +
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

    public String getStatusIcon()
    {
        Map<String, String> statusToIcon = new HashMap<>();
        statusToIcon.put("CREATED", "hourglass-start has-text-info");
        statusToIcon.put("WAITING", "hourglass-half has-text-warning");
        statusToIcon.put("SENT", "check has-text-success");
        statusToIcon.put("FAILED", "ban has-text -danger");
        return statusToIcon.getOrDefault(status, "question is-warning");
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

    public Long getActionId()
    {
        return actionId;
    }

    public void setActionId(Long actionId)
    {
        this.actionId = actionId;
    }

    public Long getCommentId()
    {
        return commentId;
    }

    public void setCommentId(Long commentId)
    {
        this.commentId = commentId;
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
