package net.ehicks.bts.beans;

import net.ehicks.bts.ISelectTagSupport;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "bts_roles")
public class Role implements Serializable, ISelectTagSupport
{
    @Version
    @Column(name = "version")
    private Long version;

    // create sequence eric.role_seq start with 1 increment by 1;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_SEQ")
    @SequenceGenerator(name="ROLE_SEQ", sequenceName="ROLE_SEQ", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "logon_id", nullable = false)
    private String logonId = "";

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName = "";

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
        return roleName;
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

    // -------- Magic ----------
    public static List<Role> getAll()
    {
        return EOI.executeQuery("select * from bts_roles");
    }

    public static Role getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from bts_roles where id=?", Arrays.asList(id));
    }

    public static List<Role> getByUserId(Long userId)
    {
        return EOI.executeQuery("select * from bts_roles where user_id=?", Arrays.asList(userId));
    }

    public static Role getByUserIdAndRoleName(Long userId, String roleName)
    {
        return EOI.executeQueryOneResult("select * from bts_roles where user_id=? and role_name=?", Arrays.asList(userId, roleName));
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

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getLogonId()
    {
        return logonId;
    }

    public void setLogonId(String logonId)
    {
        this.logonId = logonId;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
}
