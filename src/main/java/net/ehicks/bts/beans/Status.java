package net.ehicks.bts.beans;

import net.ehicks.bts.ISelectTagSupport;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "statuses")
public class Status implements Serializable, ISelectTagSupport
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name = "";

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Status)) return false;
        Status that = (Status) obj;
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

    public static List<Status> getAll()
    {
        return EOI.executeQuery("select * from statuses");
    }

    public static Status getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from statuses where id=?", Arrays.asList(id));
    }

    public static Status getByName(String name)
    {
        return EOI.executeQueryOneResult("select * from statuses where name=?", Arrays.asList(name));
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
