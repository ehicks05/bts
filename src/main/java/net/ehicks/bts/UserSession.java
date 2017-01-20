package net.ehicks.bts;

import net.ehicks.bts.beans.User;

import java.util.Date;

public class UserSession
{
    private Long userId;
    private String logonId = "";
    private String sessionId = "";
    private Date lastActivity;



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

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public Date getLastActivity()
    {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity)
    {
        this.lastActivity = lastActivity;
    }
}
