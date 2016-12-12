package com.hicks.beans;

import com.hicks.ISelectTagSupport;
import com.hicks.UserSession;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "zones")
public class Zone implements Serializable, ISelectTagSupport
{
//    @Version
//    @Column(name = "version")
//    private Long version;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_SEQ")
    @SequenceGenerator(name="ROLE_SEQ", sequenceName="ROLE_SEQ", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name = "";

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Zone)) return false;
        Zone that = (Zone) obj;
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

    public static List<Zone> getAll()
    {
        return EOI.executeQuery("select * from zones");
    }

    public static List<Zone> getAllForUser(Long userId)
    {
        User user = User.getByUserId(userId);
        if (user.isAdmin() || user.isSupport())
            return Zone.getAll();
        else
        {
            List<ZoneMap> zoneMaps = ZoneMap.getByUserId(userId);
            return zoneMaps.stream().map(zoneMap -> Zone.getById(zoneMap.getZoneId())).collect(Collectors.toList());
        }
    }

    public static Zone getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from zones where id=?", Arrays.asList(id));
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
}
