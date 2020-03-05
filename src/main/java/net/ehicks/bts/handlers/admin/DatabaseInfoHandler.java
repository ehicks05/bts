package net.ehicks.bts.handlers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

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
        return new ModelAndView("admin/databaseInfo")
                .addObject("databaseSizeColumnLabels", getDatabaseSizeColumnLabels())
                .addObject("databaseSizeRows", getDatabaseSizeRows())
                .addObject("tableSizeColumnLabels", getTableSizeColumnLabels())
                .addObject("tableSizeRows", getTableSizeRows());
    }

    private List<String> getDatabaseSizeColumnLabels()
    {
        return Arrays.asList("Database", "Size");
    }

    private List<Object> getDatabaseSizeRows()
    {
        return entityManager.createNativeQuery("select datname, pg_size_pretty(pg_database_size(datname)) from pg_database " +
                "where datname='puffin' order by pg_database_size(datname);").getResultList();
    }

    private List<String> getTableSizeColumnLabels()
    {
        return Arrays.asList("table_name", "row_estimate", "full_size", "table_size", "index_size");
    }

    private List<Object> getTableSizeRows()
    {
//        String queryString = """
//                select relname, n_live_tup as live_tuples,
//                    pg_size_pretty(pg_total_relation_size(relname::regclass)) as full_size,
//                    pg_size_pretty(pg_table_size(relname::regclass)) as table_size,
//                    pg_size_pretty(pg_total_relation_size(relname::regclass) - pg_table_size(relname::regclass)) as index_size
//                    from pg_stat_user_tables
//                    order by pg_total_relation_size(relname::regclass) desc;
//                    """;

        String relNameRegClass = "relname\\:\\:regclass";
        String queryString =
                "select relname, " +
                    "to_char(n_live_tup, '999G999') as live_tuples," +
                    "pg_size_pretty(pg_total_relation_size(" + relNameRegClass + ")) as full_size," +
                    "pg_size_pretty(pg_table_size(" + relNameRegClass + ")) as table_size," +
                    "pg_size_pretty(pg_total_relation_size(" + relNameRegClass + ") - pg_table_size(" + relNameRegClass + ")) as index_size" +
                    " from pg_stat_user_tables" +
                    " order by pg_total_relation_size(" + relNameRegClass + ") desc;"
                ;

        return entityManager.createNativeQuery(queryString).getResultList();
    }
}
