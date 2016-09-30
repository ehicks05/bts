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
import java.lang.management.ManagementFactory;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.ParseException;

@WebServlet(value = "/view", loadOnStartup = 1)
public class Controller extends HttpServlet
{
    private static final int DEBUG_LEVEL = 1;
    private static final boolean DROP_TABLES = true;
    private static final boolean CREATE_TABLES = true;

    @Override
    public void init() throws ServletException
    {
        long controllerStart = System.currentTimeMillis();
        System.out.println("Max Memory: " + new DecimalFormat("#,###").format(Runtime.getRuntime().maxMemory()));

        EOI.init("jdbc:h2:~/bts;TRACE_LEVEL_FILE=1;CACHE_SIZE=131072;");
        SystemInfo.setServletContext(getServletContext());
        SystemInfo.setDebugLevel(DEBUG_LEVEL);

        long subTaskStart = System.currentTimeMillis();
        DBMap.loadDbMaps(getServletContext().getRealPath("/WEB-INF/classes/com/hicks/beans"), "com.hicks.beans");
        System.out.println("Loaded DBMAPS in " + (System.currentTimeMillis() - subTaskStart) + "ms");

        if (DROP_TABLES)
            dropTables();

        if (CREATE_TABLES)
            createTables();

        DefaultDataLoader.createDemoData();

        if (DEBUG_LEVEL > 1)
            for (String argument : ManagementFactory.getRuntimeMXBean().getInputArguments())
                System.out.println(argument);

        System.out.println("Controller.init finished in " + (System.currentTimeMillis() - controllerStart) + " ms");
    }

    private void createTables()
    {
        long subTaskStart;
        int tablesCreated = 0;
        subTaskStart = System.currentTimeMillis();
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
            userSession = createSession(request);

        request.setAttribute("zones", Zone.getAllForUser(userSession));
        request.setAttribute("projects", Project.getAll());
        request.setAttribute("issueTypes", IssueType.getAll());
        request.setAttribute("severities", Severity.getAll());
        request.setAttribute("statuses", Status.getAll());
        request.setAttribute("users", User.getAll());
        request.setAttribute("issueForms", IssueForm.getByUserId(userSession.getUserId()));

        if (request.getParameter("tab1") == null)
        {
            response.sendRedirect("view?tab1=main&tab2=dashboard&action=form");
            return;
        }

        String tab1   = request.getParameter("tab1") == null ? "main" : request.getParameter("tab1");
        String tab2   = request.getParameter("tab2") == null ? "dashboard" : request.getParameter("tab2");
        String action = request.getParameter("action") == null ? "form" : request.getParameter("action");

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
                    logout(request, response);
            }
            if (tab1.equals("admin"))
            {
                if (tab2.equals("overview"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showOverview(request, response);
                }
                if (tab2.equals("users"))
                {
                    if (action.equals("form"))
                        viewJsp = AdminHandler.showManageUsers(request, response);
                }
            }

        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
        }

        if (viewJsp.length() > 0)
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewJsp);
            dispatcher.forward(request, response);
        }

        if (DEBUG_LEVEL > 1)
            System.out.println((System.currentTimeMillis() - start) + " ms for last request " + request.getQueryString());
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

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        request.getSession().invalidate();
        response.sendRedirect("view?tab1=main&tab2=dashboard&action=form");
    }
}
