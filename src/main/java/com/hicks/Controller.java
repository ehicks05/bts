package com.hicks;

import com.hicks.beans.*;
import com.hicks.handlers.*;
import net.ehicks.eoi.DBMap;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.SQLGenerator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.ParseException;
import java.util.Properties;

@WebServlet(value = "/view", loadOnStartup = 1)
public class Controller extends HttpServlet
{
    @Override
    public void init() throws ServletException
    {
        loadProperties();

        EOI.init("jdbc:h2:tcp://localhost/~/bts;TRACE_LEVEL_FILE=1;DB_CLOSE_ON_EXIT=FALSE;COMPRESS=TRUE;CACHE_SIZE=" + SystemInfo.INSTANCE.getDatabaseCacheInKBs() + ";");

        loadDBMaps();

        if (SystemInfo.INSTANCE.isDropTables())
            dropTables();

        if (SystemInfo.INSTANCE.isCreateTables())
            createTables();

        if (SystemInfo.INSTANCE.isLoadDemoData())
            DefaultDataLoader.createDemoData();

        System.out.println("Controller.init finished in " + (System.currentTimeMillis() - SystemInfo.INSTANCE.getSystemStart()) + " ms");
    }

    private void loadProperties()
    {
        Properties properties = new Properties();

        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/bts.properties");)
        {
            properties.load(input);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        SystemInfo.INSTANCE.setSystemStart(System.currentTimeMillis());
        SystemInfo.INSTANCE.setServletContext(getServletContext());

        SystemInfo.INSTANCE.setDebugLevel(net.ehicks.common.Common.stringToInt(properties.getProperty("debugLevel")));
        SystemInfo.INSTANCE.setDropTables(properties.getProperty("dropTables").equals("true"));
        SystemInfo.INSTANCE.setCreateTables(properties.getProperty("createTables").equals("true"));
        SystemInfo.INSTANCE.setLoadDemoData(properties.getProperty("loadDemoData").equals("true"));

        SystemInfo.INSTANCE.setDatabaseCacheInKBs(net.ehicks.common.Common.stringToLong(properties.getProperty("databaseCacheInKBs")));

        SystemInfo.INSTANCE.setEmailHost(properties.getProperty("emailHost"));
        SystemInfo.INSTANCE.setEmailPort(net.ehicks.common.Common.stringToInt(properties.getProperty("emailPort")));
        SystemInfo.INSTANCE.setEmailUser(properties.getProperty("emailUser"));
        SystemInfo.INSTANCE.setEmailPassword(properties.getProperty("emailPassword"));
        SystemInfo.INSTANCE.setEmailFromAddress(properties.getProperty("emailFromAddress"));
        SystemInfo.INSTANCE.setEmailFromName(properties.getProperty("emailFromName"));
    }

    private void loadDBMaps()
    {
        long subTaskStart = System.currentTimeMillis();
        DBMap.loadDbMaps(getServletContext().getRealPath("/WEB-INF/classes/com/hicks/beans"), "com.hicks.beans");
        System.out.println("Loaded DBMAPS in " + (System.currentTimeMillis() - subTaskStart) + "ms");
    }

    private void createTables()
    {
        long subTaskStart = System.currentTimeMillis();
        int tablesCreated = 0;
        for (DBMap dbMap : DBMap.dbMaps)
            if (!EOI.isTableExists(dbMap))
            {
                String createTableStatement = SQLGenerator.getCreateTableStatement(dbMap);
                EOI.executeUpdate(createTableStatement);
                tablesCreated++;
            }
        System.out.println("Autocreated " + tablesCreated + " tables in " + (System.currentTimeMillis() - subTaskStart) + "ms");
    }

    private void dropTables()
    {
        long subTaskStart;
        subTaskStart = System.currentTimeMillis();
        for (DBMap dbMap : DBMap.dbMaps)
        {
            try
            {
                EOI.executeUpdate("drop table " + dbMap.tableName);
            }
            catch (Exception e)
            {
                System.out.println("didnt drop " + dbMap.tableName);
            }
        }
        System.out.println("Dropped existing tables in " + (System.currentTimeMillis() - subTaskStart) + "ms");
    }

    @Override
    public void destroy()
    {
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

        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
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

        if (!User.getByUserId(userSession.getUserId()).getEnabled())
        {
            request.setAttribute("clientMessage", "Your account is disabled...");
            String viewJsp = logout(request, response);
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewJsp);
            dispatcher.forward(request, response);
            return;
        }

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-)Control", "private, no-store, no-cache, must-revalidate");

        request.setAttribute("issueTypes", IssueType.getAll());
        request.setAttribute("severities", Severity.getAll());
        request.setAttribute("statuses", Status.getAll());
        request.setAttribute("groups", Group.getAll());

        // the following collections have restricted access
        request.setAttribute("zones", Zone.getAllForUser(userSession.getUserId()));
        request.setAttribute("projects", Project.getAllForUser(userSession.getUserId()));
        request.setAttribute("users", User.getAllForUser(userSession.getUserId()));
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
            System.out.println((System.currentTimeMillis() - start) + " ms for last request " + request.getQueryString());
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

                if (action.equals("debug"))
                    DebugHandler.getDebugInfo(request, response);
                if (action.equals("logout"))
                    viewJsp = logout(request, response);
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
                if (tab2.equals("users"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showManageUsers(request, response);
                    if (action.equals("create"))
                        AdminHandler.createUser(request, response);
                    if (action.equals("delete"))
                        AdminHandler.deleteUser(request, response);

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
                if (tab2.equals("zones"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showManageZones(request, response);
                    if (action.equals("create"))
                        AdminHandler.createZone(request, response);
                    if (action.equals("delete"))
                        AdminHandler.deleteZone(request, response);

                    if (tab3.equals("modify"))
                    {
                        if (action.equals("form"))
                            viewJsp = AdminHandler.showModifyZone(request, response);
                        if (action.equals("modify"))
                            AdminHandler.modifyZone(request, response);
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
            }

        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
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
        request.getSession().setAttribute("userSession", userSession);

        return userSession;
    }

    private String logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        request.getSession().invalidate();
        return "/WEB-INF/webroot/logged-out.jsp";
    }
}
