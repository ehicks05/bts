package net.ehicks.bts.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "project_maps")
public class ProjectMap implements Serializable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = false)
    private Long userId;

    @Column(name = "project_id", nullable = false, unique = false)
    private Long projectId;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ProjectMap)) return false;
        ProjectMap that = (ProjectMap) obj;
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

    public static List<ProjectMap> getAll()
    {
        return EOI.executeQuery("select * from project_maps");
    }

    public static List<ProjectMap> getByUserId(Long userId)
    {
        return EOI.executeQuery("select * from project_maps where user_id=?", Arrays.asList(userId));
    }

    public static List<ProjectMap> getByProjectId(Long projectId)
    {
        return EOI.executeQuery("select * from project_maps where project_id=?", Arrays.asList(projectId));
    }

    public static ProjectMap getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from project_maps where id=?", Arrays.asList(id));
    }

    public static GroupMap getByUserIdAndProjectId(Long userId, Long projectId)
    {
        return EOI.executeQueryOneResult("select * from project_maps where user_id=? and project_id", Arrays.asList(userId, projectId));
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

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }
}
