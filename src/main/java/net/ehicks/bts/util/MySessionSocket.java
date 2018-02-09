package net.ehicks.bts.util;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.servlet.http.HttpSession;

@WebSocket
public class MySessionSocket
{
    private HttpSession httpSession;
    private Session wsSession;

    public MySessionSocket(HttpSession httpSession)
    {
        this.httpSession = httpSession;
    }

    @OnWebSocketConnect
    public void onOpen(Session wsSession)
    {
        this.wsSession = wsSession;
    }
}