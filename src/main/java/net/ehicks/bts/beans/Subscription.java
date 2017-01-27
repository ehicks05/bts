package net.ehicks.bts.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "subscriptions")
public class Subscription implements Serializable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "group_ids")
    private String groupIds;
    @Column(name = "issue_type_ids")
    private String issueTypeIds;
    @Column(name = "project_ids")
    private String projectIds;
    @Column(name = "assignee_user_ids")
    private String assigneeUserIds;
    @Column(name = "reporter_user_ids")
    private String reporterUserIds;
    @Column(name = "severity_ids")
    private String severityIds;
    @Column(name = "status_ids")
    private String statusIds;

    public void updateFields(Long userId, String statusIds, String severityIds, String projectIds, String groupIds, String assigneeUserIds)
    {
        this.userId = userId;
        this.statusIds = statusIds;
        this.severityIds = severityIds;
        this.projectIds = projectIds;
        this.groupIds = groupIds;
        this.assigneeUserIds = assigneeUserIds;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Subscription)) return false;
        Subscription that = (Subscription) obj;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 17 * 37 * id.intValue();
    }

    public String toString()
    {
        String thisId = id == null ? "" : id.toString();
        return this.getClass().getSimpleName() + ":" + thisId;
    }

    // --------

    public static List<Subscription> getAll()
    {
        return EOI.executeQuery("select * from subscriptions");
    }

    public static Subscription getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from subscriptions where id=?", Arrays.asList(id));
    }

    public static List<Subscription> getByUserId(Long userId)
    {
        return EOI.executeQuery("select * from subscriptions where user_id=?", Arrays.asList(userId));
    }

    public List<String> getProjectIdsAsList()
    {
        if (projectIds == null)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(projectIds.split(",")));
    }

    public List<String> getGroupIdsAsList()
    {
        if (groupIds == null)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(groupIds.split(",")));
    }

    public List<String> getSeverityIdsAsList()
    {
        if (severityIds == null)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(severityIds.split(",")));
    }

    public List<String> getStatusIdsAsList()
    {
        if (statusIds == null)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(statusIds.split(",")));
    }

    public List<String> getAssigneeUserIdsAsList()
    {
        if (assigneeUserIds == null)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(assigneeUserIds.split(",")));
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

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getGroupIds()
    {
        return groupIds;
    }

    public void setGroupIds(String groupIds)
    {
        this.groupIds = groupIds;
    }

    public String getIssueTypeIds()
    {
        return issueTypeIds;
    }

    public void setIssueTypeIds(String issueTypeIds)
    {
        this.issueTypeIds = issueTypeIds;
    }

    public String getProjectIds()
    {
        return projectIds;
    }

    public void setProjectIds(String projectIds)
    {
        this.projectIds = projectIds;
    }

    public String getAssigneeUserIds()
    {
        return assigneeUserIds;
    }

    public void setAssigneeUserIds(String assigneeUserIds)
    {
        this.assigneeUserIds = assigneeUserIds;
    }

    public String getReporterUserIds()
    {
        return reporterUserIds;
    }

    public void setReporterUserIds(String reporterUserIds)
    {
        this.reporterUserIds = reporterUserIds;
    }

    public String getSeverityIds()
    {
        return severityIds;
    }

    public void setSeverityIds(String severityIds)
    {
        this.severityIds = severityIds;
    }

    public String getStatusIds()
    {
        return statusIds;
    }

    public void setStatusIds(String statusIds)
    {
        this.statusIds = statusIds;
    }
}
