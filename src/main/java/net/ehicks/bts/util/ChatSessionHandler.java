package net.ehicks.bts.util;

import net.ehicks.bts.beans.ChatRoom;
import net.ehicks.bts.beans.ChatRoomMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

//@ApplicationScoped
public class ChatSessionHandler
{
    private static final Logger log = LoggerFactory.getLogger(ChatSessionHandler.class);

//    private final Map<Session, HttpSession> sessions = new ConcurrentHashMap<>();
    private final Map<Session, String> sessions = new ConcurrentHashMap<>();
    private final Map<ChatRoom, String> chatRooms = new ConcurrentHashMap<>();
    private final Map<ChatRoomMessage, String> chatRoomMessages = new ConcurrentSkipListMap<>();

    public void addSession(Session session)
    {
//        sessions.put(session, (HttpSession) session.getUserProperties().get("httpSession"));
        sessions.put(session, "");
        for (ChatRoomMessage chatRoomMessage : chatRoomMessages.keySet())
        {
            JsonObject addMessage = createAddMessage(chatRoomMessage);
            sendToSession(session, addMessage);
        }
    }

    public void removeSession(Session session)
    {
        sessions.remove(session);
    }

    public void addChatRoomMessage(ChatRoomMessage chatRoomMessage)
    {
        chatRoomMessages.put(chatRoomMessage, "");
        JsonObject addMessage = createAddMessage(chatRoomMessage);
        sendToAllConnectedSessions(addMessage);
    }

    private JsonObject createAddMessage(ChatRoomMessage message)
    {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "add")
                .add("id", message.getId())
                .add("timestamp", message.getTimestamp().toString())
                .add("author", message.getUserId())
                .add("contents", message.getContents())
                .build();
    }

    private void sendToAllConnectedSessions(JsonObject message)
    {
        for (Session session : sessions.keySet())
        {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message)
    {
        try
        {
            session.getBasicRemote().sendText(message.toString());
        }
        catch (IOException ex)
        {
            sessions.remove(session);
            log.error(ex.getMessage());
        }
    }
}