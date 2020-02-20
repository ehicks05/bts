//package net.ehicks.bts.beans;
//
//import net.ehicks.eoi.EOI;
//import net.ehicks.eoi.Index;
//import net.ehicks.eoi.Indexes;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Arrays;
//import java.util.List;
//
//@Entity
//@Table(name = "issues")
//@Indexes({
//        @Index(sql = "CREATE INDEX  ON ISSUES USING gin (title gin_trgm_ops);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES USING gin (description gin_trgm_ops);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(title);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(description);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(group_id);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(issue_type_id);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(project_id);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(assignee_user_id);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(reporter_user_id);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(severity_id);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(created_on);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(last_updated_on);"),
//        @Index(sql = "CREATE INDEX  ON ISSUES(status_id);")
//})
//public class Issue implements Serializable
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(length = 2000)
//    private String title = "";
//    @Column(columnDefinition = "varchar2(32000 CHAR)")
//    private String description = "";
//
//    private Long groupId;
//    private Long issueTypeId;
//    private Long projectId;
//    private Long assigneeUserId;
//    private Long reporterUserId;
//    private Long severityId;
//    private Long statusId;
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof Issue)) return false;
//        Issue that = (Issue) obj;
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
//    // --------
//
//    public static List<Issue> getAll()
//    {
//        return EOI.executeQuery("select * from issues");
//    }
//
//    public static Issue getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from issues where id=?", Arrays.asList(id));
//    }
//
//    public Project getProject()
//    {
//        return Project.getById(projectId);
//    }
//
//    public Group getGroup()
//    {
//        return Group.getById(groupId);
//    }
//
//    public IssueType getIssueType()
//    {
//        return IssueType.getById(issueTypeId);
//    }
//
//    public Severity getSeverity()
//    {
//        return Severity.getById(severityId);
//    }
//
//    public Status getStatus()
//    {
//        return Status.getById(statusId);
//    }
//
//    public User getAssignee()
//    {
//        return User.getByUserId(assigneeUserId);
//    }
//
//    public User getReporter()
//    {
//        return User.getByUserId(reporterUserId);
//    }
//
//    public List<Attachment> getAttachments()
//    {
//        return Attachment.getByIssueId(id);
//    }
//
//    public String getTimeSinceCreation()
//    {
////        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.getCreatedOn().getTime()), ZoneId.systemDefault());
//        LocalDateTime start = LocalDateTime.now();
//        LocalDateTime until = LocalDateTime.now();
//
//        long days = ChronoUnit.DAYS.between(start, until);
//        long hours = ChronoUnit.HOURS.between(start, until);
//        long minutes = ChronoUnit.MINUTES.between(start, until);
//
//        if (days > 0)
//            return days + " days";
//        if (hours > 0)
//            return hours + " hours";
//        if (minutes > 0)
//            return minutes + " minutes";
//
//        return "<1 minute";
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
//    public String getTitle()
//    {
//        return title;
//    }
//
//    public void setTitle(String title)
//    {
//        this.title = title;
//    }
//
//    public String getDescription()
//    {
//        return description;
//    }
//
//    public void setDescription(String description)
//    {
//        this.description = description;
//    }
//
//    public Long getGroupId()
//    {
//        return groupId;
//    }
//
//    public void setGroupId(Long groupId)
//    {
//        this.groupId = groupId;
//    }
//
//    public Long getIssueTypeId()
//    {
//        return issueTypeId;
//    }
//
//    public void setIssueTypeId(Long issueTypeId)
//    {
//        this.issueTypeId = issueTypeId;
//    }
//
//    public Long getProjectId()
//    {
//        return projectId;
//    }
//
//    public void setProjectId(Long projectId)
//    {
//        this.projectId = projectId;
//    }
//
//    public Long getAssigneeUserId()
//    {
//        return assigneeUserId;
//    }
//
//    public void setAssigneeUserId(Long assigneeUserId)
//    {
//        this.assigneeUserId = assigneeUserId;
//    }
//
//    public Long getReporterUserId()
//    {
//        return reporterUserId;
//    }
//
//    public void setReporterUserId(Long reporterUserId)
//    {
//        this.reporterUserId = reporterUserId;
//    }
//
//    public Long getSeverityId()
//    {
//        return severityId;
//    }
//
//    public void setSeverityId(Long severityId)
//    {
//        this.severityId = severityId;
//    }
//
//    public Long getStatusId()
//    {
//        return statusId;
//    }
//
//    public void setStatusId(Long statusId)
//    {
//        this.statusId = statusId;
//    }
//}
