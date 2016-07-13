package com.hicks;

import com.hicks.beans.Issue;
import com.hicks.beans.User;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(value = "/view", loadOnStartup = 1)
public class Controller extends HttpServlet
{
    private static final boolean DEBUG = false;
    private static final boolean DROP_TABLES = false;
    private static final boolean CREATE_TABLES = true;

    @Override
    public void init() throws ServletException
    {
        long controllerStart = System.currentTimeMillis();
        System.out.println("Max Memory: " + new DecimalFormat("#,###").format(Runtime.getRuntime().maxMemory()));

        EOI.init("jdbc:h2:~/bts;TRACE_LEVEL_FILE=1;CACHE_SIZE=131072;");
        SystemInfo.setServletContext(getServletContext());

        long subTaskStart = System.currentTimeMillis();
        DBMap.loadDbMaps(getServletContext().getRealPath("/WEB-INF/classes/com/hicks/beans"));
        System.out.println("Loaded DBMAPS in " + (System.currentTimeMillis() - subTaskStart) + "ms");

        if (DROP_TABLES)
            dropTables();

        if (CREATE_TABLES)
            createTables();

//        User user = new User();
//        user.setLogonId("eric");
//        user.setPassword("eric");
//        EOI.insert(user);
//
//        Role role = new Role();
//        role.setLogonId("eric");
//        role.setRoleName("user");
//        EOI.insert(role);
//
//        role = new Role();
//        role.setLogonId("eric");
//        role.setRoleName("admin");
//        EOI.insert(role);

        List result = EOI.executeQueryOneResult("select count(*) from issues", new ArrayList<>());
        long rows = (Long) result.get(0);
        if (rows == 0)
        {
            Issue issue = new Issue();
            issue.setTitle("Thing is Broken");
            issue.setDescription("Please fix thing.");
            issue.setCreatedOn(new Date());
            EOI.insert(issue);

            issue = new Issue();
            issue.setTitle("We Would Like This New Thing");
            issue.setDescription("Can you add it?");
            issue.setCreatedOn(new Date());
            EOI.insert(issue);
        }

        if (DEBUG)
            for (String argument : ManagementFactory.getRuntimeMXBean().getInputArguments())
                System.out.println(argument);

        System.out.println("Controller.init finished in " + (System.currentTimeMillis() - controllerStart) + " ms");
    }

    private void createTables()
    {
        long subTaskStart;
        subTaskStart = System.currentTimeMillis();
        for (DBMap dbMap : DBMap.dbMaps)
            if (!EOI.isTableExists(dbMap))
            {
                System.out.println("Creating table " + dbMap.tableName + "...");
                String createTableStatement = SQLGenerator.getCreateTableStatement(dbMap);
                System.out.println(createTableStatement);
                EOI.executeUpdate(createTableStatement);
            }
        System.out.println("Made sure all tables exist (creating if necessary) in " + (System.currentTimeMillis() - subTaskStart) + "ms");
    }

    private void dropTables()
    {
        long subTaskStart;
        subTaskStart = System.currentTimeMillis();
        for (DBMap dbMap : DBMap.dbMaps)
        {
            System.out.print("Dropping " + dbMap.tableName + "...");
            System.out.println(EOI.executeUpdate("drop table " + dbMap.tableName));
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
            Principal principal = request.getUserPrincipal();
            User user = User.getByLogonId(principal.getName());
            userSession = new UserSession();
            userSession.setLogonId(user.getLogonId());
            request.getSession().setAttribute("userSession", userSession);
        }


        String tab1   = request.getParameter("tab1") == null ? "home" : request.getParameter("tab1");
        String action = request.getParameter("action") == null ? "form" : request.getParameter("action");

        String viewJsp = "";
        try
        {
            if (action.equals("form"))
                viewJsp = IssuesHandler.showIssues(request, response);
            if (action.equals("search"))
                IssuesHandler.search(request, response);
            if (action.equals("ajaxGetNewPage"))
            {
                long ajaxStart = System.currentTimeMillis();
                IssuesHandler.ajaxGetNewPage(request, response);
                System.out.println((System.currentTimeMillis() - ajaxStart) + " ms for ajaxGetNewPage()");
            }
            if (action.equals("debug"))
                DebugHandler.getDebugInfo(request, response);
            if (action.equals("logout"))
                logout(request, response);
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

        System.out.println((System.currentTimeMillis() - start) + " ms for last request " + request.getQueryString());
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        request.getSession().invalidate();
        response.sendRedirect("view?action=form");
    }
}
