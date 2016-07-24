package com.hicks.beans;

import com.hicks.ISelectTagSupport;
import com.hicks.UserSession;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "db_files")
public class DBFile implements Serializable, ISelectTagSupport
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

    @Column(name = "content", nullable = false, unique = true)
    private byte[] content;

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

    public String getBase64()
    {
        return "data:image/png;base64," + Common.byteArrayToBase64(content);
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
}
