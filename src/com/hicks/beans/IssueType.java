package com.hicks.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "issue_types")
public class IssueType implements Serializable
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
        if (!(obj instanceof IssueType)) return false;
        IssueType that = (IssueType) obj;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 17 * 37 * id.intValue();
    }

    public String toString()
    {
        return name;
    }

    public static List<IssueType> getAll()
    {
        return EOI.executeQuery("select * from issue_types");
    }

    public static Zone getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from issue_types where id=?", Arrays.asList(id));
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
