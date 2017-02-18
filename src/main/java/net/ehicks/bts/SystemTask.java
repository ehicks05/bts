package net.ehicks.bts;

import net.ehicks.eoi.AuditUser;

public enum SystemTask implements AuditUser
{
    DEFAULT_DATA_LOADER("DEFAULT_DATA_LOADER", "127.0.0.1"),
    EMAIL_ENGINE("EMAIL_ENGINE", "127.0.0.1");

    private String id;
    private String ipAddress;

    SystemTask(String id, String ipAddress)
    {
        this.id = id;
        this.ipAddress = ipAddress;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getIpAddress()
    {
        return ipAddress;
    }
}
