//package net.ehicks.bts;
//
//import net.ehicks.bts.routing.RouteDescription;
//import net.ehicks.bts.routing.Router;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.Date;
//
//@org.springframework.stereotype.Controller
//@RequestMapping(value = "/view")
//public class Controller
//{
//    private static final Logger log = LoggerFactory.getLogger(Controller.class);
//
////    public void destroy()
////    {
////        log.info("BTS shutting down...");
////        CommonScheduling.shutDown(EmailThreadPool.getPool());
////        BackupDbTask.getScheduler().stop();
////        EOI.destroy();
////        log.info("BTS finished shutting down...");
////    }
//
//    @GetMapping
//    @PostMapping
//    protected void handleRequest(@AuthenticationPrincipal UserSession userSession, HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException
//    {
//        long start = System.currentTimeMillis();
//
//        userSession.setLastActivity(new Date());
//        userSession.setEnteredController(System.currentTimeMillis());
//
//        // start of collections used in footer.jsp
////        request.setAttribute("issueTypes", IssueType.getAll());
////        request.setAttribute("severities", Severity.getAll());
////        request.setAttribute("statuses", Status.getAll());
////
////        // the following collections have restricted access
////        request.setAttribute("projects", Project.getAllVisible(userSession.getUserId()));
////        request.setAttribute("groups", Group.getAllVisible(userSession.getUserId()));
//        // end of collections used in footer.jsp
//
//        if (request.getParameter("tab1") == null)
//        {
//            response.sendRedirect("view?tab1=dashboard/form");
//            return;
//        }
//
//        String viewJsp = processRequest(request, response, userSession);
//        long duration = System.currentTimeMillis() - start;
//        request.getSession().setAttribute("lastRequestDuration", duration);
//
//        if (viewJsp.length() > 0)
//        {
//            RequestDispatcher dispatcher = request.getRequestDispatcher(viewJsp);
//            dispatcher.forward(request, response);
//        }
//
//        if (duration > 100)
//            log.info("{} ms for last request {}", duration, request.getQueryString());
//    }
//
//    private static String processRequest(HttpServletRequest request, HttpServletResponse response, UserSession userSession) throws IOException
//    {
//        String tab1   = request.getParameter("tab1") == null ? "" : request.getParameter("tab1");
//        String tab2   = request.getParameter("tab2") == null ? "" : request.getParameter("tab2");
//        String tab3   = request.getParameter("tab3") == null ? "" : request.getParameter("tab3");
//        String action = request.getParameter("action") == null ? "form" : request.getParameter("action");
//
//        // security
////        if (tab1.equals("admin") && !userSession.getUser().isAdmin())
////            return "/webroot/error.jsp";
//
//        // routing
//        RouteDescription routeDescription = new RouteDescription(tab1, tab2, tab3, action);
//        Method handler = Router.getRouteMap().get(routeDescription);
//        if (handler != null)
//        {
//            try
//            {
//                Object result = handler.invoke(handler.getClass(), request, response);
//                if (result != null && result instanceof String)
//                    return (String) result;
//                else
//                    return "";
//            }
//            catch (Exception e)
//            {
//                log.error(e.getMessage(), e);
//                response.sendRedirect("view?tab1=dashboard/form");
//            }
//        }
//
//        return "";
//    }
//}
