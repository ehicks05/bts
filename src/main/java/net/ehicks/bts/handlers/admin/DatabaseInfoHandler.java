package net.ehicks.bts.handlers.admin;

import net.ehicks.common.Common;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DatabaseInfoHandler
{
    private EntityManager entityManager;

    public DatabaseInfoHandler(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @GetMapping("admin/dbInfo/form")
    public ModelAndView showDatabaseInfo()
    {
        String queryString = "SELECT *, total_bytes-index_bytes-COALESCE(toast_bytes,0) AS table_bytes FROM (\n" +
                "      SELECT c.oid,nspname AS table_schema, relname AS TABLE_NAME\n" +
                "              , c.reltuples AS row_estimate\n" +
                "              , pg_total_relation_size(c.oid) AS total_bytes\n" +
                "              , pg_indexes_size(c.oid) AS index_bytes\n" +
                "              , pg_total_relation_size(reltoastrelid) AS toast_bytes\n" +
                "          FROM pg_class c\n" +
                "          LEFT JOIN pg_namespace n ON n.oid = c.relnamespace\n" +
                "          WHERE relkind = 'r' and nspname='public'\n" +
                "  ) a order by total_bytes desc;";
        Query query = entityManager.createNativeQuery(queryString);

        List<Object> columnLabels = Arrays.asList("oid", " table_schema", "table_name", "row_estimate", "total_bytes", "index_bytes", "toast_bytes", "table_bytes");
        List<Object> resultRows = query.getResultList();

        for (Object columnLabel : columnLabels)
        {
            int columnIndex = columnLabels.indexOf(columnLabel);
            String label = (String) columnLabel;

            if (label.contains("_bytes"))
            {
                resultRows.forEach(resultRow -> {
                    Object[] row = (Object[]) resultRow;
                    BigInteger value = (BigInteger) row[columnIndex];
                    if (value == null)
                        row[columnIndex] = "";
                    else
                        row[columnIndex] = Common.toMetric(value.longValue());
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

        return new ModelAndView("admin/databaseInfo")
                .addObject("columnLabels", columnLabels)
                .addObject("resultRows", resultRows);
    }
}
