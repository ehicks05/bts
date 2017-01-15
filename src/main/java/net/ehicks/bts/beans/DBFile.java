package net.ehicks.bts.beans;

import net.ehicks.bts.ISelectTagSupport;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "db_files")
public class DBFile implements Serializable, ISelectTagSupport
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name = "";

    @Column(name = "content", nullable = false)
    private byte[] content;

    @Column(name = "length", nullable = false)
    private Long length;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof DBFile)) return false;
        DBFile that = (DBFile) obj;
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

    public static List<DBFile> getAll()
    {
        return EOI.executeQuery("select * from db_files");
    }

    public static DBFile getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from db_files where id=?", Arrays.asList(id));
    }

    public static DBFile getByName(String name)
    {
        return EOI.executeQueryOneResult("select * from db_files where name=?", Arrays.asList(name));
    }

    public String getBase64()
    {
        return "data:image/png;base64," + Common.byteArrayToBase64(content);
    }

    public String getLengthPretty()
    {
        return Common.toMetric(length);
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

    public byte[] getContent()
    {
        return content;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    public Long getLength()
    {
        return length;
    }

    public void setLength(Long length)
    {
        this.length = length;
    }
}
