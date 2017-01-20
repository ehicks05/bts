package net.ehicks.bts;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener
{
    private static ConcurrentHashMap<String, UserSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void sessionCreated(HttpSessionEvent se)
    {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se)
    {
        HttpSession session = se.getSession();

        ServletContext context = session.getServletContext();
        Map activeUsers = (Map) context.getAttribute("activeUsers");
        activeUsers.remove(session.getId());
        sessions.remove(session.getId());

        System.out.println("session destroyed: " + se.getSession().getId());
    }

    public void attributeAdded(HttpSessionBindingEvent event)
    {
        HttpSession session = event.getSession();
        if (event.getName().equals("userSession"))
        {
            Object temp = event.getValue();
            if (temp instanceof UserSession)
            {
                UserSession userSession = (UserSession) event.getValue();
//                SessionManager.audit(session.getId(), userSession.getLogonId(), userSession.getUserObjectid(), null, new java.util.Date(),
//                        userSession.getRemoteIP(), userSession.getRemoteHost() , "",0,"","Logged on.", userSession.getUserAgent());
                sessions.put(session.getId(), userSession);
                System.out.println("put " + session.getId() + " in session");
            }
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent event)
    {
        if (event.getName().equals("userSession"))
        {
            Object temp = event.getValue();
            if (temp instanceof UserSession)
            {
                UserSession userSession = (UserSession) event.getValue();
//                SessionManager.audit(session.getId(), userSession.getLogonId(), userSession.getUserObjectid(), null, new java.util.Date(),
//                        userSession.getRemoteIP(), userSession.getRemoteHost(), "", 0, "", "Logged off.", userSession.getUserAgent());
            }
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event)
    {

    }

    public static Map<String, UserSession> getSessions()
    {
        return sessions;
    }

    public static List<String> getSessionIds()
    {
        return new ArrayList<>(sessions.keySet());
    }

    public static UserSession getBySessionId(String sessionId)
    {
        return sessions.get(sessionId);
    }
}
