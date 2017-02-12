package net.ehicks.bts;

import net.ehicks.bts.beans.*;
import net.ehicks.bts.handlers.*;
import net.ehicks.eoi.EOI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Date;

@WebServlet(value = "/view", loadOnStartup = 1)
public class Controller extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @Override
    public void init() throws ServletException
    {
        log.info("BTS starting...");
        Startup.loadProperties(getServletContext());
        String h2Settings = "TRACE_LEVEL_FILE=1;DB_CLOSE_ON_EXIT=FALSE;COMPRESS=TRUE;CACHE_SIZE=" + SystemInfo.INSTANCE.getDatabaseCacheInKBs() + ";";
        String h2ConnectionString = "jdbc:h2:tcp://localhost/~/bts/bts;" + h2Settings;
        String h2MemConnectionString = "jdbc:h2:mem:" + h2Settings;
        String mssqlConnectionString = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;user=erictest;***REMOVED***";
        SystemInfo.INSTANCE.setDbConnectionString(h2ConnectionString);

        EOI.init(SystemInfo.INSTANCE.getDbConnectionString());

        Startup.loadDBMaps(getServletContext());

        if (SystemInfo.INSTANCE.isDropCreateLoad())
        {
            Startup.dropTables();
            Startup.createTables();
            new Thread(DefaultDataLoader::createDemoData).start();
        }

        log.info("BTS loaded in {} ms", (System.currentTimeMillis() - SystemInfo.INSTANCE.getSystemStart()));
    }

    @Override
    public void destroy()
    {
        log.info("BTS shutting down...");
        EmailThreadPool.shutDown();
        EOI.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        long start = System.currentTimeMillis();

        UserSession userSession = (UserSession) request.getSession(false).getAttribute("userSession");
        if (userSession == null)
        {
            userSession = createSession(request);

            // this will hit if they 1. log out, 2. hit F5, and 3. attempt to log back in.
            // without this check, they will log in and immediately be logged out again.
            if (request.getParameter("action").equals("logout"))
            {
                response.sendRedirect("view?tab1=main&tab2=dashboard&action=form");
                return;
            }
        }
        userSession.setLastActivity(new Date());

        if (!User.getByUserId(userSession.getUserId()).getEnabled())
        {
            request.setAttribute("clientMessage", "Your account is disabled...");
            logout(request, response);
            response.sendRedirect("view?tab1=main&tab2=dashboard&action=form");
            return;
        }

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-)Control", "private, no-store, no-cache, must-revalidate");

        request.setAttribute("issueTypes", IssueType.getAll());
        request.setAttribute("severities", Severity.getAll());
        request.setAttribute("statuses", Status.getAll());

        // the following collections have restricted access
        request.setAttribute("projects", Project.getAllForUser(userSession.getUserId()));
        request.setAttribute("users", User.getAllForUser(userSession.getUserId()));
        request.setAttribute("groups", Group.getAllForUser(userSession.getUserId()));
        request.setAttribute("issueForms", IssueForm.getByUserId(userSession.getUserId()));

        if (request.getParameter("tab1") == null)
        {
            response.sendRedirect("view?tab1=main&tab2=dashboard&action=form");
            return;
        }

        String viewJsp = processRequest(request, response, userSession);

        if (viewJsp.length() > 0)
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewJsp);
            dispatcher.forward(request, response);
        }

        if (SystemInfo.INSTANCE.getDebugLevel() > 1)
            log.debug("{} ms for last request {}", (System.currentTimeMillis() - start), request.getQueryString());
    }

    private String processRequest(HttpServletRequest request, HttpServletResponse response, UserSession userSession) throws IOException, ServletException
    {
        String tab1   = request.getParameter("tab1") == null ? "main" : request.getParameter("tab1");
        String tab2   = request.getParameter("tab2") == null && tab1.equals("main") ? "dashboard" : request.getParameter("tab2");
        String tab3   = request.getParameter("tab3") == null ? "" : request.getParameter("tab3");
        String action = request.getParameter("action") == null ? "form" : request.getParameter("action");

        // security
        if (tab1.equals("admin") && !userSession.getUser().isAdmin())
            return "/WEB-INF/webroot/error.jsp";

        String viewJsp = "";
        try
        {
            if (tab1.equals("main"))
            {
                if (tab2.equals("search"))
                {
                    if (action.equals("form"))
                        viewJsp = IssueSearchHandler.showIssues(request, response);
                    if (action.equals("search"))
                        IssueSearchHandler.search(request, response);
                    if (action.equals("ajaxGetPageOfResults"))
                        IssueSearchHandler.ajaxGetPageOfResults(request, response);
                    if (action.equals("saveIssueForm"))
                        IssueSearchHandler.saveIssueForm(request, response);
                }

                if (tab2.equals("issue"))
                {
                    if (action.equals("form"))
                        viewJsp = ModifyIssueHandler.showModifyIssue(request, response);
                    if (action.equals("create"))
                        ModifyIssueHandler.createIssue(request, response);
                    if (action.equals("update"))
                        ModifyIssueHandler.updateIssue(request, response);
                    if (action.equals("addComment"))
                        ModifyIssueHandler.addComment(request, response);
                    if (action.equals("updateComment"))
                        ModifyIssueHandler.updateComment(request, response);
                    if (action.equals("addWatcher"))
                        ModifyIssueHandler.addWatcher(request, response);
                    if (action.equals("removeWatcher"))
                        ModifyIssueHandler.removeWatcher(request, response);
                    if (action.equals("addAttachment"))
                        AttachmentHandler.addAttachment(request, response);
                    if (action.equals("retrieveAttachment"))
                        AttachmentHandler.retrieveAttachment(request, response);
                    if (action.equals("deleteAttachment"))
                        AttachmentHandler.deleteAttachment(request, response);
                }
                if (tab2.equals("issueForm"))
                {
                    if (action.equals("form"))
                        viewJsp = IssueFormHandler.showIssueForms(request, response);
                    if (action.equals("delete"))
                        IssueFormHandler.deleteIssueForm(request, response);
                    if (action.equals("addToDashboard"))
                        IssueFormHandler.addToDashboard(request, response);
                }
                if (tab2.equals("settings"))
                {
                    if (action.equals("form"))
                        viewJsp = SettingsHandler.showSettings(request, response);
                }

                if (tab2.equals("dashboard"))
                {
                    if (action.equals("form"))
                        viewJsp = DashboardHandler.showDashboard(request, response);
                    if (action.equals("remove"))
                        DashboardHandler.removeIssueForm(request, response);
                }

                if (tab2.equals("profile"))
                {
                    if (action.equals("form"))
                        viewJsp = ProfileHandler.showModifyUser(request, response);
                    if (action.equals("create"))
                        ProfileHandler.createUser(request, response);
                }
                if (tab2.equals("subscriptions"))
                {
                    if (tab3.equals("list"))
                    {
                        if (action.equals("form"))
                            viewJsp = SubscriptionHandler.showSubscriptions(request, response);
                        if (action.equals("add"))
                            SubscriptionHandler.addSubscription(request, response);
                        if (action.equals("delete"))
                            SubscriptionHandler.deleteSubscription(request, response);
                        if (action.equals("print"))
                            SubscriptionHandler.printSubscriptions(request, response);
                    }
                }

                if (action.equals("debug"))
                    DebugHandler.getDebugInfo(request, response);
                if (action.equals("logout"))
                {
                    logout(request, response);
                    return "";
                }
            }
            if (tab1.equals("admin"))
            {
                if (tab2 == null || tab2.isEmpty())
                {
                    response.sendRedirect("view?tab1=admin&tab2=overview&action=form");
                    return "";
                }
                if (tab2.equals("overview"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showOverview(request, response);
                }
                if (tab2.equals("cache"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showCacheInfo(request, response);
                    if (action.equals("clearCache"))
                        AdminHandler.clearCache(request, response);
                }
                if (tab2.equals("system"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showSystemInfo(request, response);
                }

                if (tab2.equals("audit"))
                {
                    if (action.equals("form"))
                        viewJsp = AuditHandler.showAuditRecords(request, response);
                    if (action.equals("search"))
                        AuditHandler.search(request, response);
                    if (action.equals("ajaxGetPageOfResults"))
                        AuditHandler.ajaxGetPageOfResults(request, response);
                }
                if (tab2.equals("users"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showManageUsers(request, response);
                    if (action.equals("create"))
                        AdminHandler.createUser(request, response);
                    if (action.equals("delete"))
                        AdminHandler.deleteUser(request, response);
                    if (action.equals("print"))
                        AdminHandler.printUsers(request, response);

                    if (tab3.equals("modify"))
                    {
                        if (action.equals("form"))
                            viewJsp = AdminHandler.showModifyUser(request, response);
                        if (action.equals("modify"))
                            AdminHandler.modifyUser(request, response);
                    }
                }
                if (tab2.equals("projects"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showManageProjects(request, response);
                    if (action.equals("create"))
                        AdminHandler.createProject(request, response);
                    if (action.equals("delete"))
                        AdminHandler.deleteProject(request, response);

                    if (tab3.equals("modify"))
                    {
                        if (action.equals("form"))
                            viewJsp = AdminHandler.showModifyProject(request, response);
                        if (action.equals("modify"))
                            AdminHandler.modifyProject(request, response);
                    }
                }
                if (tab2.equals("groups"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showManageGroups(request, response);
                    if (action.equals("create"))
                        AdminHandler.createGroup(request, response);
                    if (action.equals("delete"))
                        AdminHandler.deleteGroup(request, response);

                    if (tab3.equals("modify"))
                    {
                        if (action.equals("form"))
                            viewJsp = AdminHandler.showModifyGroup(request, response);
                        if (action.equals("modify"))
                            AdminHandler.modifyGroup(request, response);
                    }
                }
                if (tab2.equals("email"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showManageEmails(request, response);
                    if (action.equals("sendTest"))
                        AdminHandler.sendTestEmail(request, response);
                    if (action.equals("delete"))
                        AdminHandler.deleteEmail(request, response);

                    if (tab3.equals("modify"))
                    {
                        if (action.equals("form"))
                            viewJsp = AdminHandler.showModifyEmail(request, response);
                        if (action.equals("modify"))
                            AdminHandler.modifyEmail(request, response);
                    }
                }
                if (tab2.equals("logs"))
                {
                    if (action.equals("form"))
                        viewJsp = LogHandler.showLogs(request, response);
                    if (action.equals("viewLog"))
                        LogHandler.viewLog(request, response);
                    if (action.equals("viewLogPretty"))
                        viewJsp = LogHandler.viewLogPretty(request, response);
                    if (action.equals("delete"))
                        LogHandler.deleteLog(request, response);
                }
            }

        }
        catch (ParseException e)
        {
            log.error(e.getMessage(), e);
        }
        return viewJsp;
    }

    private UserSession createSession(HttpServletRequest request)
    {
        Principal principal = request.getUserPrincipal();
        User user = User.getByLogonId(principal.getName());

        UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setLogonId(user.getLogonId());
        userSession.setSessionId(request.getSession().getId());
        userSession.setLastActivity(new Date());
        request.getSession().setAttribute("userSession", userSession);

        return userSession;
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        request.removeAttribute("userSession");
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();

        response.sendRedirect("view?tab1=main&tab2=dashboard&action=form");
    }
}
