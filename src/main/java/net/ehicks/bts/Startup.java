package net.ehicks.bts;

import net.ehicks.eoi.DBMap;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.SQLGenerator;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Startup
{
    static void loadProperties(ServletContext servletContext)
    {
        Properties properties = new Properties();

        try (InputStream input = servletContext.getResourceAsStream("/WEB-INF/bts.properties");)
        {
            properties.load(input);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        SystemInfo.INSTANCE.setSystemStart(System.currentTimeMillis());
        SystemInfo.INSTANCE.setServletContext(servletContext);

        SystemInfo.INSTANCE.setDebugLevel(net.ehicks.common.Common.stringToInt(properties.getProperty("debugLevel")));
        SystemInfo.INSTANCE.setDropTables(properties.getProperty("dropTables").equals("true"));
        SystemInfo.INSTANCE.setCreateTables(properties.getProperty("createTables").equals("true"));
        SystemInfo.INSTANCE.setLoadDemoData(properties.getProperty("loadDemoData").equals("true"));

        SystemInfo.INSTANCE.setDatabaseCacheInKBs(net.ehicks.common.Common.stringToLong(properties.getProperty("databaseCacheInKBs")));

        SystemInfo.INSTANCE.setEmailHost(properties.getProperty("emailHost"));
        SystemInfo.INSTANCE.setEmailPort(net.ehicks.common.Common.stringToInt(properties.getProperty("emailPort")));
        SystemInfo.INSTANCE.setEmailUser(properties.getProperty("emailUser"));
        SystemInfo.INSTANCE.setEmailPassword(properties.getProperty("emailPassword"));
        SystemInfo.INSTANCE.setEmailFromAddress(properties.getProperty("emailFromAddress"));
        SystemInfo.INSTANCE.setEmailFromName(properties.getProperty("emailFromName"));
    }

    static void loadDBMaps(ServletContext servletContext)
    {
        long subTaskStart = System.currentTimeMillis();
        DBMap.loadDbMaps(servletContext.getRealPath("/WEB-INF/classes/net/ehicks/bts/beans"), "net.ehicks.bts.beans");
        System.out.println("Loaded DBMAPS in " + (System.currentTimeMillis() - subTaskStart) + "ms");
    }

    static void createTables()
    {
        long subTaskStart = System.currentTimeMillis();
        int tablesCreated = 0;
        for (DBMap dbMap : DBMap.dbMaps)
            if (!EOI.isTableExists(dbMap))
            {
                String createTableStatement = SQLGenerator.getCreateTableStatement(dbMap);
                EOI.executeUpdate(createTableStatement);
                tablesCreated++;

                for (String indexDefinition : dbMap.indexDefinitions)
                    EOI.executeUpdate(indexDefinition);
            }
        System.out.println("Autocreated " + tablesCreated + " tables in " + (System.currentTimeMillis() - subTaskStart) + "ms");
    }

    static void dropTables()
    {
        long subTaskStart;
        subTaskStart = System.currentTimeMillis();
        for (DBMap dbMap : DBMap.dbMaps)
        {
            try
            {
                EOI.executeUpdate("drop table " + dbMap.tableName);
            }
            catch (Exception e)
            {
                System.out.println("didnt drop " + dbMap.tableName);
            }
        }
        System.out.println("Dropped existing tables in " + (System.currentTimeMillis() - subTaskStart) + "ms");
    }
}
