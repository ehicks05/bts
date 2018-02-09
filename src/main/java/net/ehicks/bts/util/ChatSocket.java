package net.ehicks.bts.util;

import net.ehicks.bts.SessionListener;
import net.ehicks.bts.beans.ChatRoom;
import net.ehicks.bts.beans.ChatRoomMessage;
import net.ehicks.eoi.EOI;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpSession;
import java.io.StringReader;
import java.util.Date;

@WebSocket
public class ChatSocket
{
    private HttpSession httpSession;
    private Session wsSession;

    public ChatSocket(HttpSession httpSession)
    {
        this.httpSession = httpSession;
    }

    @OnWebSocketMessage
    public void onText(Session session, String message)
    {
        System.out.println("Message received:" + message);

        try (JsonReader reader = Json.createReader(new StringReader(message)))
        {
            JsonObject jsonMessage = reader.readObject();
            String action = jsonMessage.getString("action");

            if (action.equals("addMessage"))
            {
                handleAddMessage(session, jsonMessage);
            }

            if (action.equals("remove"))
            {
                int id = (int) jsonMessage.getInt("id");
            }
            if (action.equals("changeRoom"))
            {
                handleChangeRoom(session, jsonMessage);
            }
        }
    }

    private void handleChangeRoom(Session session, JsonObject jsonMessage)
    {
        Long newRoom = Long.valueOf(jsonMessage.getString("newRoom"));
        ChatSessionHandler.changeRoom(session, ChatRoom.getById(newRoom));
    }

    private void handleAddMessage(Session session, JsonObject jsonMessage)
    {
        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setTimestamp(new Date());
        chatRoomMessage.setRoomId(ChatSessionHandler.sessions.get(session).getId());
        chatRoomMessage.setUserId(SessionListener.getBySessionId(httpSession.getId()).getUserId());
        chatRoomMessage.setAuthor(SessionListener.getBySessionId(httpSession.getId()).getLogonId());

        chatRoomMessage.setContents(jsonMessage.getString("contents"));

        long id = EOI.insert(chatRoomMessage, SessionListener.getBySessionId(httpSession.getId()));
        chatRoomMessage = ChatRoomMessage.getById(id);

        ChatSessionHandler.addChatRoomMessage(chatRoomMessage);
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        this.wsSession = session;

        long userId = SessionListener.getBySessionId(httpSession.getId()).getUserId();

        ChatSessionHandler.sendRooms(session, userId);

        System.out.println(session.getRemoteAddress().getHostString() + " connected!");
        ChatSessionHandler.addSession(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason)
    {
        System.out.println(session.getRemoteAddress().getHostString() + " closed!");
        ChatSessionHandler.removeSession(session);
    }
}