package net.ehicks.bts;

import net.ehicks.common.Common;
import net.ehicks.eoi.ConnectionInfo;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;
import java.util.*;

public enum SystemInfo
{
    INSTANCE;

    private ConnectionInfo dbConnectionInfo;

    private String appName;
    private int debugLevel;
    private boolean dropCreateLoad;

    private ServletContext servletContext;
    private long systemStart;
    private long databaseCacheInKBs;

    private String logDirectory = "";
    private String backupDirectory = "";
    private String overridePropertiesDirectory = "";

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

    /** url, icon name, label, tab2 of the url */
    public List<List<String>> getSettingsSubscreens()
    {
        return Arrays.asList(
                Arrays.asList("view?tab1=settings&tab2=savedSearches&action=form", "search", "Saved Searches", "savedSearches"),
                Arrays.asList("view?tab1=settings&tab2=subscriptions&action=form", "envelope", "Subscriptions", "subscriptions")
        );
    }

    /** url, icon name, label, tab2 of the url */
    public List<List<String>> getAdminSubscreens()
    {
        return Arrays.asList(
                Arrays.asList("view?tab1=admin&tab2=system&tab3=modify&action=form", "server", "Manage System", "system"),
                Arrays.asList("view?tab1=admin&tab2=users&action=form", "user", "Manage Users", "users"),
                Arrays.asList("view?tab1=admin&tab2=groups&action=form", "users", "Manage Groups", "groups"),
                Arrays.asList("view?tab1=admin&tab2=projects&action=form", "folder", "Manage Projects", "projects"),
                Arrays.asList("view?tab1=admin&tab2=cache&action=form", "microchip", "Cache Info", "cache"),
                Arrays.asList("view?tab1=admin&tab2=system&tab3=info&action=form", "chart-bar", "System Info", "system"),
                Arrays.asList("view?tab1=admin&tab2=audit&action=form", "history", "Audit Records", "audit"),
                Arrays.asList("view?tab1=admin&tab2=email&action=form", "envelope", "Manage Email", "email"),
                Arrays.asList("view?tab1=admin&tab2=logs&action=form", "file-alt", "Logs", "logs"),
                Arrays.asList("view?tab1=admin&tab2=backups&action=form", "cloud-upload-alt", "Backups", "backups"),
                Arrays.asList("view?tab1=admin&tab2=sql&action=form", "database", "SQL", "sql")
        );
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

    public String getLogDirectoryCanonical() throws IOException
    {
        return new File(logDirectory).getCanonicalPath();
    }

    public String getBackupDirectoryCanonical() throws IOException
    {
        return new File(backupDirectory).getCanonicalPath();
    }

    // getters setters

    public ConnectionInfo getDbConnectionInfo()
    {
        return dbConnectionInfo;
    }

    public void setDbConnectionInfo(ConnectionInfo dbConnectionInfo)
    {
        this.dbConnectionInfo = dbConnectionInfo;
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

    public String getOverridePropertiesDirectory()
    {
        return overridePropertiesDirectory;
    }

    public void setOverridePropertiesDirectory(String overridePropertiesDirectory)
    {
        this.overridePropertiesDirectory = overridePropertiesDirectory;
    }
}
