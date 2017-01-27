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
    private String groupIds = "";
    @Column(name = "issue_type_ids")
    private String issueTypeIds = "";
    @Column(name = "project_ids")
    private String projectIds = "";
    @Column(name = "assignee_user_ids")
    private String assigneeUserIds = "";
    @Column(name = "reporter_user_ids")
    private String reporterUserIds = "";
    @Column(name = "severity_ids")
    private String severityIds = "";
    @Column(name = "status_ids")
    private String statusIds = "";

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
        if (projectIds == null || projectIds.length() == 0)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(projectIds.split(",")));
    }

    public List<String> getGroupIdsAsList()
    {
        if (groupIds == null || groupIds.length() == 0)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(groupIds.split(",")));
    }

    public List<String> getSeverityIdsAsList()
    {
        if (severityIds == null || severityIds.length() == 0)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(severityIds.split(",")));
    }

    public List<String> getStatusIdsAsList()
    {
        if (statusIds == null || statusIds.length() == 0)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(statusIds.split(",")));
    }

    public List<String> getAssigneeUserIdsAsList()
    {
        if (assigneeUserIds == null || assigneeUserIds.length() == 0)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(assigneeUserIds.split(",")));
    }

    public List<String> getReporterUserIdsAsList()
    {
        if (reporterUserIds == null || reporterUserIds.length() == 0)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(reporterUserIds.split(",")));
    }

    // todo: generisize
    public String getDescription()
    {
        String description = "";

        if (getReporterUserIdsAsList().size() > 0)
        {
            if (description.length() > 0)
                description += " AND ";
            description += " were reported by ";
            String clause = "";
            for (String id : getReporterUserIdsAsList())
            {
                if (clause.length() > 0)
                {
                    if (getReporterUserIdsAsList().indexOf(id) == getReporterUserIdsAsList().size() - 1)
                    {
                        if (getReporterUserIdsAsList().indexOf(id) > 1)
                            clause += ",";
                        clause += " or ";
                    }
                    else
                        clause += ", ";
                }
                clause += User.getByUserId(Long.valueOf(id)).getName();
            }
            description += clause;
        }
        if (getAssigneeUserIdsAsList().size() > 0)
        {
            if (description.length() > 0)
                description += " AND ";
            description += " are assigned to ";
            String clause = "";
            for (String id : getAssigneeUserIdsAsList())
            {
                if (clause.length() > 0)
                {
                    if (getAssigneeUserIdsAsList().indexOf(id) == getAssigneeUserIdsAsList().size() - 1)
                    {
                        if (getAssigneeUserIdsAsList().indexOf(id) > 1)
                            clause += ",";
                        clause += " or ";
                    }
                    else
                        clause += ", ";
                }
                clause += User.getByUserId(Long.valueOf(id)).getName();
            }
            description += clause;
        }
        if (getGroupIdsAsList().size() > 0)
        {
            if (description.length() > 0)
                description += " AND ";
            description += " are in group ";
            String clause = "";
            for (String id : getGroupIdsAsList())
            {
                if (clause.length() > 0)
                {
                    if (getGroupIdsAsList().indexOf(id) == getGroupIdsAsList().size() - 1)
                    {
                        if (getGroupIdsAsList().indexOf(id) > 1)
                            clause += ",";
                        clause += " or ";
                    }
                    else
                        clause += ", ";
                }
                clause += Group.getById(Long.valueOf(id)).getName();
            }
            description += clause;
        }
        if (getProjectIdsAsList().size() > 0)
        {
            if (description.length() > 0)
                description += " AND ";
            description += " are in project ";
            String clause = "";
            for (String id : getProjectIdsAsList())
            {
                if (clause.length() > 0)
                {
                    if (getProjectIdsAsList().indexOf(id) == getProjectIdsAsList().size() - 1)
                    {
                        if (getProjectIdsAsList().indexOf(id) > 1)
                            clause += ",";
                        clause += " or ";
                    }
                    else
                        clause += ", ";
                }
                clause += Project.getById(Long.valueOf(id)).getName();
            }
            description += clause;
        }
        if (getSeverityIdsAsList().size() > 0)
        {
            if (description.length() > 0)
                description += " AND ";
            description += " have a severity of ";
            String clause = "";
            for (String id : getSeverityIdsAsList())
            {
                if (clause.length() > 0)
                {
                    if (getSeverityIdsAsList().indexOf(id) == getSeverityIdsAsList().size() - 1)
                    {
                        if (getSeverityIdsAsList().indexOf(id) > 1)
                            clause += ",";
                        clause += " or ";
                    }
                    else
                        clause += ", ";
                }
                clause += Severity.getById(Long.valueOf(id)).getName();
            }
            description += clause;
        }
        if (getStatusIdsAsList().size() > 0)
        {
            if (description.length() > 0)
                description += " AND ";
            description += " have a status of ";
            String clause = "";
            for (String id : getStatusIdsAsList())
            {
                if (clause.length() > 0)
                {
                    if (getStatusIdsAsList().indexOf(id) == getStatusIdsAsList().size() - 1)
                    {
                        if (getStatusIdsAsList().indexOf(id) > 1)
                            clause += ",";
                        clause += " or ";
                    }
                    else
                        clause += ", ";
                }
                clause += Status.getById(Long.valueOf(id)).getName();
            }
            description += clause;
        }

        return "Subscribe to issues that " + description + ".";
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
