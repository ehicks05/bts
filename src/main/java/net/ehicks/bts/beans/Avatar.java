package net.ehicks.bts.beans;

import net.ehicks.bts.ISelectTagSupport;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "avatars")
public class Avatar implements Serializable, ISelectTagSupport
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "db_file_id")
    private Long dbFileId;

    @Column(name = "thumbnail_db_file_id")
    private Long thumbnailDbFileId;

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
        if (!(obj instanceof Avatar)) return false;
        Avatar that = (Avatar) obj;
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

    @Override
    public String getValue()
    {
        return getDbFile().getId().toString();
    }

    @Override
    public String getText()
    {
        return getDbFile().getName();
    }

    public static List<Avatar> getAll()
    {
        return EOI.executeQuery("select * from avatars");
    }

    public static Avatar getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from avatars where id=?", Arrays.asList(id));
    }

    public static List<Avatar> getByCreatedByUserId(Long createdByUserId)
    {
        return EOI.executeQuery("select * from avatars where created_by_user_id=?", Arrays.asList(createdByUserId));
    }

    public static List<Avatar> getByDbFileId(long dbFileId)
    {
        return EOI.executeQuery("select * from avatars where db_file_id=?", Arrays.asList(dbFileId));
    }

    public DBFile getDbFile()
    {
        return DBFile.getById(dbFileId);
    }

    public DBFile getThumbnailDbFile()
    {
        return DBFile.getById(thumbnailDbFileId);
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

    public Long getDbFileId()
    {
        return dbFileId;
    }

    public void setDbFileId(Long dbFileId)
    {
        this.dbFileId = dbFileId;
    }

    public Long getThumbnailDbFileId()
    {
        return thumbnailDbFileId;
    }

    public void setThumbnailDbFileId(Long thumbnailDbFileId)
    {
        this.thumbnailDbFileId = thumbnailDbFileId;
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
