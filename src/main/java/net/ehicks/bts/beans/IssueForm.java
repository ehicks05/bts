//package net.ehicks.bts.beans;
//
//import net.ehicks.bts.SearchForm;
//import net.ehicks.bts.handlers.IssueSearchHandler;
//import net.ehicks.bts.SearchResult;
//import net.ehicks.eoi.EOI;
//import net.ehicks.eoi.PSIngredients;
//import net.ehicks.eoi.SQLGenerator;
//
//import javax.persistence.*;
//import java.io.IOException;
//import java.io.Serializable;
//import java.text.ParseException;
//import java.util.*;
//
//@Entity
//@Table(name = "issue_forms")
//public class IssueForm extends SearchForm implements Serializable
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(length = 2000)
//    private String formName = "";
//    @Column(length = 2000)
//    private String containsText = "";
//    @Column(length = 2000)
//    private String title = "";
//    @Column(columnDefinition = "varchar2(32000 CHAR)")
//    private String description = "";
//
//    private Long userId;
//    private Boolean onDash;
//    private Long issueId;
//    private String groupIds;
//    private String issueTypeIds;
//    private String projectIds;
//    private String assigneeUserIds;
//    private String reporterUserIds;
//    private String severityIds;
//    private String statusIds;
//
//    @Column(name = "created_by_user_id")
//    private Long createdByUserId;
//
//    @Column(name = "created_on")
//    @Temporal(TemporalType.DATE)
//    private Date createdOn;
//
//    @Column(name = "last_updated_by_user_id")
//    private Long lastUpdatedByUserId;
//
//    @Column(name = "last_updated_on")
//    @Temporal(TemporalType.DATE)
//    private Date lastUpdatedOn;
//
//    @Column(name = "sort_column", length = 2000)
//    private String sortColumn = "";
//
//    @Column(name = "sort_direction", length = 2000)
//    private String sortDirection = "";
//
//    @Column(name = "page", length = 2000)
//    private String page = "";
//
//    public IssueForm()
//    {
//        this.sortColumn = "id";
//        this.sortDirection = "asc";
//        this.page = "1";
//    }
//
//    public String getEndpoint()
//    {
//        return "/search/ajaxGetPageOfResults";
//    }
//
//    public void updateFields(String filterName, Long userId, String containsText, String title, String description, String statusIds,
//                     String severityIds, String projectIds, String groupIds, String assigneeUserIds, Date createdOn, Date lastUpdatedOn, boolean onDash)
//    {
//        this.setFormName(filterName);
//        this.userId = userId;
//        this.containsText = containsText;
//        this.title = title;
//        this.description = description;
//        this.statusIds = statusIds;
//        this.severityIds = severityIds;
//        this.projectIds = projectIds;
//        this.groupIds = groupIds;
//        this.assigneeUserIds = assigneeUserIds;
//        this.createdOn = createdOn;
//        this.lastUpdatedOn = lastUpdatedOn;
//        this.onDash = onDash;
//    }
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof IssueForm)) return false;
//        IssueForm that = (IssueForm) obj;
//
//        if (this.id == null || that.id == null)
//            return false;
//
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
//        String thisId = id == null ? "" : id.toString();
//        return this.getClass().getSimpleName() + ":" + thisId;
//    }
//
//    // --------
//
//    public static List<IssueForm> getAll()
//    {
//        return EOI.executeQuery("select * from issue_forms");
//    }
//
//    public static IssueForm getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from issue_forms where id=?", Arrays.asList(id));
//    }
//
//    public static List<IssueForm> getByUserId(Long userId)
//    {
//        return EOI.executeQuery("select * from issue_forms where user_id=?", Arrays.asList(userId));
//    }
//
//    public SearchResult getSearchResult() throws IOException, ParseException
//    {
//        return IssueSearchHandler.performSearch(this);
//    }
//
//    public List<String> getProjectIdsAsList()
//    {
//        if (projectIds == null)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(projectIds.split(",")));
//    }
//
//    public List<String> getGroupIdsAsList()
//    {
//        if (groupIds == null)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(groupIds.split(",")));
//    }
//
//    public List<String> getSeverityIdsAsList()
//    {
//        if (severityIds == null)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(severityIds.split(",")));
//    }
//
//    public List<String> getStatusIdsAsList()
//    {
//        if (statusIds == null)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(statusIds.split(",")));
//    }
//
//    public List<String> getAssigneeUserIdsAsList()
//    {
//        if (assigneeUserIds == null)
//            return Collections.EMPTY_LIST;
//        return new ArrayList<>(Arrays.asList(assigneeUserIds.split(",")));
//    }
//
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
//    public Long getUserId()
//    {
//        return userId;
//    }
//
//    public void setUserId(Long userId)
//    {
//        this.userId = userId;
//    }
//
//    public String getFormName()
//    {
//        return formName;
//    }
//
//    public void setFormName(String formName)
//    {
//        this.formName = formName;
//    }
//
//    public Boolean getOnDash()
//    {
//        return onDash;
//    }
//
//    public void setOnDash(Boolean onDash)
//    {
//        this.onDash = onDash;
//    }
//
//    public String getContainsText()
//    {
//        return containsText;
//    }
//
//    public void setContainsText(String containsText)
//    {
//        this.containsText = containsText;
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
//    public String getGroupIds()
//    {
//        return groupIds;
//    }
//
//    public void setGroupIds(String groupIds)
//    {
//        this.groupIds = groupIds;
//    }
//
//    public String getIssueTypeIds()
//    {
//        return issueTypeIds;
//    }
//
//    public void setIssueTypeIds(String issueTypeIds)
//    {
//        this.issueTypeIds = issueTypeIds;
//    }
//
//    public String getProjectIds()
//    {
//        return projectIds;
//    }
//
//    public void setProjectIds(String projectIds)
//    {
//        this.projectIds = projectIds;
//    }
//
//    public String getAssigneeUserIds()
//    {
//        return assigneeUserIds;
//    }
//
//    public void setAssigneeUserIds(String assigneeUserIds)
//    {
//        this.assigneeUserIds = assigneeUserIds;
//    }
//
//    public String getReporterUserIds()
//    {
//        return reporterUserIds;
//    }
//
//    public void setReporterUserIds(String reporterUserIds)
//    {
//        this.reporterUserIds = reporterUserIds;
//    }
//
//    public String getSeverityIds()
//    {
//        return severityIds;
//    }
//
//    public void setSeverityIds(String severityIds)
//    {
//        this.severityIds = severityIds;
//    }
//
//    public String getStatusIds()
//    {
//        return statusIds;
//    }
//
//    public void setStatusIds(String statusIds)
//    {
//        this.statusIds = statusIds;
//    }
//
//    public Long getCreatedByUserId()
//    {
//        return createdByUserId;
//    }
//
//    public void setCreatedByUserId(Long createdByUserId)
//    {
//        this.createdByUserId = createdByUserId;
//    }
//
//    public Date getCreatedOn()
//    {
//        return createdOn;
//    }
//
//    public void setCreatedOn(Date createdOn)
//    {
//        this.createdOn = createdOn;
//    }
//
//    public Long getLastUpdatedByUserId()
//    {
//        return lastUpdatedByUserId;
//    }
//
//    public void setLastUpdatedByUserId(Long lastUpdatedByUserId)
//    {
//        this.lastUpdatedByUserId = lastUpdatedByUserId;
//    }
//
//    public Date getLastUpdatedOn()
//    {
//        return lastUpdatedOn;
//    }
//
//    public void setLastUpdatedOn(Date lastUpdatedOn)
//    {
//        this.lastUpdatedOn = lastUpdatedOn;
//    }
//
//    public String getSortColumn()
//    {
//        return sortColumn;
//    }
//
//    public void setSortColumn(String sortColumn)
//    {
//        this.sortColumn = sortColumn;
//    }
//
//    public String getSortDirection()
//    {
//        return sortDirection;
//    }
//
//    public void setSortDirection(String sortDirection)
//    {
//        this.sortDirection = sortDirection;
//    }
//
//    public String getPage()
//    {
//        return page;
//    }
//
//    public void setPage(String page)
//    {
//        this.page = page;
//    }
//}
