package com.hicks;

import javax.servlet.ServletContext;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public enum SystemInfo
{
    INSTANCE;

    private Properties properties;
    private ServletContext servletContext;
    private int debugLevel;
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

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    public ServletContext getServletContext()
    {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    public int getDebugLevel()
    {
        return debugLevel;
    }

    public void setDebugLevel(int debugLevel)
    {
        this.debugLevel = debugLevel;
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
