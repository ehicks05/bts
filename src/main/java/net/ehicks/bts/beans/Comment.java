//package net.ehicks.bts.beans;
//
//import net.ehicks.eoi.EOI;
//import net.ehicks.eoi.Index;
//import net.ehicks.eoi.Indexes;
//import net.ehicks.eoi.SQLGenerator;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Table(name = "comments")
//@Indexes({
//        @Index(sql = "CREATE INDEX IDX_COMMENT_CREATED_ON ON COMMENTS(created_on desc);"),
//        @Index(sql = "CREATE INDEX IDX_COMMENT_ISSUE_ID ON COMMENTS(issue_id);")
//})
//public class Comment implements Serializable
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false)
//    private Long issueId;
//
//    @Column(columnDefinition = "varchar2(32000 CHAR)")
//    private String content = "";
//
//    private Long visibleToGroupId;
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof Comment)) return false;
//        Comment that = (Comment) obj;
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
//    public static List<Comment> getAll()
//    {
//        return EOI.executeQuery("select * from comments");
//    }
//
//    public static List<Comment> getByIssueId(Long issueId)
//    {
//        return EOI.executeQuery("select * from comments where issue_id=?", Arrays.asList(issueId));
//    }
//
//    public static Comment getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from comments where id=?", Arrays.asList(id));
//    }
//
//    public static List<Comment> getByCreatedByUserId(Long createdByUserId)
//    {
//        return EOI.executeQuery("select * from comments where created_by_user_id=?", Arrays.asList(createdByUserId));
//    }
//
//    public static List<Comment> getRecentCreatedByUserId(Long createdByUserId, int lastN)
//    {
//        return EOI.executeQuery("select * from comments where created_by_user_id=? order by created_on desc" + SQLGenerator.getLimitClause("?", "0"), Arrays.asList(createdByUserId, lastN));
//    }
//
//    public Issue getIssue()
//    {
//        return Issue.getById(issueId);
//    }
//
//    public void setDefaultAvatar()
//    {
//        //
//    }
//    public DBFile getDefaultAvatar()
//    {
//        return DBFile.getByName("no_avatar.png");
//    }
//
//    public Group getVisibleToGroup()
//    {
//        return Group.getById(visibleToGroupId);
//    }
//    // -------- Getters / Setters ----------
//
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
//    public String getContent()
//    {
//        return content;
//    }
//
//    public void setContent(String content)
//    {
//        this.content = content;
//    }
//
//    public Long getVisibleToGroupId()
//    {
//        return visibleToGroupId;
//    }
//
//    public void setVisibleToGroupId(Long visibleToGroupId)
//    {
//        this.visibleToGroupId = visibleToGroupId;
//    }
//}
