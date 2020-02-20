//package net.ehicks.bts.beans;
//
//import net.ehicks.eoi.EOI;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Table(name = "attachments")
//public class Attachment implements Serializable
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false)
//    private Long issueId;
//
//    @Column(nullable = false)
//    private Long dbFileId;
//
//    public Attachment()
//    {
//    }
//
//    public Attachment(Long issueId, Long dbFileId)
//    {
//        this.issueId = issueId;
//        this.dbFileId = dbFileId;
//    }
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof Attachment)) return false;
//        Attachment that = (Attachment) obj;
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
//    public static List<Attachment> getAll()
//    {
//        return EOI.executeQuery("select * from attachments");
//    }
//
//    public DBFile getDbFile()
//    {
//        return DBFile.getById(dbFileId);
//    }
//
//    public Issue getIssue()
//    {
//        return Issue.getById(issueId);
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
//    public Long getIssueId()
//    {
//        return issueId;
//    }
//
//    public void setIssueId(Long issueId)
//    {
//        this.issueId = issueId;
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
//}
