//package net.ehicks.bts.beans;
//
//import net.ehicks.bts.ISelectTagSupport;
//import net.ehicks.eoi.EOI;
//import org.springframework.data.jpa.domain.AbstractPersistable;
//import org.springframework.security.core.GrantedAuthority;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.List;
//
//@Entity
//@Table(name = "bts_roles")
//public class Role extends AbstractPersistable<Long> implements GrantedAuthority, Serializable, ISelectTagSupport
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false)
//    private Long userId;
//
//    @Column(nullable = false)
//    private String logonId = "";
//
//    @Column(nullable = false, unique = true)
//    private String role = "";
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof User)) return false;
//        User that = (User) obj;
//        return this.id.equals(that.getId());
//    }
//
//    @Override
//    public int hashCode()
//    {
//        return 17 * 37 * id.intValue();
//    }
//
//    public String toString()
//    {
//        return role;
//    }
//
//    @Override
//    public String getValue()
//    {
//        return id.toString();
//    }
//
//    @Override
//    public String getText()
//    {
//        return logonId;
//    }
//
//    // -------- Magic ----------
//    public static List<Role> getAll()
//    {
//        return EOI.executeQuery("select * from bts_roles");
//    }
//
//    public static Role getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from bts_roles where id=?", Arrays.asList(id));
//    }
//
//    public static List<Role> getByUserId(Long userId)
//    {
//        return EOI.executeQuery("select * from bts_roles where user_id=?", Arrays.asList(userId));
//    }
//
//    public static Role getByUserIdAndRoleName(Long userId, String roleName)
//    {
//        return EOI.executeQueryOneResult("select * from bts_roles where user_id=? and role_name=?", Arrays.asList(userId, roleName));
//    }
//
//
//    @Override
//    public String getAuthority()
//    {
//        return role;
//    }
//
//    // -------- Getters / Setters ----------
//
//    public Long getId()
//    {
//        return id;
//    }
//
//    public void setId(Long id)
//    {
//        this.id = id;
//    }
//
//    public Long getUserId()
//    {
//        return userId;
//    }
//
//    public void setUserId(Long userId)
//    {
//        this.userId = userId;
//    }
//
//    public String getLogonId()
//    {
//        return logonId;
//    }
//
//    public void setLogonId(String logonId)
//    {
//        this.logonId = logonId;
//    }
//
//    public String getRole()
//    {
//        return role;
//    }
//
//    public void setRole(String role)
//    {
//        this.role = role;
//    }
//}
