package net.ehicks.bts.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "attachments")
public class Attachment implements Serializable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "issue_id", nullable = false)
    private Long issueId;

    @Column(name = "db_file_id", nullable = false)
    private Long dbFileId;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "created_on")
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @Column(name = "last_updated_by_user_id")
    private Long lastUpdatedByUserId;

    @Column(name = "last_updated_on")
    @Temporal(TemporalType.DATE)
    private Date lastUpdatedOn;

    public Attachment()
    {
    }

    public Attachment(Long issueId, Long dbFileId, Long createdByUserId)
    {
        this.issueId = issueId;
        this.dbFileId = dbFileId;
        this.createdByUserId = createdByUserId;
        this.createdOn = new Date();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Attachment)) return false;
        Attachment that = (Attachment) obj;
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

    public static List<Attachment> getAll()
    {
        return EOI.executeQuery("select * from attachments");
    }

    public static List<Attachment> getByIssueId(Long issueId)
    {
        return EOI.executeQuery("select * from attachments where issue_id=?", Arrays.asList(issueId));
    }

    public static Attachment getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from attachments where id=?", Arrays.asList(id));
    }

    public static List<Attachment> getByCreatedByUserId(Long createdByUserId)
    {
        return EOI.executeQuery("select * from attachments where created_by_user_id=?", Arrays.asList(createdByUserId));
    }

    public static List<Attachment> getByDbFileId(long dbFileId)
    {
        return EOI.executeQuery("select * from attachments where db_file_id=?", Arrays.asList(dbFileId));
    }

    public DBFile getDbFile()
    {
        return DBFile.getById(dbFileId);
    }

    public Issue getIssue()
    {
        return Issue.getById(issueId);
    }

    public User getCreatedBy()
    {
        return User.getByUserId(createdByUserId);
    }

    public String getCreatedByLogonId()
    {
        return User.getByUserId(createdByUserId).getLogonId();
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

    public Long getDbFileId()
    {
        return dbFileId;
    }

    public void setDbFileId(Long dbFileId)
    {
        this.dbFileId = dbFileId;
    }

    public Long getCreatedByUserId()
    {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId)
    {
        this.createdByUserId = createdByUserId;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public Long getLastUpdatedByUserId()
    {
        return lastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId(Long lastUpdatedByUserId)
    {
        this.lastUpdatedByUserId = lastUpdatedByUserId;
    }

    public Date getLastUpdatedOn()
    {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn)
    {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
