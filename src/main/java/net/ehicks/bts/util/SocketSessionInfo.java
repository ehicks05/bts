package net.ehicks.bts.util;

import java.util.Date;

public class SocketSessionInfo
{
    Long roomId;
    Date lastActive;
    String status;

    public SocketSessionInfo(Long roomId, Date lastActive, String status)
    {
        this.roomId = roomId;
        this.lastActive = lastActive;
        this.status = status;
    }

    public String getStatusClass()
    {
        if (status.equals("active"))
            return "is-success";
        if (status.equals("idle"))
            return "is-info";
        if (status.equals("away"))
            return "is-warning";
        return "";
    }

    public String getStatusIcon()
    {
        if (status.equals("active"))
            return "fas";
        if (status.equals("idle"))
            return "";
        if (status.equals("away"))
            return "";
        return "";
    }

    public Long getRoomId()
    {
        return roomId;
    }

    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    public Date getLastActive()
    {
        return lastActive;
    }

    public void setLastActive(Date lastActive)
    {
        this.lastActive = lastActive;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
