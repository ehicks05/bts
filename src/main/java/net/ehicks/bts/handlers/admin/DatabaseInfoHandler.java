package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.routing.Route;
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
        String query = "SELECT *, pg_size_pretty(total_bytes) AS total\n" +
                "    , pg_size_pretty(index_bytes) AS INDEX\n" +
                "    , pg_size_pretty(toast_bytes) AS toast\n" +
                "    , pg_size_pretty(table_bytes) AS TABLE\n" +
                "  FROM (\n" +
                "  SELECT *, total_bytes-index_bytes-COALESCE(toast_bytes,0) AS table_bytes FROM (\n" +
                "      SELECT c.oid,nspname AS table_schema, relname AS TABLE_NAME\n" +
                "              , c.reltuples AS row_estimate\n" +
                "              , pg_total_relation_size(c.oid) AS total_bytes\n" +
                "              , pg_indexes_size(c.oid) AS index_bytes\n" +
                "              , pg_total_relation_size(reltoastrelid) AS toast_bytes\n" +
                "          FROM pg_class c\n" +
                "          LEFT JOIN pg_namespace n ON n.oid = c.relnamespace\n" +
                "          WHERE relkind = 'r' and nspname='public'\n" +
                "  ) a\n" +
                ") a order by total_bytes desc;";

        Map<String, List<Object>> tableSizes = EOI.getPrintableResult(query);
        request.setAttribute("tableSizes", tableSizes);

        return "/webroot/admin/databaseInfo.jsp";
    }
}
