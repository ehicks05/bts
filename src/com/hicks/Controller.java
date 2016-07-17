package com.hicks;

import com.hicks.beans.*;
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

        createDemoData();

        if (DEBUG_LEVEL > 1)
            for (String argument : ManagementFactory.getRuntimeMXBean().getInputArguments())
                System.out.println(argument);

        System.out.println("Controller.init finished in " + (System.currentTimeMillis() - controllerStart) + " ms");
    }

    private void createDemoData()
    {
        List result = EOI.executeQueryOneResult("select count(*) from bts_users", new ArrayList<>());
        long rows = (Long) result.get(0);
        if (rows == 0)
        {
            User user = new User();
            user.setLogonId("eric");
            user.setPassword("eric");
            EOI.insert(user);

            user = new User();
            user.setLogonId("val");
            user.setPassword("val");
            EOI.insert(user);
        }

        result = EOI.executeQueryOneResult("select count(*) from bts_roles", new ArrayList<>());
        rows = (Long) result.get(0);
        if (rows == 0)
        {
            Role role = new Role();
            role.setLogonId("eric");
            role.setRoleName("user");
            EOI.insert(role);

            role = new Role();
            role.setLogonId("eric");
            role.setRoleName("admin");
            EOI.insert(role);
        }

        result = EOI.executeQueryOneResult("select count(*) from projects", new ArrayList<>());
        rows = (Long) result.get(0);
        if (rows == 0)
        {
            Project project = new Project();
            project.setName("Genesis");
            project.setPrefix("GS");
            EOI.insert(project);

            project = new Project();
            project.setName("SchoolFI");
            project.setPrefix("SF");
            EOI.insert(project);
        }

        result = EOI.executeQueryOneResult("select count(*) from issues", new ArrayList<>());
        rows = (Long) result.get(0);
        if (rows == 0)
        {
            Issue issue = new Issue();
            issue.setTitle("Thing is Broken");
            issue.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras quis lorem ac dui scelerisque gravida. Nulla efficitur turpis nec augue finibus, a dictum neque elementum. Ut eget aliquam nisl. Cras ultricies semper blandit. Nulla in semper leo. Phasellus cursus metus tortor, non lacinia purus hendrerit vitae. Etiam venenatis erat in magna maximus volutpat. Aenean dapibus nisi nisl, vitae viverra nulla cursus a. Nullam efficitur quam non elementum aliquet. Cras odio risus, dapibus ac mauris ut, malesuada pellentesque nunc.\n" +
                    "<br><br>" +
                    "Suspendisse consectetur augue dolor, et dignissim libero dapibus non. Nulla accumsan sollicitudin hendrerit. Aliquam ex eros, volutpat ac lobortis id, aliquet quis odio. Curabitur vel suscipit lorem. Sed a felis justo. Phasellus lacinia lorem eget sem venenatis dapibus. Nulla facilisi. Donec finibus urna sit amet dui porttitor, non laoreet enim luctus. Mauris luctus, tellus vitae tincidunt congue, purus dui faucibus eros, sit amet venenatis lectus orci eget felis. Maecenas tempus, urna nec varius bibendum, ligula libero egestas sapien, et varius eros odio ut libero. Nullam scelerisque consectetur purus, a auctor dui pharetra ac. Sed congue vel risus non bibendum.");
            issue.setProjectId(1L);
            issue.setZoneId(1L);
            issue.setAssigneeUserId(2L);
            issue.setIssueTypeId(1L);
            issue.setCreatedOn(new Date());
            EOI.insert(issue);

            issue = new Issue();
            issue.setTitle("We Would Like This New Thing");
            issue.setDescription("Aliquam nec rhoncus lorem. Curabitur maximus ligula lectus, id fermentum mauris tempus nec. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed vel neque accumsan, sodales velit sed, gravida ipsum. Aliquam erat volutpat. Fusce et nulla dui. Nulla congue dolor vitae nulla imperdiet scelerisque. In iaculis dapibus dolor, id tempus erat sagittis a. Vivamus sed facilisis leo, vel consectetur libero. Pellentesque quis faucibus nisl, in lobortis justo. Donec id quam consectetur, euismod ex nec, porttitor mauris. Quisque sollicitudin arcu nec ex vehicula, quis tempus nibh imperdiet. Vestibulum cursus dui ut diam interdum, eu cursus justo hendrerit. Aliquam molestie dui ex, eu eleifend ipsum pulvinar ut. Mauris mollis, justo at finibus porttitor, tortor diam porttitor lectus, eget varius augue ex at arcu. Maecenas sit amet tellus accumsan, feugiat ligula in, egestas nisl.");
            issue.setProjectId(2L);
            issue.setZoneId(2L);
            issue.setAssigneeUserId(1L);
            issue.setIssueTypeId(2L);
            issue.setCreatedOn(new Date());
            EOI.insert(issue);
        }

        List<Zone> zones = Zone.getAll();
        if (zones.size() == 0)
        {
            Zone zone = new Zone();
            zone.setName("Readington");
            EOI.insert(zone);

            zone = new Zone();
            zone.setName("Bridgewater");
            EOI.insert(zone);
        }

        List<ZoneMap> zoneMaps = ZoneMap.getAll();
        if (zoneMaps.size() == 0)
        {
            User user = User.getByLogonId("eric");
            zones = Zone.getAll();
            for (Zone zone : zones)
            {
                ZoneMap zoneMap = new ZoneMap();
                zoneMap.setUserId(user.getId());
                zoneMap.setZoneId(zone.getId());
                EOI.insert(zoneMap);
            }
            user = User.getByLogonId("val");
            for (Zone zone : zones)
            {
                ZoneMap zoneMap = new ZoneMap();
                zoneMap.setUserId(user.getId());
                zoneMap.setZoneId(zone.getId());
                EOI.insert(zoneMap);
            }
        }

        List<IssueType> issueTypes = IssueType.getAll();
        if (issueTypes.size() == 0)
        {
            IssueType issueType = new IssueType();
            issueType.setName("Bug");
            EOI.insert(issueType);
            issueType.setName("New Feature");
            EOI.insert(issueType);
            issueType.setName("Question");
            EOI.insert(issueType);
        }

        List<Comment> comments = Comment.getAll();
        if (comments.size() == 0)
        {
            Comment comment = new Comment();
            comment.setIssueId(2L);
            comment.setZoneId(2L);
            comment.setCreatedByUserId(1L);
            comment.setCreatedOn(new Date());
            comment.setContent("I think we can do that.");
            EOI.insert(comment);

            comment = new Comment();
            comment.setIssueId(2L);
            comment.setZoneId(2L);
            comment.setCreatedByUserId(2L);
            comment.setCreatedOn(new Date());
            comment.setContent("OK AWESOME TO HEAR SO.");
            EOI.insert(comment);
        }

        List<WatcherMap> watcherMaps = WatcherMap.getAll();
        if (watcherMaps.size() == 0)
        {
            WatcherMap watcherMap = new WatcherMap();
            watcherMap.setUserId(2L);
            watcherMap.setIssueId(2L);
            EOI.insert(watcherMap);
        }
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
            userSession = createSession(request);

        request.setAttribute("zones", Zone.getAllForUser(userSession));
        request.setAttribute("projects", Project.getAll());
        request.setAttribute("issueTypes", IssueType.getAll());

        String tab1   = request.getParameter("tab1") == null ? "main" : request.getParameter("tab1");
        String tab2   = request.getParameter("tab2") == null ? "dash" : request.getParameter("tab2");
        String action = request.getParameter("action") == null ? "form" : request.getParameter("action");

        String viewJsp = "";
        try
        {
            if (tab1.equals("main"))
            {
                if (tab2.equals("dash"))
                    if (action.equals("form"))
                        viewJsp = IssuesHandler.showIssues(request, response);

                if (tab2.equals("issue"))
                {
                    if (action.equals("form"))
                        viewJsp = IssuesHandler.showModifyIssue(request, response);
                    if (action.equals("create"))
                        IssuesHandler.createIssue(request, response);
                }

                if (action.equals("search"))
                    IssuesHandler.search(request, response);

                if (action.equals("debug"))
                    DebugHandler.getDebugInfo(request, response);
                if (action.equals("logout"))
                    logout(request, response);
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
        response.sendRedirect("view?action=form");
    }
}
