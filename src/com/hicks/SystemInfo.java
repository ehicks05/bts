package com.hicks;

import javax.servlet.ServletContext;
import java.util.Properties;

public class SystemInfo
{
    private static Properties properties;
    private static ServletContext servletContext;
    private static int debugLevel;

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
}
