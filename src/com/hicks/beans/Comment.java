package com.hicks.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment implements Serializable
{
//    @Version
//    @Column(name = "version")
//    private Long version;

    // create sequence eric.role_seq start with 1 increment by 1;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_SEQ")
    @SequenceGenerator(name="ROLE_SEQ", sequenceName="ROLE_SEQ", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "issue_id", nullable = false)
    private Long issueId;

    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(name = "content", nullable = false, unique = true)
    private String content = "";

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "created_on")
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Comment)) return false;
        Comment that = (Comment) obj;
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

    public static List<Comment> getAll()
    {
        return EOI.executeQuery("select * from comments");
    }

    public static List<Comment> getByIssueId(Long issueId)
    {
        return EOI.executeQuery("select * from comments where issue_id=?", Arrays.asList(issueId));
    }

    public static Comment getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from comments where id=?", Arrays.asList(id));
    }

    public User getCreatedBy ()
    {
        return User.getByUserId(createdByUserId);
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

    public Long getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(Long zoneId)
    {
        this.zoneId = zoneId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
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
