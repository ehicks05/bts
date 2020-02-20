package net.ehicks.bts;

import net.ehicks.common.Common;

import java.lang.management.ManagementFactory;
import java.util.*;

public class SystemInfo
{
    public static SystemInfo INSTANCE = new SystemInfo();

    private long systemStart;

    public Date getSystemStartTime()
    {
        return new Date(systemStart);
    }

    public List<String> getRuntimeMXBeanArguments()
    {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    public Map<String, String> getStats()
    {
        Map<String, String> stats = new LinkedHashMap<>(); // LinkedHashMap to keep insertion order
        stats.put("Start Time", getSystemStartTime().toString());
        stats.put("Used RAM", getUsedRam());
        stats.put("Free RAM", getFreeRam());
        stats.put("Max RAM", getMaxRam());

        return stats;
    }

    /** url, icon name, label, tab2 of the url */
    public List<List<String>> getSettingsSubscreens()
    {
        return Arrays.asList(
                Arrays.asList("/settings/savedSearches/form", "search", "Saved Searches", "savedSearches"),
                Arrays.asList("/settings/subscriptions/form", "envelope", "Subscriptions", "subscriptions")
        );
    }

    /** url, icon name, label, tab2 of the url */
    public List<List<String>> getAdminSubscreens()
    {
        return Arrays.asList(
                Arrays.asList("/admin/system/modify/form", "server", "Manage System", "system"),
                Arrays.asList("/admin/users/form", "user", "Manage Users", "users"),
                Arrays.asList("/admin/groups/form", "users", "Manage Groups", "groups"),
                Arrays.asList("/admin/projects/form", "folder", "Manage Projects", "projects"),
                Arrays.asList("/admin/email/form", "envelope", "Manage Email", "email"),
                Arrays.asList("/admin/backups/form", "cloud-upload-alt", "Backups", "backups"),
                Arrays.asList("/admin/system/info/form", "chart-bar", "System Info", "system"),
                Arrays.asList("/admin/dbInfo/form", "chart-bar", "Database Info", "dbInfo"),
                Arrays.asList("/admin/audit/form", "history", "Audit Records", "audit"),
                Arrays.asList("/admin/sql/form", "database", "SQL", "sql")
        );
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

    // getters setters
    public long getSystemStart()
    {
        return systemStart;
    }

    public void setSystemStart(long systemStart)
    {
        this.systemStart = systemStart;
    }
}
