//package net.ehicks.bts.beans;
//
//import net.ehicks.bts.mail.EmailAction;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.*;
//
//@Entity
//@Table(name = "email_messages")
//public class EmailMessage2 implements Serializable
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    private String toAddress = "";
//    private Long userId;
//    private Long issueId;
//    private Long actionId;
//    private Long commentId;
//    private String status = "CREATED";
//
//    @Column(columnDefinition = "varchar2(32000 CHAR)")
//    private String description = "";
//
//    public String toString()
//    {
//        return this.getClass().getSimpleName() + ":" + id.toString();
//    }
//
//    // --------
//
//    public EmailAction getEmailAction()
//    {
//        return EmailAction.getById(actionId);
//    }
//
//    public String getStatusIcon()
//    {
//        Map<String, String> statusToIcon = new HashMap<>();
//        statusToIcon.put("CREATED", "hourglass-start has-text-info");
//        statusToIcon.put("WAITING", "hourglass-half has-text-warning");
//        statusToIcon.put("SENT", "check has-text-success");
//        statusToIcon.put("FAILED", "ban has-text-danger");
//        return statusToIcon.getOrDefault(status, "question is-warning");
//    }
//    // -------- Getters / Setters ----------
//
//
//    public Long getId()
//    {
//        return id;
//    }
//
//    public void setId(Long id)
//    {
//        this.id = id;
//    }
//
//    public String getToAddress()
//    {
//        return toAddress;
//    }
//
//    public void setToAddress(String toAddress)
//    {
//        this.toAddress = toAddress;
//    }
//
//    public Long getUserId()
//    {
//        return userId;
//    }
//
//    public void setUserId(Long userId)
//    {
//        this.userId = userId;
//    }
//
//    public Long getIssueId()
//    {
//        return issueId;
//    }
//
//    public void setIssueId(Long issueId)
//    {
//        this.issueId = issueId;
//    }
//
//    public Long getActionId()
//    {
//        return actionId;
//    }
//
//    public void setActionId(Long actionId)
//    {
//        this.actionId = actionId;
//    }
//
//    public Long getCommentId()
//    {
//        return commentId;
//    }
//
//    public void setCommentId(Long commentId)
//    {
//        this.commentId = commentId;
//    }
//
//    public String getStatus()
//    {
//        return status;
//    }
//
//    public void setStatus(String status)
//    {
//        this.status = status;
//    }
//
//    public String getDescription()
//    {
//        return description;
//    }
//
//    public void setDescription(String description)
//    {
//        this.description = description;
//    }
//}
