package com.hicks;

import javax.servlet.ServletContext;
import java.net.URL;
import java.util.Properties;

public class SystemInfo
{
    private static Properties properties;
    private static ServletContext servletContext;
    private static int debugLevel;

    private static String emailHost = "";
    private static int emailPort;
    private static String emailUser = "";
    private static String emailPassword = "";
    private static String emailFromAddress = "";
    private static String emailFromName = "";

    public static long getFreeRamMb()
    {
        long maxMemory = Runtime.getRuntime().maxMemory() ;
        long allocatedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        return (maxMemory - allocatedMemory);
    }

    public static Properties getProperties()
    {
        return properties;
    }

    public static void setProperties(Properties properties)
    {
        SystemInfo.properties = properties;
    }

    public static ServletContext getServletContext()
    {
        return servletContext;
    }

    public static void setServletContext(ServletContext servletContext)
    {
        SystemInfo.servletContext = servletContext;
    }

    public static int getDebugLevel()
    {
        return debugLevel;
    }

    public static void setDebugLevel(int debugLevel)
    {
        SystemInfo.debugLevel = debugLevel;
    }

    public static String getEmailHost()
    {
        return emailHost;
    }

    public static void setEmailHost(String emailHost)
    {
        SystemInfo.emailHost = emailHost;
    }

    public static int getEmailPort()
    {
        return emailPort;
    }

    public static void setEmailPort(int emailPort)
    {
        SystemInfo.emailPort = emailPort;
    }

    public static String getEmailUser()
    {
        return emailUser;
    }

    public static void setEmailUser(String emailUser)
    {
        SystemInfo.emailUser = emailUser;
    }

    public static String getEmailPassword()
    {
        return emailPassword;
    }

    public static void setEmailPassword(String emailPassword)
    {
        SystemInfo.emailPassword = emailPassword;
    }

    public static String getEmailFromAddress()
    {
        return emailFromAddress;
    }

    public static void setEmailFromAddress(String emailFromAddress)
    {
        SystemInfo.emailFromAddress = emailFromAddress;
    }

    public static String getEmailFromName()
    {
        return emailFromName;
    }

    public static void setEmailFromName(String emailFromName)
    {
        SystemInfo.emailFromName = emailFromName;
    }
}
