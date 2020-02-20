package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.AuditForm;
import net.ehicks.bts.SearchResult;
import net.ehicks.bts.beans.Issue;
import net.ehicks.bts.beans.IssueRepository;
import net.ehicks.bts.model.AuditQueryLogic;
import net.ehicks.common.Common;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class AuditHandler
{
    IssueRepository issueRepository;
    AuditQueryLogic auditQueryLogic;

    public AuditHandler(IssueRepository issueRepository, AuditQueryLogic auditQueryLogic)
    {
        this.issueRepository = issueRepository;
        this.auditQueryLogic = auditQueryLogic;
    }

    @GetMapping("/admin/audit/form")
    public ModelAndView showAuditRecords()
    {
        AuditForm<Revision<Integer, Issue>> auditForm = new AuditForm<>();
        auditForm.setIssueId(138L);
        SearchResult<Revision<Integer, Issue>> searchResult = auditQueryLogic.query(auditForm);

        auditForm.setSearchResult(searchResult);
        return new ModelAndView("admin/audit")
                .addObject("auditForm", auditForm)
                .addObject("searchResult", searchResult);
    }

    @GetMapping("/admin/audit/search")
    public ModelAndView search()
    {
        return new ModelAndView("redirect:/admin/audit/form")
//                .addObject("auditForm", auditForm)
                ;
    }

    @GetMapping("/admin/audit/ajaxGetPageOfResults")
    public ModelAndView ajaxGetPageOfResults()
    {
//        AuditForm auditForm = (AuditForm) request.getSession().getAttribute("auditForm");
//        if (auditForm == null)
//            auditForm = new AuditForm();
//
//        // parse sorting fields
//        String sortColumn = request.getParameter("sortColumn");
//        String sortDirection = request.getParameter("sortDirection");
//
//        // we must be doing a resort
//        if (sortColumn != null && sortDirection != null)
//        {
//            if (sortColumn.equals(auditForm.getSortColumn()))
//            {
//                if (sortDirection.equals("asc"))
//                    sortDirection = "desc";
//                else
//                    sortDirection = "asc";
//            }
//
//            auditForm.setSortColumn(sortColumn);
//            auditForm.setSortDirection(sortDirection);
//        }
//
//        String page = request.getParameter("page");
//        if (page != null)
//            auditForm.setPage(page);
//
//        SearchResult searchResult = auditForm.getSearchResult();
//
//        request.setAttribute("auditForm", auditForm);
//        request.setAttribute("searchResult", searchResult);
//
//        request.getSession().setAttribute("auditForm", auditForm);
//        request.getSession().setAttribute("searchResult", searchResult);
//
        return new ModelAndView("auditTable");
    }

    private AuditForm updateAuditFormFromRequest(AuditForm auditForm, HttpServletRequest request)
    {
        Long issueId    = Long.parseLong(request.getParameter("issueId"));
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

        auditForm.updateFields(issueId, fieldName, fromEventTime, toEventTime, eventType);

        auditForm.setSortColumn(sortColumn);
        auditForm.setSortDirection(sortDirection);
        auditForm.setPage(page);

        return auditForm;
    }

    public SearchResult performSearch(AuditForm auditForm)
    {
        long resultsPerPage = 20;

        if (auditForm.getSortColumn().length() == 0) auditForm.setSortColumn("id");
        if (auditForm.getSortDirection().length() == 0) auditForm.setSortDirection("asc");
        if (auditForm.getPage().length() == 0) auditForm.setPage("1");

//        PSIngredients auditQuery = auditForm.buildSQLQuery(auditForm, resultsPerPage);
//        String countVersionOfQuery = SQLGenerator.getCountVersionOfQuery(auditQuery.query);
//
//        List countResult = EOI.executeQueryOneResult(countVersionOfQuery, auditQuery.args);
//        long resultSize = (Long) countResult.get(0);
//        List<Object> filteredAudits = EOI.executeQuery(auditQuery.query, auditQuery.args);
//
//        return new SearchResult(auditForm.getPage(), filteredAudits, resultSize, resultsPerPage);
        return null;
    }
}
