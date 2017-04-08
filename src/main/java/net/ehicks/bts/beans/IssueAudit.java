package net.ehicks.bts.beans;

import net.ehicks.eoi.AuditUser;
import net.ehicks.eoi.EOI;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "issue_audits")
public class IssueAudit implements Serializable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "issue_id", nullable = false)
    private Long issueId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_ip", nullable = false)
    private String userIp;

    @Column(name = "event_time", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date eventTime;

    @Column(name = "event_type", nullable = false)
    private String eventType = "";

    @Column(name = "object_key", nullable = false)
    private String objectKey = "";

    @Column(name = "field_name")
    private String fieldName = "";

    @Column(name = "old_value")
    private String oldValue = "";

    @Column(name = "new_value")
    private String newValue = "";

    public IssueAudit()
    {
    }

    public IssueAudit(Long issueId, AuditUser auditUser, String eventType, String objectKey)
    {
        this(issueId, auditUser, eventType, objectKey, null, null, null);
    }

    public IssueAudit(Long issueId, AuditUser auditUser, String eventType, String objectKey, String fieldName, String oldValue, String newValue)
    {
        this.issueId = issueId;
        this.userId = auditUser.getId();
        this.userIp = auditUser.getIpAddress();
        this.eventTime = new Date();
        this.eventType = eventType;
        this.objectKey = objectKey;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof IssueAudit)) return false;
        IssueAudit that = (IssueAudit) obj;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 17 * 37 * id.intValue();
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + ":" + id;
    }

    public static List<IssueAudit> getAll()
    {
        return EOI.executeQuery("select * from issue_audits");
    }

    public static IssueAudit getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from issue_audits where id=?", Arrays.asList(id));
    }

    public static List<IssueAudit> getByIssueId(Long issueId)
    {
        return EOI.executeQuery("select * from issue_audits where issue_id=? order by event_time desc", Arrays.asList(issueId));
    }


    public String getUserName()
    {
        String userName = "";
        if (StringUtils.isNumeric(userId))
            userName = " (" + User.getByUserId(Long.valueOf(userId)).getName() + ")";

        return userName;
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

    public Long getIssueId()
    {
        return issueId;
    }

    public void setIssueId(Long issueId)
    {
        this.issueId = issueId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserIp()
    {
        return userIp;
    }

    public void setUserIp(String userIp)
    {
        this.userIp = userIp;
    }

    public Date getEventTime()
    {
        return eventTime;
    }

    public void setEventTime(Date eventTime)
    {
        this.eventTime = eventTime;
    }

    public String getEventType()
    {
        return eventType;
    }

    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    public String getObjectKey()
    {
        return objectKey;
    }

    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public String getOldValue()
    {
        return oldValue;
    }

    public void setOldValue(String oldValue)
    {
        this.oldValue = oldValue;
    }

    public String getNewValue()
    {
        return newValue;
    }

    public void setNewValue(String newValue)
    {
        this.newValue = newValue;
    }
}