package com.hicks.beans;

import com.hicks.ISelectTagSupport;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bts_users")
public class User implements Serializable, ISelectTagSupport
{
    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_SEQ")
    @SequenceGenerator(name="USER_SEQ", sequenceName="USER_SEQ", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "logon_id", nullable = false, unique = true)
    private String logonId = "";

    @Column(name = "password", nullable = false)
    private String password = "";

    @Column(name = "avatar_id")
    private Long avatarId;

    @Column(name = "created_on")
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @Column(name = "updated_on")
    @Temporal(TemporalType.DATE)
    private Date updatedOn;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof User)) return false;
        User that = (User) obj;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 17 * 37 * id.intValue();
    }

    public String toString()
    {
        return logonId;
    }

    @Override
    public String getValue()
    {
        return id.toString();
    }

    @Override
    public String getText()
    {
        return logonId;
    }

    // --------

    public static List<User> getAll()
    {
        return EOI.executeQuery("select * from bts_users;");
    }

    public static User getByLogonId(String logonid)
    {
        return EOI.executeQueryOneResult("select * from bts_users where logon_id=?;", new ArrayList<>(Arrays.asList(logonid)));
    }

    public static User getByUserId(Long userId)
    {
        return EOI.executeQueryOneResult("select * from bts_users where id=?;", new ArrayList<>(Arrays.asList(userId)));
    }

    public List<Comment> getAllComments()
    {
        return Comment.getByCreatedByUserId(id);
    }

    public List<Role> getAllRoles()
    {
        return Role.getByUserId(id);
    }

    public DBFile getAvatar()
    {
        return DBFile.getById(avatarId);
    }

    public static DBFile getDefaultAvatar()
    {
        return DBFile.getByName("no-avatar.png");
    }
    // -------- Getters / Setters ----------

    public Long getVersion()
    {
        return version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLogonId()
    {
        return logonId;
    }

    public void setLogonId(String logonId)
    {
        this.logonId = logonId;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Long getAvatarId()
    {
        return avatarId;
    }

    public void setAvatarId(Long avatarId)
    {
        this.avatarId = avatarId;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn()
    {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn)
    {
        this.updatedOn = updatedOn;
    }
}
