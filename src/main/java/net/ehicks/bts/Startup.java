package net.ehicks.bts;

import net.ehicks.bts.beans.BtsSystem;
import net.ehicks.common.Common;
import net.ehicks.eoi.ConnectionInfo;
import net.ehicks.eoi.DBMap;
import net.ehicks.eoi.EOI;
import net.ehicks.eoi.SQLGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Startup
{
    private static final Logger log = LoggerFactory.getLogger(Startup.class);

    static void loadProperties(ServletContext servletContext)
    {
        Properties properties = new Properties();

        try (InputStream input = servletContext.getResourceAsStream("/WEB-INF/bts.properties");)
        {
            properties.load(input);
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }

        SystemInfo.INSTANCE.setSystemStart(System.currentTimeMillis());
        SystemInfo.INSTANCE.setServletContext(servletContext);

        SystemInfo.INSTANCE.setAppName(Common.getSafeString(properties.getProperty("appName")));
        SystemInfo.INSTANCE.setDebugLevel(Common.stringToInt(properties.getProperty("debugLevel")));
        SystemInfo.INSTANCE.setDropCreateLoad(properties.getProperty("dropCreateLoad").equals("true"));

        SystemInfo.INSTANCE.setLogDirectory(properties.getProperty("logDirectory"));
        SystemInfo.INSTANCE.setBackupDirectory(properties.getProperty("backupDirectory"));
        SystemInfo.INSTANCE.setOverridePropertiesDirectory(properties.getProperty("overridePropertiesDirectory"));

        ConnectionInfo dbConnectionInfo = new ConnectionInfo(Common.getSafeString(properties.getProperty("dbMode")),
                Common.getSafeString(properties.getProperty("dbHost")),
                Common.getSafeString(properties.getProperty("dbPort")),
                Common.getSafeString(properties.getProperty("dbName")),
                Common.getSafeString(properties.getProperty("dbUser")),
                Common.getSafeString(properties.getProperty("dbPass")),
                Common.getSafeString(properties.getProperty("h2DbCacheKBs")),
                Common.getSafeString(properties.getProperty("pgDumpPath")),
                Common.getSafeString(properties.getProperty("sqlserverServerInstance")));
        SystemInfo.INSTANCE.setDbConnectionInfo(dbConnectionInfo);

        servletContext.setAttribute("systemInfo", SystemInfo.INSTANCE);
    }

    static void loadOverrideProperties(String path)
    {
        File overridePropsFile = new File(path);
        if (overridePropsFile.exists() && overridePropsFile.isFile())
        {
            Properties overrideProps = new Properties();

            try (InputStream input = new FileInputStream(overridePropsFile);)
            {
                overrideProps.load(input);
            }
            catch (IOException e)
            {
                log.error(e.getMessage(), e);
            }

            BtsSystem btsSystem = BtsSystem.getSystem();
            if (btsSystem == null)
            {
                btsSystem = new BtsSystem();
                EOI.insert(btsSystem, SystemTask.STARTUP);
            }
            // only override email settings if they're not already in the DB.
            if (btsSystem.getEmailHost().isEmpty()) btsSystem.setEmailHost(overrideProps.getProperty("emailHost"));
            if (btsSystem.getEmailPort() == 0) btsSystem.setEmailPort(Common.stringToInt(overrideProps.getProperty("emailPort")));
            if (btsSystem.getEmailUser().isEmpty()) btsSystem.setEmailUser(overrideProps.getProperty("emailUser"));
            if (btsSystem.getEmailPassword().isEmpty()) btsSystem.setEmailPassword(overrideProps.getProperty("emailPassword"));
            if (btsSystem.getEmailFromAddress().isEmpty()) btsSystem.setEmailFromAddress(overrideProps.getProperty("emailFromAddress"));
            if (btsSystem.getEmailFromName().isEmpty()) btsSystem.setEmailFromName(overrideProps.getProperty("emailFromName"));

            EOI.update(btsSystem, SystemTask.STARTUP);
        }
    }

    static void loadDBMaps(ServletContext servletContext)
    {
        long subTaskStart = System.currentTimeMillis();
        DBMap.loadDbMaps(servletContext.getRealPath("/WEB-INF/classes/net/ehicks/bts/beans"), "net.ehicks.bts.beans");
        log.debug("Loaded DBMAPS in {} ms", (System.currentTimeMillis() - subTaskStart));
    }

    static void createTables()
    {
        long subTaskStart = System.currentTimeMillis();
        int tablesCreated = 0;
        for (DBMap dbMap : DBMap.dbMaps)
            if (!EOI.isTableExists(dbMap.tableName))
            {
                String createTableStatement = SQLGenerator.getCreateTableStatement(dbMap);
                EOI.executeUpdate(createTableStatement);
                tablesCreated++;

                for (String indexDefinition : dbMap.indexDefinitions)
                    EOI.executeUpdate(indexDefinition);
            }
        log.info("Autocreated {}/{} tables in {} ms", tablesCreated, DBMap.dbMaps.size(), (System.currentTimeMillis() - subTaskStart));
    }

    static void dropTables()
    {
        long subTaskStart;
        subTaskStart = System.currentTimeMillis();
        int tablesDropped = 0;
        for (DBMap dbMap : DBMap.dbMaps)
        {
            String tableName = dbMap.tableName;
            try
            {
                if (EOI.isTableExists(tableName))
                {
                    log.debug("Dropping " + tableName + "...");
                    EOI.executeUpdate("drop table " + tableName);
                    tablesDropped++;
                }
            }
            catch (Exception e)
            {
                log.error("didnt drop {}", tableName);
            }
        }
        log.info("Dropped {}/{} existing tables in {} ms", tablesDropped, DBMap.dbMaps.size(), (System.currentTimeMillis() - subTaskStart));
    }
}
