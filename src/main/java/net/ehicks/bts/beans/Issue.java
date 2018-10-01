package net.ehicks.bts.beans;


import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "issues")
public class Issue implements Serializable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "title", length = 2000)
    private String title = "";
    @Column(name = "description", columnDefinition = "varchar2(32000 CHAR)")
    private String description = "";

    @Column(name = "group_id")
    private Long groupId;
    @Column(name = "issue_type_id")
    private Long issueTypeId;
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "assignee_user_id")
    private Long assigneeUserId;
    @Column(name = "reporter_user_id")
    private Long reporterUserId;
    @Column(name = "severity_id")
    private Long severityId;
    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "created_on")
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @Column(name = "last_updated_by_user_id")
    private Long lastUpdatedByUserId;

    @Column(name = "last_updated_on")
    @Temporal(TemporalType.DATE)
    private Date lastUpdatedOn;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Issue)) return false;
        Issue that = (Issue) obj;
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

    // --------

    public static List<Issue> getAll()
    {
        return EOI.executeQuery("select * from issues");
    }

    public static Issue getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from issues where id=?", Arrays.asList(id));
    }

    public Project getProject()
    {
        return Project.getById(projectId);
    }

    public Group getGroup()
    {
        return Group.getById(groupId);
    }

    public IssueType getIssueType()
    {
        return IssueType.getById(issueTypeId);
    }

    public Severity getSeverity()
    {
        return Severity.getById(severityId);
    }

    public Status getStatus()
    {
        return Status.getById(statusId);
    }

    public User getAssignee()
    {
        return User.getByUserId(assigneeUserId);
    }

    public User getReporter()
    {
        return User.getByUserId(reporterUserId);
    }

    public List<Attachment> getAttachments()
    {
        return Attachment.getByIssueId(id);
    }

    public String getTimeSinceCreation()
    {
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.getCreatedOn().getTime()), ZoneId.systemDefault());
        LocalDateTime until = LocalDateTime.now();
        
        long days = ChronoUnit.DAYS.between(start, until);
        long hours = ChronoUnit.HOURS.between(start, until);
        long minutes = ChronoUnit.MINUTES.between(start, until);

        if (days > 0)
            return days + " days";
        if (hours > 0)
            return hours + " hours";
        if (minutes > 0)
            return minutes + " minutes";

        return "<1 minute";
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Long getIssueTypeId()
    {
        return issueTypeId;
    }

    public void setIssueTypeId(Long issueTypeId)
    {
        this.issueTypeId = issueTypeId;
    }

    public Long getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getAssigneeUserId()
    {
        return assigneeUserId;
    }

    public void setAssigneeUserId(Long assigneeUserId)
    {
        this.assigneeUserId = assigneeUserId;
    }

    public Long getReporterUserId()
    {
        return reporterUserId;
    }

    public void setReporterUserId(Long reporterUserId)
    {
        this.reporterUserId = reporterUserId;
    }

    public Long getSeverityId()
    {
        return severityId;
    }

    public void setSeverityId(Long severityId)
    {
        this.severityId = severityId;
    }

    public Long getStatusId()
    {
        return statusId;
    }

    public void setStatusId(Long statusId)
    {
        this.statusId = statusId;
    }

    public Long getCreatedByUserId()
    {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId)
    {
        this.createdByUserId = createdByUserId;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public Long getLastUpdatedByUserId()
    {
        return lastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId(Long lastUpdatedByUserId)
    {
        this.lastUpdatedByUserId = lastUpdatedByUserId;
    }

    public Date getLastUpdatedOn()
    {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn)
    {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
