package net.ehicks.bts;

public final class DbSettings
{
    private static String dbMode = "";
    private static String dbHost = "";
    private static String dbPort = "";
    private static String dbName = "";
    private static String dbUser = "";
    private static String dbPass = "";
    private static String h2DbCacheKBs = "";
    private static String pgDumpPath = "";
    private static String sqlserverServerInstance = "";

    private enum DbModes
    {
        H2_MEM, H2_TCP, SQLSERVER, POSTGRESQL
    }

    public static String getDbConnectionString()
    {
        String connectionString = "";

        String h2Settings = "TRACE_LEVEL_FILE=1;DB_CLOSE_ON_EXIT=FALSE;COMPRESS=TRUE;CACHE_SIZE=" + h2DbCacheKBs + ";";
        if (getDbMode().equals(DbModes.H2_MEM.name()))
        {
            connectionString += "jdbc:h2:mem:" + h2Settings;
        }
        if (getDbMode().equals(DbModes.H2_TCP.name()))
        {
            connectionString += "jdbc:h2:tcp://" + dbHost + ":" + dbPort + "/" + dbName + ";" + h2Settings;
        }
        if (getDbMode().equals(DbModes.SQLSERVER.name()))
        {
            connectionString += "jdbc:sqlserver://" + dbHost + "\\" + sqlserverServerInstance + ":" + dbPort + ";user=" + dbUser + ";password=" + dbPass;
        }
        if (getDbMode().equals(DbModes.POSTGRESQL.name()))
        {
            connectionString += "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName + "?user=" + dbUser + "&password=" + dbPass;
        }

        return connectionString;
    }

    public static String getDbMode()
    {
        return dbMode;
    }

    public static void setDbMode(String dbMode)
    {
        DbSettings.dbMode = dbMode;
    }

    public static String getDbHost()
    {
        return dbHost;
    }

    public static void setDbHost(String dbHost)
    {
        DbSettings.dbHost = dbHost;
    }

    public static String getDbPort()
    {
        return dbPort;
    }

    public static void setDbPort(String dbPort)
    {
        DbSettings.dbPort = dbPort;
    }

    public static String getDbName()
    {
        return dbName;
    }

    public static void setDbName(String dbName)
    {
        DbSettings.dbName = dbName;
    }

    public static String getDbUser()
    {
        return dbUser;
    }

    public static void setDbUser(String dbUser)
    {
        DbSettings.dbUser = dbUser;
    }

    public static String getDbPass()
    {
        return dbPass;
    }

    public static void setDbPass(String dbPass)
    {
        DbSettings.dbPass = dbPass;
    }

    public static String getH2DbCacheKBs()
    {
        return h2DbCacheKBs;
    }

    public static void setH2DbCacheKBs(String h2DbCacheKBs)
    {
        DbSettings.h2DbCacheKBs = h2DbCacheKBs;
    }

    public static String getPgDumpPath()
    {
        return pgDumpPath;
    }

    public static void setPgDumpPath(String pgDumpPath)
    {
        DbSettings.pgDumpPath = pgDumpPath;
    }

    public static String getSqlserverServerInstance()
    {
        return sqlserverServerInstance;
    }

    public static void setSqlserverServerInstance(String sqlserverServerInstance)
    {
        DbSettings.sqlserverServerInstance = sqlserverServerInstance;
    }
}
