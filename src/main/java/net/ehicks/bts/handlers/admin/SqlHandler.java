package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.Route;
import net.ehicks.bts.UserSession;
import net.ehicks.eoi.EOI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SqlHandler
{
    private static final Logger log = LoggerFactory.getLogger(SqlHandler.class);

    @Route(tab1 = "admin", tab2 = "sql", tab3 = "", action = "form")
    public static String showSql(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        return "/WEB-INF/webroot/admin/sql.jsp";
    }

    @Route(tab1 = "admin", tab2 = "sql", tab3 = "", action = "runCommand")
    public static void runSqlCommand(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String commandsParam = request.getParameter("sqlCommand").trim();
        log.info("Received command from " + userSession.getUser() + " at ip " + userSession.getIpAddress() + ". ");

        List<PrintableSqlResult> printableSqlResults = new ArrayList<>();

        int i = 1;
        for (String command : Arrays.asList(commandsParam.split(";")))
        {
            command = command.trim();
            log.info("Command " + i++ + ": " + command.replaceAll("\r\n", " "));
            PrintableSqlResult printableSqlResult = new PrintableSqlResult(command.trim());

            try
            {
                if (command.toUpperCase().startsWith("SELECT") || command.toUpperCase().startsWith("EXPLAIN"))
                {
                    Map<String, List<Object>> printableResult = EOI.getPrintableResult(command);
                    List<Object> resultRows = printableResult.get("resultRows");
                    if (resultRows.size() > 1000)
                    {
                        resultRows = resultRows.subList(0, 1000);
                        printableSqlResult.setTruncated(true);
                    }

                    printableSqlResult.setColumnLabels(printableResult.get("columnLabels"));
                    printableSqlResult.setResultRows(resultRows);
                }
                if (command.toUpperCase().startsWith("CREATE") || command.toUpperCase().startsWith("DROP") || command.toUpperCase().startsWith("INSERT") || command.toUpperCase().startsWith("UPDATE") || command.toUpperCase().startsWith("DELETE"))
                {
                    Integer rowsUpdated = EOI.executeUpdate(command);
                    printableSqlResult.setRowsUpdated(rowsUpdated);
                }
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                printableSqlResult.setError(e.getMessage());
            }

            printableSqlResults.add(printableSqlResult);
        }
        request.getSession().setAttribute("sqlCommand", commandsParam);
        request.getSession().setAttribute("resultSets", printableSqlResults);

        response.sendRedirect("view?tab1=admin&tab2=sql&action=form");
    }
}
