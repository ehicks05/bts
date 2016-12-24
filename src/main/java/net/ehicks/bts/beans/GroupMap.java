package net.ehicks.bts.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "group_maps")
public class GroupMap implements Serializable
{
//    @Version
//    @Column(name = "version")
//    private Long version;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_SEQ")
    @SequenceGenerator(name="ROLE_SEQ", sequenceName="ROLE_SEQ", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = false)
    private Long userId;

    @Column(name = "group_id", nullable = false, unique = false)
    private Long groupId;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof GroupMap)) return false;
        GroupMap that = (GroupMap) obj;
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

    public static List<GroupMap> getAll()
    {
        return EOI.executeQuery("select * from group_maps");
    }

    public static List<GroupMap> getByUserId(Long userId)
    {
        return EOI.executeQuery("select * from group_maps where user_id=?", Arrays.asList(userId));
    }

    public static List<GroupMap> getByGroupId(Long groupId)
    {
        return EOI.executeQuery("select * from group_maps where group_id=?", Arrays.asList(groupId));
    }

    public static GroupMap getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from group_maps where id=?", Arrays.asList(id));
    }

    public static GroupMap getByUserIdAndGroupId(Long userId, Long groupId)
    {
        return EOI.executeQueryOneResult("select * from group_maps where user_id=? and group_id=?", Arrays.asList(userId, groupId));
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

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }
}
