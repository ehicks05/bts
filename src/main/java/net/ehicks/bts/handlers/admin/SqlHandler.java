package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.DefaultDataLoader;
import net.ehicks.bts.Route;
import net.ehicks.bts.UserSession;
import net.ehicks.eoi.EOI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SqlHandler
{
    private static final Logger log = LoggerFactory.getLogger(DefaultDataLoader.class);

    @Route(tab1 = "admin", tab2 = "sql", tab3 = "", action = "form")
    public static String showSql(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        return "/WEB-INF/webroot/admin/sql.jsp";
    }

    @Route(tab1 = "admin", tab2 = "sql", tab3 = "", action = "runCommand")
    public static void runSqlCommand(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String sqlCommand = request.getParameter("sqlCommand");
        List<String> commands = Arrays.asList(sqlCommand.split(";"));

        for (String command : commands)
        {
            if (sqlCommand.toUpperCase().startsWith("SELECT"))
            {
                clearSession(request);

                try
                {
                    Map<String, List<Object>> printableResults = EOI.getPrintableResults(sqlCommand);
                    request.getSession().setAttribute("columnLabels", printableResults.get("columnLabels"));
                    request.getSession().setAttribute("resultRows", printableResults.get("resultRows"));
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                    request.getSession().setAttribute("error", e.getMessage());
                }
            }
            if (sqlCommand.toUpperCase().startsWith("INSERT") || sqlCommand.toUpperCase().startsWith("UPDATE") || sqlCommand.toUpperCase().startsWith("DELETE"))
            {
                clearSession(request);

                try
                {
                    int rowsUpdated = EOI.executeUpdate(sqlCommand);
                    request.getSession().setAttribute("rowsUpdated", rowsUpdated);
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                    request.getSession().setAttribute("error", e.getMessage());
                }
            }
        }
        request.getSession().setAttribute("sqlCommand", sqlCommand);
        response.sendRedirect("view?tab1=admin&tab2=sql&action=form");
    }

    private static void clearSession(HttpServletRequest request)
    {
        request.getSession().removeAttribute("resultRows");
        request.getSession().removeAttribute("columnLabels");
        request.getSession().removeAttribute("rowsUpdated");
        request.getSession().removeAttribute("error");
    }
}
