package net.ehicks.bts;

import net.ehicks.eoi.AuditUser;

public enum SystemTask implements AuditUser
{
    DEFAULT_DATA_LOADER("DEFAULT_DATA_LOADER"),
    STARTUP("STARTUP"),
    EMAIL_ENGINE("EMAIL_ENGINE");

    private String id;

    SystemTask(String id)
    {
        this.id = id;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getIpAddress()
    {
        return "LOCALHOST";
    }
}
