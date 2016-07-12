package com.hicks.beans;


import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "issues")
public class Issue implements Serializable
{
//    @Version
//    @Column(name = "version")
//    private Long version;

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private String id = "";

    @Column(name = "title", length = 2000)
    private String title = "";
    @Column(name = "description", columnDefinition = "varchar2(32000 CHAR)")
    private String description = "";

    @Column(name = "bucket_id")
    private Long bucketId;
    @Column(name = "zone_id")
    private Long zoneId;
    @Column(name = "issue_type_id")
    private Long issueTypeId;

    @Column(name = "severity")
    private String severity;
    @Column(name = "status")
    private String status;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "created_on")
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Issue)) return false;
        Issue that = (Issue) obj;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 17 * 37 * Integer.valueOf(id);
    }

    public String toString()
    {
        return "Issue " + id;
    }

    // --------

    public static List<Issue> getAll()
    {
        return EOI.executeQuery("select * from films");
    }

    public static Issue getById(String imdbId)
    {
        return EOI.executeQueryOneResult("select * from films where imdb_id=?", Arrays.asList(imdbId));
    }

    // -------- Getters / Setters ----------

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Long getBucketId()
    {
        return bucketId;
    }

    public void setBucketId(Long bucketId)
    {
        this.bucketId = bucketId;
    }

    public Long getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(Long zoneId)
    {
        this.zoneId = zoneId;
    }

    public Long getIssueTypeId()
    {
        return issueTypeId;
    }

    public void setIssueTypeId(Long issueTypeId)
    {
        this.issueTypeId = issueTypeId;
    }

    public String getSeverity()
    {
        return severity;
    }

    public void setSeverity(String severity)
    {
        this.severity = severity;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
}
