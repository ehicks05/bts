package net.ehicks.bts;

import net.ehicks.bts.util.ChatSessionHandler;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener
{
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private static List<UserSession> sessions = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void sessionCreated(HttpSessionEvent se)
    {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se)
    {
        HttpSession session = se.getSession();

        sessions.remove(session);

        log.info("!!!!!!!!!!! session destroyed: {}", se.getSession().getId());
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
                sessions.add(userSession);
                log.debug("put {} in session", session.getId());
            }
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent event)
    {
        HttpSession session = event.getSession();
        if (event.getName().equals("userSession"))
        {
            Object temp = event.getValue();
            if (temp instanceof UserSession)
            {
                UserSession userSession = (UserSession) event.getValue();
//                SessionManager.audit(session.getId(), userSession.getLogonId(), userSession.getUserObjectid(), null, new java.util.Date(),
//                        userSession.getRemoteIP(), userSession.getRemoteHost(), "", 0, "", "Logged off.", userSession.getUserAgent());
                sessions.remove(userSession);

                Session socketSession = ChatSessionHandler.userIdToSocketSession.get(userSession.getUserId());
                if (socketSession != null)
                {
                    ChatSessionHandler.removeSession(socketSession, userSession.getUserId());
    
                    ChatSessionHandler.announceStatusChange(socketSession, userSession.getUserId());
                }

                log.debug("removed {} from session", session.getId());
            }
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event)
    {

    }

    public static List<UserSession> getSessions()
    {
        return sessions;
    }

    public static UserSession getBySessionId(String sessionId)
    {
        Optional<UserSession> optional = sessions.stream()
                .filter(userSession -> userSession.getSessionId().equals(sessionId))
                .findFirst();
        return optional.orElse(null);
    }

    public static UserSession getByUserId(Long userId)
    {
        Optional<UserSession> optional = sessions.stream()
                .filter(userSession -> userSession.getUserId().equals(userId))
                .findFirst();
        return optional.orElse(null);
    }
}
