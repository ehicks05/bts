package net.ehicks.bts.util;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.HttpSession;

public class MySessionSocketCreator implements WebSocketCreator
{
    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp)
    {
        HttpSession httpSession = req.getSession();
        return new ChatSocket(httpSession);
    }
}