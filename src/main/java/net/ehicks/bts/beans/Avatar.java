//package net.ehicks.bts.beans;
//
//import net.ehicks.bts.ISelectTagSupport;
//import net.ehicks.eoi.EOI;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Table(name = "avatars")
//public class Avatar implements Serializable, ISelectTagSupport
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    private Long dbFileId;
//    private Long thumbnailDbFileId;
//    private Boolean publicUse;
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof Avatar)) return false;
//        Avatar that = (Avatar) obj;
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
//        return getDbFile().getId().toString();
//    }
//
//    @Override
//    public String getText()
//    {
//        return getDbFile().getName();
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
//    public Long getDbFileId()
//    {
//        return dbFileId;
//    }
//
//    public void setDbFileId(Long dbFileId)
//    {
//        this.dbFileId = dbFileId;
//    }
//
//    public Long getThumbnailDbFileId()
//    {
//        return thumbnailDbFileId;
//    }
//
//    public void setThumbnailDbFileId(Long thumbnailDbFileId)
//    {
//        this.thumbnailDbFileId = thumbnailDbFileId;
//    }
//
//    public Boolean getPublicUse()
//    {
//        return publicUse;
//    }
//
//    public void setPublicUse(Boolean publicUse)
//    {
//        this.publicUse = publicUse;
//    }
//}
