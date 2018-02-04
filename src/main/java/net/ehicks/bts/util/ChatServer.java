package net.ehicks.bts.util;

import net.ehicks.bts.beans.ChatRoomMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.StringReader;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
@ServerEndpoint(value = "/chat"
)
public class ChatServer
{
    private static final Logger log = LoggerFactory.getLogger(ChatServer.class);

    private static ChatSessionHandler sessionHandler = new ChatSessionHandler();

    private static AtomicLong id = new AtomicLong(1);

    @OnOpen
    public void open(Session session, EndpointConfig config)
    {
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session)
    {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error)
    {
        log.error(error.getMessage());
    }

    @OnMessage
    public void handleMessage(String message, Session session)
    {
        try (JsonReader reader = Json.createReader(new StringReader(message)))
        {
            JsonObject jsonMessage = reader.readObject();
            String action = jsonMessage.getString("action");

            if (action.equals("addMessage"))
            {
                ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
                chatRoomMessage.setTimestamp(new Date());
                chatRoomMessage.setId(id.get());
                chatRoomMessage.setRoomId(id.get());
                chatRoomMessage.setUserId(id.getAndIncrement());

                chatRoomMessage.setContents(jsonMessage.getString("contents"));
                sessionHandler.addChatRoomMessage(chatRoomMessage);
            }

            if (action.equals("remove"))
            {
                int id = (int) jsonMessage.getInt("id");
            }
        }
    }
}