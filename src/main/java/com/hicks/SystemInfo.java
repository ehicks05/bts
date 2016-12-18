package com.hicks;

import javax.servlet.ServletContext;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.List;

public enum SystemInfo
{
    INSTANCE;

    private int debugLevel;
    private boolean dropTables;
    private boolean createTables;
    private boolean loadDemoData;

    private ServletContext servletContext;
    private long systemStart;
    private long databaseCacheInKBs;

    private String emailHost = "";
    private int emailPort;
    private String emailUser = "";
    private String emailPassword = "";
    private String emailFromAddress = "";
    private String emailFromName = "";

    public Date getSystemStartTime()
    {
        return new Date(systemStart);
    }

    public List<String> getRuntimeMXBeanArguments()
    {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
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

    public boolean isDropTables()
    {
        return dropTables;
    }

    public void setDropTables(boolean dropTables)
    {
        this.dropTables = dropTables;
    }

    public boolean isCreateTables()
    {
        return createTables;
    }

    public void setCreateTables(boolean createTables)
    {
        this.createTables = createTables;
    }

    public boolean isLoadDemoData()
    {
        return loadDemoData;
    }

    public void setLoadDemoData(boolean loadDemoData)
    {
        this.loadDemoData = loadDemoData;
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
}
