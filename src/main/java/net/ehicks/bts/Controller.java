package net.ehicks.bts;

import net.ehicks.bts.beans.*;
import net.ehicks.bts.routing.Route;
import net.ehicks.bts.routing.RouteDescription;
import net.ehicks.bts.routing.Router;
import net.ehicks.bts.util.ChatSessionHandler;
import net.ehicks.bts.util.CommonScheduling;
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
import java.lang.reflect.Method;
import java.security.Principal;
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
        Startup.loadVersionFile(getServletContext());

        EOI.init(SystemInfo.INSTANCE.getDbConnectionInfo());

        Startup.loadDBMaps(getServletContext());

        if (SystemInfo.INSTANCE.isDropCreateLoad())
        {
            Startup.dropTables();
            Startup.createTables();

            BtsSystem btsSystem = BtsSystem.getSystem();
            if (btsSystem == null)
            {
                btsSystem = new BtsSystem();
                EOI.insert(btsSystem, SystemTask.STARTUP);
            }

            Seeder.createDemoData();
        }
        else
        {
            // run pre-migration sql script here
            Startup.runSqlScripts();
            // database migration here
            Startup.migrateDb();
        }

        Startup.loadOverrideProperties(SystemInfo.INSTANCE.getOverridePropertiesDirectory());

        Router.loadRoutes();

        BackupDbTask.scheduleTask();

        ChatSessionHandler.init(); // todo: move this?

        getServletContext().setAttribute("btsSystem", BtsSystem.getSystem());

        log.info("BTS Controller.init done in {} ms", (System.currentTimeMillis() - SystemInfo.INSTANCE.getSystemStart()));
    }

    @Override
    public void destroy()
    {
        log.info("BTS shutting down...");
        CommonScheduling.shutDown(EmailThreadPool.getPool());
        BackupDbTask.getScheduler().stop();
        EOI.destroy();
        log.info("BTS finished shutting down...");
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
            if (request.getParameter("action") != null && request.getParameter("action").equals("logout"))
            {
                response.sendRedirect("view?tab1=dashboard&action=form");
                return;
            }
        }
        userSession.setLastActivity(new Date());
        userSession.setEnteredController(System.currentTimeMillis());

        if (!User.getByUserId(userSession.getUserId()).getEnabled())
        {
            invalidateSession(request);

            request.setAttribute("responseMessage", "Your account is disabled...");

            RequestDispatcher dispatcher = request.getRequestDispatcher("/webroot/login-failed.jsp");
            dispatcher.forward(request, response);

            return;
        }

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");

        // start of collections used in footer.jsp
        request.setAttribute("issueTypes", IssueType.getAll());
        request.setAttribute("severities", Severity.getAll());
        request.setAttribute("statuses", Status.getAll());

        // the following collections have restricted access
        request.setAttribute("projects", Project.getAllVisible(userSession.getUserId()));
        request.setAttribute("groups", Group.getAllVisible(userSession.getUserId()));
        // end of collections used in footer.jsp

        if (request.getParameter("tab1") == null)
        {
            response.sendRedirect("view?tab1=dashboard&action=form");
            return;
        }

        String viewJsp = processRequest(request, response, userSession);
        long duration = System.currentTimeMillis() - start;
        request.getSession().setAttribute("lastRequestDuration", duration);

        if (viewJsp.length() > 0)
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewJsp);
            dispatcher.forward(request, response);
        }

        if (duration > 100)
            log.info("{} ms for last request {}", duration, request.getQueryString());
    }

    private static String processRequest(HttpServletRequest request, HttpServletResponse response, UserSession userSession) throws IOException
    {
        String tab1   = request.getParameter("tab1") == null ? "" : request.getParameter("tab1");
        String tab2   = request.getParameter("tab2") == null ? "" : request.getParameter("tab2");
        String tab3   = request.getParameter("tab3") == null ? "" : request.getParameter("tab3");
        String action = request.getParameter("action") == null ? "form" : request.getParameter("action");

        // security
        if (tab1.equals("admin") && !userSession.getUser().isAdmin())
            return "/webroot/error.jsp";

        // routing
        RouteDescription routeDescription = new RouteDescription(tab1, tab2, tab3, action);
        Method handler = Router.getRouteMap().get(routeDescription);
        if (handler != null)
        {
            try
            {
                Object result = handler.invoke(handler.getClass(), request, response);
                if (result != null && result instanceof String)
                    return (String) result;
                else
                    return "";
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                response.sendRedirect("view?tab1=dashboard&action=form");
            }
        }
        
        return "";
    }

    private static UserSession createSession(HttpServletRequest request)
    {
        Principal principal = request.getUserPrincipal();
        User user = User.getByLogonId(principal.getName());

        UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setLogonId(user.getLogonId());
        userSession.setSessionId(request.getSession().getId());
        userSession.setIpAddress(request.getRemoteAddr());
        userSession.setLastActivity(new Date());
        request.getSession().setAttribute("userSession", userSession);

        return userSession;
    }

    @Route(tab1 = "logout", tab2 = "", tab3 = "", action = "logout")
    private static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        invalidateSession(request);

        response.sendRedirect("view?tab1=dashboard&action=form");
    }

    private static void invalidateSession(HttpServletRequest request)
    {
        request.removeAttribute("userSession");
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
    }
}
