package net.ehicks.bts.model;

import java.util.Collections;
import java.util.Date;

public class AuditForm<T> extends SearchForm
{
    private Long issueId = 0L;
    private String fieldName = "";
    private Date fromEventTime;
    private Date toEventTime;
    private String eventType = "";
    private SearchResult<T> searchResult = new SearchResult<>(Collections.emptyList(), 0, 20, "1");

    public AuditForm()
    {
        setSortColumn("id");
        setSortDirection("desc");
        setPage("1");
    }

    public String getEndpoint()
    {
        return "/admin/audit/ajaxGetPageOfResults";
    }

    public void updateFields(Long issueId, String fieldName, Date fromEventTime, Date toEventTime, String eventType)
    {
        this.issueId = issueId;
        this.fieldName = fieldName;
        this.fromEventTime = fromEventTime;
        this.toEventTime = toEventTime;
        this.eventType = eventType;
    }

    public SearchResult getSearchResult()
    {
        return searchResult;
    }

    public void setSearchResult(SearchResult<T> searchResult)
    {
        this.searchResult = searchResult;
    }

    // -------- Getters / Setters ----------

    public Long getIssueId()
    {
        return issueId;
    }

    public void setIssueId(Long issueId)
    {
        this.issueId = issueId;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public Date getFromEventTime()
    {
        return fromEventTime;
    }

    public void setFromEventTime(Date fromEventTime)
    {
        this.fromEventTime = fromEventTime;
    }

    public Date getToEventTime()
    {
        return toEventTime;
    }

    public void setToEventTime(Date toEventTime)
    {
        this.toEventTime = toEventTime;
    }

    public String getEventType()
    {
        return eventType;
    }

    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }
}
