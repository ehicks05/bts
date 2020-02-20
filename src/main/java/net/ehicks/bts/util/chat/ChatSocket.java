//package net.ehicks.bts.util.chat;
//
//import net.ehicks.bts.SessionListener;
//import net.ehicks.bts.beans.ChatRoom;
//import net.ehicks.bts.beans.ChatRoomMessage;
//import net.ehicks.bts.beans.User;
//import net.ehicks.eoi.EOI;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.json.Json;
//import javax.json.JsonObject;
//import javax.json.JsonReader;
//import javax.servlet.http.HttpSession;
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.StringReader;
//import java.util.Date;
//
//@ServerEndpoint(value = "/chat")
//public class ChatSocket
//{
//    private static final Logger log = LoggerFactory.getLogger(ChatSocket.class);
//
//    private HttpSession httpSession;
//    private Session wsSession;
//
//    @OnMessage
//    public void onText(Session session, String message)
//    {
//        log.debug("Message received:" + message);
//
//        Long userId = User.getByLogonId(session.getUserPrincipal().getName()).getId();
//        try (JsonReader reader = Json.createReader(new StringReader(message)))
//        {
//            JsonObject jsonMessage = reader.readObject();
//            String action = jsonMessage.getString("action");
//
//            if (action.equals("addMessage"))
//                handleAddMessage(session, userId, jsonMessage);
//            if (action.equals("changeRoom"))
//                handleChangeRoom(session, userId, jsonMessage);
//            if (action.equals("changeToPrivateRoom"))
//                handleChangeToPrivateRoom(session, userId, jsonMessage);
//        }
//    }
//
//    private void handleChangeRoom(Session session, Long userId, JsonObject jsonMessage)
//    {
//        Long newRoom = Long.valueOf(jsonMessage.getString("newRoom"));
//
//        ChatSessionHandler.changeRoom(session, ChatRoom.getById(newRoom));
//        ChatSessionHandler.announceStatusChange(session, userId);
//    }
//
//    private void handleChangeToPrivateRoom(Session session, Long userId, JsonObject jsonMessage)
//    {
//        Long otherUserId = Long.valueOf(jsonMessage.getString("otherUserId"));
//
//        ChatSessionHandler.changeToPrivateRoom(session, otherUserId);
//        ChatSessionHandler.announceStatusChange(session, userId);
//    }
//
//    private void handleAddMessage(Session session, Long userId, JsonObject jsonMessage)
//    {
//        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
//        chatRoomMessage.setTimestamp(new Date());
//        chatRoomMessage.setRoomId(ChatSessionHandler.sessions.get(session).getRoomId());
//        chatRoomMessage.setUserId(userId);
//        chatRoomMessage.setAuthor(User.getByUserId(userId).getLogonId());
//        chatRoomMessage.setContents(jsonMessage.getString("contents"));
//
//        long messageId = EOI.insert(chatRoomMessage, SessionListener.getByUserId(User.getByLogonId(session.getUserPrincipal().getName()).getId()));
//        chatRoomMessage = ChatRoomMessage.getById(messageId);
//
//        ChatSessionHandler.addChatRoomMessage(chatRoomMessage);
//    }
//
//    @OnOpen
//    public void onConnect(Session session)
//    {
//        this.wsSession = session;
//
//        long userId = User.getByLogonId(session.getUserPrincipal().getName()).getId();
//
//        ChatSessionHandler.addSession(session, userId);
//
//        ChatSessionHandler.sendRooms(session, userId);
//        ChatSessionHandler.sendPeople(session, userId);
//
//        ChatSessionHandler.announceStatusChange(session, userId);
//
//        log.debug(session.getId() + " connected!");
//    }
//
//    @OnClose
//    public void onClose(Session session)
//    {
//        long userId = User.getByLogonId(session.getUserPrincipal().getName()).getId();
//
//        ChatSessionHandler.removeSession(session, userId);
//
//        ChatSessionHandler.announceStatusChange(session, userId);
//
//        log.debug(session.getBasicRemote().toString() + " closed!");
//    }
//
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        log.error(throwable.getMessage(), throwable);
//    }
//}