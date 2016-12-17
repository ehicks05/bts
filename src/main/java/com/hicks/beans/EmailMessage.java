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
