package net.ehicks.bts.beans;

import net.ehicks.bts.ISelectTagSupport;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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

    @Column(name = "first_name")
    private String firstName = "";

    @Column(name = "last_name")
    private String lastName = "";

    @Column(name = "enabled")
    private Boolean enabled;

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
        return this.getClass().getSimpleName() + ":" + id;
    }

    @Override
    public String getValue()
    {
        return id.toString();
    }

    @Override
    public String getText()
    {
        return getName();
    }

    // --------

    public static List<User> getAll()
    {
        return EOI.executeQuery("select * from bts_users;");
    }

    public static List<User> getAllForUser(Long userId)
    {
        User user = User.getByUserId(userId);
        if (user.isAdmin() || user.isSupport())
            return User.getAll();
        else
        {
            List<Zone> zones = Zone.getAllForUser(userId);
            Set<User> users = new HashSet<>();
            for (Zone zone : zones)
                users.addAll(getAllUsersInZone(zone.getId()));
            return new ArrayList<>(users);
        }
    }

    public static List<User> getAllUsersInZone(Long zoneId)
    {
        return EOI.executeQuery("select * from bts_users where id in (select user_id from zone_maps where zone_id=?)", new ArrayList<>(Arrays.asList(zoneId)));
    }

    public static User getByLogonId(String logonid)
    {
        return EOI.executeQueryOneResult("select * from bts_users where logon_id=?;", new ArrayList<>(Arrays.asList(logonid)));
    }

    public static User getByUserId(Long userId)
    {
        return EOI.executeQueryOneResult("select * from bts_users where id=?;", new ArrayList<>(Arrays.asList(userId)));
    }

    public List<Comment> getRecentComments()
    {
        return Comment.getRecentCreatedByUserId(id, 10);
    }

    public List<Role> getAllRoles()
    {
        return Role.getByUserId(id);
    }

    public List<Group> getAllGroups()
    {
        return Group.getByUserId(id);
    }

    public List<Long> getAllGroupIds()
    {
        return Group.getByUserId(id).stream().map(Group::getId).collect(Collectors.toList());
    }

    public List<Zone> getAllZones()
    {
        return Zone.getAllForUser(id);
    }

    public List<Project> getAllProjects()
    {
        return Project.getAllForUser(id);
    }

    public DBFile getAvatar()
    {
        return DBFile.getById(avatarId);
    }

    public static DBFile getDefaultAvatar()
    {
        return DBFile.getByName("no-avatar.png");
    }

    public boolean isAdmin()
    {
        List<Group> groups =  GroupMap.getByUserId(id).stream().map(groupMap -> Group.getById(groupMap.getGroupId())).collect(Collectors.toList());
        for (Group group : groups)
            if (group.getAdmin())
                return true;
        return false;
    }

    public boolean isSupport()
    {
        return GroupMap.getByUserId(id).stream().map(groupMap -> Group.getById(groupMap.getId())).anyMatch(Group::getSupport);
    }

    public boolean isCustomer()
    {
        return !(isAdmin() || isSupport());
    }

    public String getName()
    {
        return firstName + " " + lastName;
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

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
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
