package net.ehicks.bts.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "zone_maps")
public class ZoneMap implements Serializable
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

    @Column(name = "zone_id", nullable = false, unique = false)
    private Long zoneId;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ZoneMap)) return false;
        ZoneMap that = (ZoneMap) obj;
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

    public static List<ZoneMap> getAll()
    {
        return EOI.executeQuery("select * from zone_maps");
    }

    public static List<ZoneMap> getByUserId(Long userId)
    {
        return EOI.executeQuery("select * from zone_maps where user_id=?", Arrays.asList(userId));
    }

    public static List<ZoneMap> getByZoneId(Long zoneId)
    {
        return EOI.executeQuery("select * from zone_maps where zone_id=?", Arrays.asList(zoneId));
    }

    public static ZoneMap getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from zone_maps where id=?", Arrays.asList(id));
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

    public Long getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(Long zoneId)
    {
        this.zoneId = zoneId;
    }
}
