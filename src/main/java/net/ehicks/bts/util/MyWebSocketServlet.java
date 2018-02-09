package net.ehicks.bts.util;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/chat" }, loadOnStartup = 3)
public class MyWebSocketServlet extends WebSocketServlet
{
    @Override
    public void configure(WebSocketServletFactory factory)
    {
        System.out.println("net.ehicks.bts.util.MyWebSocketServlet.configure");
        factory.getPolicy().setIdleTimeout(0 /* seconds */ * 1000 /* millis */);
        factory.setCreator(new MySessionSocketCreator());
        factory.register(ChatSocket.class);
    }

    @Override
    public void init() throws ServletException
    {
        super.init();
    }
}
