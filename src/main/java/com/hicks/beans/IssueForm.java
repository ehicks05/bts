package com.hicks.beans;


import com.hicks.handlers.IssueSearchHandler;
import com.hicks.SearchResult;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.PSIngredients;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

@Entity
@Table(name = "issue_forms")
public class IssueForm implements Serializable
{
//    @Version
//    @Column(name = "version")
//    private Long version;

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "contains_text", length = 2000)
    private String containsText = "";

    @Column(name = "form_name", length = 2000)
    private String formName = "";

    @Column(name = "title", length = 2000)
    private String title = "";
    @Column(name = "description", columnDefinition = "varchar2(32000 CHAR)")
    private String description = "";

    @Column(name = "issue_id")
    private Long issueId;

    @Column(name = "zone_ids")
    private String zoneIds;
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

    @Column(name = "sort_column", length = 2000)
    private String sortColumn = "";

    @Column(name = "sort_direction", length = 2000)
    private String sortDirection = "";

    @Column(name = "page", length = 2000)
    private String page = "";

    public IssueForm()
    {
        this.sortColumn = "id";
        this.sortDirection = "asc";
        this.page = "1";
    }

    public IssueForm(Long issueId, Long userId, String containsText, String title, String description, String statusIds,
                     String severityIds, String zoneIds, String assigneeUserIds, Date createdOn, Date lastUpdatedOn)
    {
        this();
        this.issueId = issueId;
        this.userId = userId;
        this.containsText = containsText;
        this.title = title;
        this.description = description;
        this.statusIds = statusIds;
        this.severityIds = severityIds;
        this.zoneIds = zoneIds;
        this.assigneeUserIds = assigneeUserIds;
        this.createdOn = createdOn;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public static PSIngredients buildFilmSQLQuery(IssueForm issueForm, long resultsPerPage)
    {
        List<Object> args = new ArrayList<>();
        String selectClause = "select * from issues where ";
        String whereClause = "";

        if (issueForm.getIssueId() != null && issueForm.getIssueId() != 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(id) like ? ";
            args.add(issueForm.getIssueId());
        }

        if (issueForm.getContainsText().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += "( ";
            whereClause += " lower(id) like ? ";
            whereClause += " or lower(title) like ? ";
            whereClause += " or lower(description) like ? ";
            whereClause += ") ";
            args.add("%" + issueForm.getContainsText().toLowerCase().replaceAll("\\*","%") + "%");
            args.add("%" + issueForm.getContainsText().toLowerCase().replaceAll("\\*","%") + "%");
            args.add("%" + issueForm.getContainsText().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (issueForm.getTitle().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(title) like ? ";
            args.add("%" + issueForm.getTitle().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (issueForm.getDescription().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(description) like ? ";
            args.add("%" + issueForm.getDescription().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (issueForm.getZoneIds() != null && issueForm.getZoneIds().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            String questionMarks = "";
            for (String id : issueForm.getZoneIds().split(","))
            {
                if (questionMarks.length() > 0)
                    questionMarks += ",";
                questionMarks += "?";
                args.add(id);
            }
            whereClause += " zone_id in (" + questionMarks +") ";
        }

        if (issueForm.getSeverityIds() != null && issueForm.getSeverityIds().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            String questionMarks = "";
            for (String id : issueForm.getSeverityIds().split(","))
            {
                if (questionMarks.length() > 0)
                    questionMarks += ",";
                questionMarks += "?";
                args.add(id);
            }
            whereClause += " severity_id in (" + questionMarks + ") ";
        }

        if (issueForm.getAssigneeUserIds() != null && issueForm.getAssigneeUserIds().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            String questionMarks = "";
            for (String id : issueForm.getAssigneeUserIds().split(","))
            {
                if (questionMarks.length() > 0)
                    questionMarks += ",";
                questionMarks += "?";
                args.add(id);
            }
            whereClause += " assignee_user_id in (" + questionMarks + ") ";
        }

        if (args.size() == 0) selectClause = selectClause.replace("where", "");

        String orderByClause = "";
        if (issueForm.getSortColumn().length() > 0)
        {
            orderByClause += " order by " + issueForm.getSortColumn() + " " + issueForm.getSortDirection() + ", id nulls last " ;
        }

        String offset = String.valueOf((Integer.valueOf(issueForm.getPage()) - 1) * resultsPerPage);
        String paginationClause = " limit " + resultsPerPage + " offset " + offset;

        String completeQuery = selectClause + whereClause + orderByClause + paginationClause;
        return new PSIngredients(completeQuery, args);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof IssueForm)) return false;
        IssueForm that = (IssueForm) obj;
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

    public static List<IssueForm> getAll()
    {
        return EOI.executeQuery("select * from issue_forms");
    }

    public static IssueForm getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from issue_forms where id=?", Arrays.asList(id));
    }

    public static List<IssueForm> getByUserId(Long userId)
    {
        return EOI.executeQuery("select * from issue_forms where user_id=?", Arrays.asList(userId));
    }

    public SearchResult getSearchResult() throws IOException, ParseException
    {
        return IssueSearchHandler.performSearch(this);
    }

    public List<String> getZoneIdsAsList()
    {
        if (zoneIds == null)
            return Collections.EMPTY_LIST;
        return new ArrayList<>(Arrays.asList(zoneIds.split(",")));
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

    public String getContainsText()
    {
        return containsText;
    }

    public void setContainsText(String containsText)
    {
        this.containsText = containsText;
    }

    public String getFormName()
    {
        return formName;
    }

    public void setFormName(String formName)
    {
        this.formName = formName;
    }

    public Long getIssueId()
    {
        return issueId;
    }

    public void setIssueId(Long issueId)
    {
        this.issueId = issueId;
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

    public String getZoneIds()
    {
        return zoneIds;
    }

    public void setZoneIds(String zoneIds)
    {
        this.zoneIds = zoneIds;
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

    public String getSortColumn()
    {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn)
    {
        this.sortColumn = sortColumn;
    }

    public String getSortDirection()
    {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection)
    {
        this.sortDirection = sortDirection;
    }

    public String getPage()
    {
        return page;
    }

    public void setPage(String page)
    {
        this.page = page;
    }
}
