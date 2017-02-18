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

    private int debugLevel;
    private boolean dropCreateLoad;

    private ServletContext servletContext;
    private long systemStart;
    private String dbConnectionString;
    private long databaseCacheInKBs;

    private String emailHost = "";
    private int emailPort;
    private String emailUser = "";
    private String emailPassword = "";
    private String emailFromAddress = "";
    private String emailFromName = "";
    private String emailContext = "";

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

    public String getEmailHost()
    {
        return emailHost;
    }

    public void setEmailHost(String emailHost)
    {
        this.emailHost = emailHost;
    }

    public int getEmailPort()
    {
        return emailPort;
    }

    public void setEmailPort(int emailPort)
    {
        this.emailPort = emailPort;
    }

    public String getEmailUser()
    {
        return emailUser;
    }

    public void setEmailUser(String emailUser)
    {
        this.emailUser = emailUser;
    }

    public String getEmailPassword()
    {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword)
    {
        this.emailPassword = emailPassword;
    }

    public String getEmailFromAddress()
    {
        return emailFromAddress;
    }

    public void setEmailFromAddress(String emailFromAddress)
    {
        this.emailFromAddress = emailFromAddress;
    }

    public String getEmailFromName()
    {
        return emailFromName;
    }

    public void setEmailFromName(String emailFromName)
    {
        this.emailFromName = emailFromName;
    }

    public String getEmailContext()
    {
        return emailContext;
    }

    public void setEmailContext(String emailContext)
    {
        this.emailContext = emailContext;
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
