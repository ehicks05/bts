package net.ehicks.bts;

public enum SystemTask
{
    SEEDER("SEEDER"),
    STARTUP("STARTUP"),
    REGISTRATION_HANDLER("REGISTRATION_HANDLER"),
    EMAIL_ENGINE("EMAIL_ENGINE");

    private String id;

    SystemTask(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public String getIpAddress()
    {
        return "LOCALHOST";
    }
}
