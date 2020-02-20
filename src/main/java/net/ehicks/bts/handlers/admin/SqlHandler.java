package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.beans.Group;
import net.ehicks.bts.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SqlHandler
{
    private static final Logger log = LoggerFactory.getLogger(SqlHandler.class);

    private EntityManager entityManager;

    public SqlHandler(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @GetMapping("/admin/sql/form")
    public ModelAndView showSql()
    {
        return new ModelAndView("admin/sql");
    }

    @PostMapping("/admin/sql/runCommand")
    public ModelAndView runSqlCommand(@AuthenticationPrincipal User user,
                                      @RequestParam String sqlCommand,
                                      @RequestParam Boolean truncateResults)
    {
        log.info("Received command from " + user.getUsername() + " at ip " + "todo" + ". ");

        List<PrintableSqlResult> printableSqlResults = new ArrayList<>();

        int i = 1;
        for (String command : sqlCommand.split(";"))
        {
            command = command.trim();
            log.info("Command " + i++ + ": " + command.replaceAll("\r\n", " "));
            PrintableSqlResult printableSqlResult = new PrintableSqlResult(command.trim());

            try
            {
                if (command.toUpperCase().startsWith("SELECT") || command.toUpperCase().startsWith("EXPLAIN"))
                {
                    TypedQuery<Group> query = entityManager.createQuery(command, Group.class);
//                    Map<String, List<Object>> printableResult = EOI.getPrintableResult(command);
                    List<Group> resultRows = query.getResultList();
                    if (truncateResults && resultRows.size() > 1000)
                    {
                        resultRows = resultRows.subList(0, 1000);
                        printableSqlResult.setTruncated(true);
                    }

//                    printableSqlResult.setColumnLabels(printableResult.get("columnLabels"));
                    printableSqlResult.setResultRows((List) resultRows);
                }

                boolean isDML = command.toUpperCase().startsWith("CREATE")
                        || command.toUpperCase().startsWith("DROP")
                        || command.toUpperCase().startsWith("INSERT")
                        || command.toUpperCase().startsWith("UPDATE")
                        || command.toUpperCase().startsWith("DELETE");

                if (isDML)
                {
//                    Integer rowsUpdated = EOI.executeUpdate(command);
//                    printableSqlResult.setRowsUpdated(rowsUpdated);
                }
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                printableSqlResult.setError(e.getMessage());
            }

            printableSqlResults.add(printableSqlResult);
        }

        return new ModelAndView("redirect:/admin/sql/form")
                .addObject("sqlCommand", sqlCommand)
                .addObject("truncateResults", truncateResults)
                .addObject("resultSets", printableSqlResults);
    }
}
