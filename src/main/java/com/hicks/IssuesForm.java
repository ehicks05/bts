package com.hicks;

import java.util.Date;

public class IssuesForm
{
    private String id = "";

    private String title = "";
    private String description = "";
    private String severity = "";
    private String status = "";

    private Long bucketId;
    private Long zoneId;
    private Long issueTypeId;

    private Long createdByUserId;
    private Date createdOn;

    private String sortColumn;
    private String sortDirection;
    private String page;

    public IssuesForm(String id, String title, String description, String severity, String status, Long bucketId, Long zoneId)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.bucketId = bucketId;
        this.zoneId = zoneId;
    }

    public String toString()
    {
        return
        "\r\nIssuesForm: " +
        "\r\nid: " + id +
        "\r\ntitle: " + title +
        "\r\n";
    }

    // Getter / Setter

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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

    public String getSeverity()
    {
        return severity;
    }

    public void setSeverity(String severity)
    {
        this.severity = severity;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Long getBucketId()
    {
        return bucketId;
    }

    public void setBucketId(Long bucketId)
    {
        this.bucketId = bucketId;
    }

    public Long getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(Long zoneId)
    {
        this.zoneId = zoneId;
    }

    public Long getIssueTypeId()
    {
        return issueTypeId;
    }

    public void setIssueTypeId(Long issueTypeId)
    {
        this.issueTypeId = issueTypeId;
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
