package net.ehicks.bts;

import net.ehicks.bts.handlers.AuditHandler;
import net.ehicks.eoi.PSIngredients;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class AuditForm extends SearchForm
{
    private String objectKey = "";
    private String fieldName = "";
    private Date fromEventTime;
    private Date toEventTime;
    private String eventType = "";

    public AuditForm()
    {
        setSortColumn("id");
        setSortDirection("asc");
        setPage("1");
    }

    public String getEndpoint()
    {
        return "/view?tab1=admin&tab2=audit&action=ajaxGetPageOfResults";
    }

    public void updateFields(String objectKey, String fieldName, Date fromEventTime, Date toEventTime, String eventType)
    {
        this.objectKey = objectKey;
        this.fieldName = fieldName;
        this.fromEventTime = fromEventTime;
        this.toEventTime = toEventTime;
        this.eventType = eventType;
    }

    public PSIngredients buildSQLQuery(AuditForm auditForm, long resultsPerPage)
    {
        List<Object> args = new ArrayList<>();
        String selectClause = "select * from audits where ";
        String whereClause = "";

        if (auditForm.getObjectKey().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(object_key) like ? ";
            args.add("%" + auditForm.getObjectKey().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (auditForm.getFieldName().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(field_name) like ? ";
            args.add("%" + auditForm.getFieldName().toLowerCase().replaceAll("\\*","%") + "%");
        }

        if (auditForm.getFromEventTime() != null)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " event_time >= ? ";
            args.add(auditForm.getFromEventTime());
        }

        if (auditForm.getToEventTime() != null)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " event_time <= ? ";
            args.add(auditForm.getToEventTime());
        }

        if (auditForm.getEventType().length() > 0)
        {
            if (whereClause.length() > 0) whereClause += " and ";
            whereClause += " lower(event_type) like ? ";
            args.add(auditForm.getEventType());
        }

        if (args.size() == 0) selectClause = selectClause.replace("where", "");

        String orderByClause = "";
        if (auditForm.getSortColumn().length() > 0)
            orderByClause += " order by " + auditForm.getSortColumn() + " " + auditForm.getSortDirection();

        String offset = String.valueOf((Integer.valueOf(auditForm.getPage()) - 1) * resultsPerPage);
        String paginationClause = " limit " + resultsPerPage + " offset " + offset;

        String completeQuery = selectClause + whereClause + orderByClause + paginationClause;
        return new PSIngredients(completeQuery, args);
    }

    public SearchResult getSearchResult() throws IOException, ParseException
    {
        return AuditHandler.performSearch(this);
    }

    // -------- Getters / Setters ----------


    public String getObjectKey()
    {
        return objectKey;
    }

    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
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
