package net.ehicks.bts;

import net.ehicks.common.Common;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;
import java.util.*;

public enum SystemInfo
{
    INSTANCE;

    private String appName;
    private int debugLevel;
    private boolean dropCreateLoad;

    private ServletContext servletContext;
    private long systemStart;
    private String dbConnectionString;
    private long databaseCacheInKBs;

    private String logDirectory = "";
    private String backupDirectory = "";

    public Date getSystemStartTime()
    {
        return new Date(systemStart);
    }

    public List<String> getRuntimeMXBeanArguments()
    {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    public Map<String, String> getStats() throws IOException
    {
        Map<String, String> stats = new LinkedHashMap<>(); // LinkedHashMap to keep insertion order
        stats.put("Start Time", getSystemStartTime().toString());
        stats.put("DB Cache", getDatabaseCache());
        stats.put("Used RAM", getUsedRam());
        stats.put("Free RAM", getFreeRam());
        stats.put("Max RAM", getMaxRam());
        stats.put("Log Directory", Paths.get(getLogDirectory()).toFile().getCanonicalPath());
        stats.put("Backup Directory", Paths.get(getBackupDirectory()).toFile().getCanonicalPath());

        return stats;
    }

    public String getDatabaseCache()
    {
        return Common.toMetric(getDatabaseCacheInKBs() * 1024);
    }

    public String getUsedRam()
    {
        return Common.toMetric(_getUsedRam());
    }

    private long _getUsedRam()
    {
        return _getMaxRam() - _getFreeRam();
    }

    public String getMaxRam()
    {
        return Common.toMetric(_getMaxRam());
    }

    private long _getMaxRam()
    {
        return Runtime.getRuntime().maxMemory();
    }

    public String getFreeRam()
    {
        return Common.toMetric(_getFreeRam());
    }

    private long _getFreeRam()
    {
        long maxMemory = Runtime.getRuntime().maxMemory() ;
        long allocatedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        return (maxMemory - allocatedMemory);
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public boolean isDropCreateLoad()
    {
        return dropCreateLoad;
    }

    public void setDropCreateLoad(boolean dropCreateLoad)
    {
        this.dropCreateLoad = dropCreateLoad;
    }

    public int getDebugLevel()
    {
        return debugLevel;
    }

    public void setDebugLevel(int debugLevel)
    {
        this.debugLevel = debugLevel;
    }

    public ServletContext getServletContext()
    {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    public long getSystemStart()
    {
        return systemStart;
    }

    public void setSystemStart(long systemStart)
    {
        this.systemStart = systemStart;
    }

    public String getDbConnectionString()
    {
        return dbConnectionString;
    }

    public void setDbConnectionString(String dbConnectionString)
    {
        this.dbConnectionString = dbConnectionString;
    }

    public long getDatabaseCacheInKBs()
    {
        return databaseCacheInKBs;
    }

    public void setDatabaseCacheInKBs(long databaseCacheInKBs)
    {
        this.databaseCacheInKBs = databaseCacheInKBs;
    }

    public String getLogDirectory()
    {
        return logDirectory;
    }

    public void setLogDirectory(String logDirectory)
    {
        this.logDirectory = logDirectory;
    }

    public String getBackupDirectory()
    {
        return backupDirectory;
    }

    public void setBackupDirectory(String backupDirectory)
    {
        this.backupDirectory = backupDirectory;
    }
}
