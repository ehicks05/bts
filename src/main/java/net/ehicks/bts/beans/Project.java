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
//@Table(name = "projects")
//public class Project implements Serializable, ISelectTagSupport
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String name = "";
//
//    @Column(nullable = false, unique = true)
//    private String prefix = "";
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof Project)) return false;
//        Project that = (Project) obj;
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
//    public static List<Project> getAll()
//    {
//        return EOI.executeQuery("select * from projects");
//    }
//
//    public static Project getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from projects where id=?", Arrays.asList(id));
//    }
//
//    public static Project getByName(String name)
//    {
//        return EOI.executeQueryOneResult("select * from projects where name=?", Arrays.asList(name));
//    }
//
//    public static List<Project> getByUserId(Long userId)
//    {
//        List<ProjectMap> projectMaps = ProjectMap.getByUserId(userId);
//        return projectMaps.stream().map(projectMap -> Project.getById(projectMap.getProjectId())).collect(Collectors.toList());
//    }
//
//    public static List<Project> getAllVisible(Long userId)
//    {
//        User user = User.getByUserId(userId);
//        if (user.isAdmin() || user.isSupport())
//            return Project.getAll();
//        else
//        {
//            return getByUserId(userId);
//        }
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
//    public String getPrefix()
//    {
//        return prefix;
//    }
//
//    public void setPrefix(String prefix)
//    {
//        this.prefix = prefix;
//    }
//}
