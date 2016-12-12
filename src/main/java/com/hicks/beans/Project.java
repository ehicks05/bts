package com.hicks.beans;

import com.hicks.ISelectTagSupport;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "projects")
public class Project implements Serializable, ISelectTagSupport
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

    @Column(name = "name", nullable = false, unique = true)
    private String name = "";

    @Column(name = "prefix", nullable = false, unique = true)
    private String prefix = "";

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Project)) return false;
        Project that = (Project) obj;
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
        return id.toString();
    }

    @Override
    public String getText()
    {
        return name;
    }

    public static List<Project> getAll()
    {
        return EOI.executeQuery("select * from projects");
    }

    public static Project getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from projects where id=?", Arrays.asList(id));
    }

    public static List<Project> getAllForUser(Long userId)
    {
        User user = User.getByUserId(userId);
        if (user.isAdmin() || user.isSupport())
            return Project.getAll();
        else
        {
            List<ProjectMap> projectMaps = ProjectMap.getByUserId(userId);
            return projectMaps.stream().map(projectMap -> Project.getById(projectMap.getProjectId())).collect(Collectors.toList());
        }
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
}
