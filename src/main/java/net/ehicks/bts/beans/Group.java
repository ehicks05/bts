//package net.ehicks.bts.beans;
//
//import net.ehicks.bts.ISelectTagSupport;
//import net.ehicks.eoi.EOI;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Entity
//@Table(name = "groups")
//public class Group implements Serializable, ISelectTagSupport
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String name = "";
//
//    private Boolean admin;
//    private Boolean support;
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof Group)) return false;
//        Group that = (Group) obj;
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
//        return this.getClass().getSimpleName() + ":" + id.toString();
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
//        return name;
//    }
//
//    public static List<Group> getAll()
//    {
//        return EOI.executeQuery("select * from groups");
//    }
//
//    public static Group getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from groups where id=?", Arrays.asList(id));
//    }
//
//    public static Group getByName(String name)
//    {
//        return EOI.executeQueryOneResult("select * from groups where name=?", Arrays.asList(name));
//    }
//
//    public static List<Group> getByUserId(Long userId)
//    {
//        List<GroupMap> groupMaps = GroupMap.getByUserId(userId);
//        return groupMaps.stream()
//                .map(groupMap -> Group.getById(groupMap.getGroupId()))
//                .collect(Collectors.toList());
//    }
//
//    public static List<Group> getAllVisible(Long userId)
//    {
//        User user = User.getByUserId(userId);
//        if (user.isAdmin() || user.isSupport())
//            return Group.getAll();
//        else
//        {
//            return getByUserId(userId);
//        }
//    }
//
//    // -------- Getters / Setters ----------
//
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
//    public String getName()
//    {
//        return name;
//    }
//
//    public void setName(String name)
//    {
//        this.name = name;
//    }
//
//    public Boolean getAdmin()
//    {
//        return admin;
//    }
//
//    public void setAdmin(Boolean admin)
//    {
//        this.admin = admin;
//    }
//
//    public Boolean getSupport()
//    {
//        return support;
//    }
//
//    public void setSupport(Boolean support)
//    {
//        this.support = support;
//    }
//}
