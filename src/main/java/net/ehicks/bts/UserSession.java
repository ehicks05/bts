package net.ehicks.bts;

import net.ehicks.bts.beans.User;

public class UserSession
{
    private Long userId;
    private String logonId = "";

    public SystemInfo getSystemInfo()
    {
        return SystemInfo.INSTANCE;
    }

    public User getUser()
    {
        return User.getByUserId(userId);
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getLogonId()
    {
        return logonId;
    }

    public void setLogonId(String logonId)
    {
        this.logonId = logonId;
    }
}
