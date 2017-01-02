package net.ehicks.bts.beans;

import net.ehicks.eoi.EOI;
import net.ehicks.eoi.Index;
import net.ehicks.eoi.Indexes;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
@Indexes({
        @Index(sql = "CREATE INDEX INDEX_COMMENT_CREATED_ON ON COMMENTS(created_on desc);")
})
public class Comment implements Serializable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "issue_id", nullable = false)
    private Long issueId;

    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(name = "content", columnDefinition = "varchar2(32000 CHAR)")
    private String content = "";

    @Column(name = "visible_to_group_id")
    private Long visibleToGroupId;

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

    public static List<Comment> getByCreatedByUserId(Long createdByUserId)
    {
        return EOI.executeQuery("select * from comments where created_by_user_id=?", Arrays.asList(createdByUserId));
    }

    public static List<Comment> getRecentCreatedByUserId(Long createdByUserId, int lastN)
    {
        return EOI.executeQuery("select * from comments where created_by_user_id=? order by created_on desc limit ? offset 0", Arrays.asList(createdByUserId, lastN));
    }

    public Issue getIssue()
    {
        return Issue.getById(issueId);
    }

    public User getCreatedBy ()
    {
        return User.getByUserId(createdByUserId);
    }

    public String getCreatedByLogonId()
    {
        return User.getByUserId(createdByUserId).getLogonId();
    }

    public void setDefaultAvatar()
    {
        //
    }
    public DBFile getDefaultAvatar()
    {
        return DBFile.getByName("no_avatar.png");
    }

    public Group getVisibleToGroup()
    {
        return Group.getById(visibleToGroupId);
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

    public Long getVisibleToGroupId()
    {
        return visibleToGroupId;
    }

    public void setVisibleToGroupId(Long visibleToGroupId)
    {
        this.visibleToGroupId = visibleToGroupId;
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

    public Date getLastUpdatedOn()
    {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn)
    {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Long getLastUpdatedByUserId()
    {
        return lastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId(Long lastUpdatedByUserId)
    {
        this.lastUpdatedByUserId = lastUpdatedByUserId;
    }
}
