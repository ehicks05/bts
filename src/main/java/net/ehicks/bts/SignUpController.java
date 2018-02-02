package net.ehicks.bts;

import net.ehicks.bts.routing.RouteDescription;
import net.ehicks.bts.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet(value = "/signUp", loadOnStartup = 2)
public class SignUpController extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        UserSession userSession = (UserSession) request.getSession(false).getAttribute("userSession");
        if (userSession != null)
        {
            return;
        }

        String viewJsp = processRequest(request, response);

        if (viewJsp.length() > 0)
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewJsp);
            dispatcher.forward(request, response);
        }
    }

    private static String processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String tab1   = request.getParameter("tab1") == null ? "" : request.getParameter("tab1");
        String tab2   = request.getParameter("tab2") == null ? "" : request.getParameter("tab2");
        String tab3   = request.getParameter("tab3") == null ? "" : request.getParameter("tab3");
        String action = request.getParameter("action") == null ? "form" : request.getParameter("action");

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
}
