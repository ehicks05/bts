package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.routing.Route;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DatabaseInfoHandler
{
    @Route(tab1 = "admin", tab2 = "dbInfo", tab3 = "", action = "form")
    public static String showDatabaseInfo(HttpServletRequest request, HttpServletResponse response) throws SQLException
    {
        String query = "SELECT *, total_bytes-index_bytes-COALESCE(toast_bytes,0) AS table_bytes FROM (\n" +
                "      SELECT c.oid,nspname AS table_schema, relname AS TABLE_NAME\n" +
                "              , c.reltuples AS row_estimate\n" +
                "              , pg_total_relation_size(c.oid) AS total_bytes\n" +
                "              , pg_indexes_size(c.oid) AS index_bytes\n" +
                "              , pg_total_relation_size(reltoastrelid) AS toast_bytes\n" +
                "          FROM pg_class c\n" +
                "          LEFT JOIN pg_namespace n ON n.oid = c.relnamespace\n" +
                "          WHERE relkind = 'r' and nspname='public'\n" +
                "  ) a order by total_bytes desc;";

        Map<String, List<Object>> queryResult = EOI.getPrintableResult(query);

        List<Object> columnLabels = queryResult.get("columnLabels");
        List<Object> resultRows = queryResult.get("resultRows");

        for (Object columnLabel : columnLabels)
        {
            int columnIndex = columnLabels.indexOf(columnLabel);
            String label = (String) columnLabel;

            if (label.contains("_bytes"))
            {
                resultRows.forEach(resultRow -> {
                    Object[] row = (Object[]) resultRow;
                    Long value = (Long) row[columnIndex];
                    if (value == null)
                        row[columnIndex] = "";
                    else
                        row[columnIndex] = Common.toMetric(value);
                });
            }
            if (label.equals("row_estimate"))
            {
                resultRows.forEach(resultRow -> {
                    Object[] row = (Object[]) resultRow;
                    float value = (Float) row[columnIndex];
                    row[columnIndex] = Common.toMetric((long) value, "");
                });
            }
        }

        request.setAttribute("columnLabels", columnLabels);
        request.setAttribute("resultRows", resultRows);

        return "/webroot/admin/databaseInfo.jsp";
    }
}
