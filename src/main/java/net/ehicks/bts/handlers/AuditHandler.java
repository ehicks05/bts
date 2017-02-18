package net.ehicks.bts.handlers;

import net.ehicks.bts.AuditForm;
import net.ehicks.bts.SearchResult;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.PSIngredients;
import net.ehicks.eoi.SQLGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class AuditHandler
{
    public static String showAuditRecords(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        AuditForm auditForm = (AuditForm) request.getSession().getAttribute("auditForm");
        if (auditForm == null)
        {
            auditForm = new AuditForm();
            auditForm = updateAuditFormFromRequest(auditForm, request);
        }

        SearchResult searchResult = auditForm.getSearchResult();

        request.setAttribute("auditForm", auditForm);
        request.setAttribute("searchResult", searchResult);

        return "/WEB-INF/webroot/admin/audit.jsp";
    }

    public static void ajaxGetPageOfResults(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, ServletException
    {
        AuditForm auditForm = (AuditForm) request.getSession().getAttribute("auditForm");
        if (auditForm == null)
            auditForm = new AuditForm();

        // parse sorting fields
        String sortColumn = request.getParameter("sortColumn");
        String sortDirection = request.getParameter("sortDirection");

        // we must be doing a resort
        if (sortColumn != null && sortDirection != null)
        {
            if (sortColumn.equals(auditForm.getSortColumn()))
            {
                if (sortDirection.equals("asc"))
                    sortDirection = "desc";
                else
                    sortDirection = "asc";
            }

            auditForm.setSortColumn(sortColumn);
            auditForm.setSortDirection(sortDirection);
        }

        String page = request.getParameter("page");
        if (page != null)
            auditForm.setPage(page);

        SearchResult searchResult = auditForm.getSearchResult();

        request.setAttribute("auditForm", auditForm);
        request.setAttribute("searchResult", searchResult);

        request.getSession().setAttribute("auditForm", auditForm);
        request.getSession().setAttribute("searchResult", searchResult);

        request.getRequestDispatcher("/WEB-INF/webroot/auditTable.jsp").forward(request, response);
    }

    public static void search(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        AuditForm auditForm = (AuditForm) request.getSession().getAttribute("auditForm");
        if (auditForm == null)
            auditForm = new AuditForm();

        auditForm = updateAuditFormFromRequest(auditForm, request);

        request.getSession().setAttribute("auditForm", auditForm);
        request.setAttribute("auditForm", auditForm);

        response.sendRedirect("view?tab1=admin&tab2=audit&action=form");
    }

    private static AuditForm updateAuditFormFromRequest(AuditForm auditForm, HttpServletRequest request)
    {
        String objectKey    = Common.getSafeString(request.getParameter("objectKey"));
        String fieldName    = Common.getSafeString(request.getParameter("fieldName"));
        Date fromEventTime  = Common.stringToDate(request.getParameter("fromEventTime"));
        Date toEventTime    = Common.stringToDate(request.getParameter("toEventTime"));
        String eventType    = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("eventType")));

        // parse sorting fields
        String sortColumn = request.getParameter("sortColumn");
        String sortDirection = request.getParameter("sortDirection");
        if (sortColumn == null)
        {
            sortColumn = "event_time";
            sortDirection = "desc";
        }

        if (sortDirection == null)
            sortDirection = "asc";

        String page = request.getParameter("page");
        if (page == null || page.length() == 0)
            page = "1";

        auditForm.updateFields(objectKey, fieldName, fromEventTime, toEventTime, eventType);

        auditForm.setSortColumn(sortColumn);
        auditForm.setSortDirection(sortDirection);
        auditForm.setPage(page);

        return auditForm;
    }

    public static SearchResult performSearch(AuditForm auditForm) throws ParseException, IOException
    {
        long resultsPerPage = 20;

        if (auditForm.getSortColumn().length() == 0) auditForm.setSortColumn("id");
        if (auditForm.getSortDirection().length() == 0) auditForm.setSortDirection("asc");
        if (auditForm.getPage().length() == 0) auditForm.setPage("1");

        PSIngredients auditQuery = auditForm.buildSQLQuery(auditForm, resultsPerPage);
        String countVersionOfQuery = SQLGenerator.getCountVersionOfQuery(auditQuery.query);

        List countResult = EOI.executeQueryOneResult(countVersionOfQuery, auditQuery.args);
        long resultSize = (Long) countResult.get(0);
        List<Object> filteredAudits = EOI.executeQuery(auditQuery.query, auditQuery.args);

        return new SearchResult(auditForm.getPage(), filteredAudits, resultSize, resultsPerPage);
    }
}
