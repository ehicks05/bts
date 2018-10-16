package net.ehicks.bts.util.chat;

import net.ehicks.bts.Seeder;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.*;
import net.ehicks.bts.util.SocketSessionInfo;
import net.ehicks.common.Common;
import net.ehicks.eoi.AuditUser;
import net.ehicks.eoi.EOI;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ChatSessionHandler
{
    private static final Logger log = LoggerFactory.getLogger(ChatSessionHandler.class);

    public static final Map<Long, Session> userIdToSocketSession = new ConcurrentHashMap<>();
    public static final Map<Session, Long> socketSessionToUserId = new ConcurrentHashMap<>();
    public static final Map<Session, SocketSessionInfo> sessions = new ConcurrentHashMap<>();
    public static final Map<ChatRoom, List<ChatRoomMessage>> chatRooms = new ConcurrentHashMap<>();

    public static void init()
    {
        ChatRoom.getAll().forEach(room -> {
            List<ChatRoomMessage> roomMessages = new CopyOnWriteArrayList<>(ChatRoomMessage.getByRoomId(room.getId()));
            chatRooms.put(room, roomMessages);
        });

        seedChatMessages();
    }

    private static void seedChatMessages()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    Common.sleep((long) (new Random().nextDouble() * 5 * 1000));

                    synchronized (ChatSessionHandler.class)
                    {
                        long roomId = new Random().nextInt(chatRooms.keySet().size()) + 1;
                        long userId = new Random().nextInt(User.getAll().size()) + 1;
                        User user = User.getByUserId(userId);

                        ChatRoomMessage message = new ChatRoomMessage();
                        message.setRoomId(roomId);
                        message.setUserId(userId);
                        message.setAuthor(user.getName());
                        message.setTimestamp(new Date());
                        message.setContents(Seeder.getRandomLatinWords(new Random(), 32));
                        long messageId = EOI.insert(message, new AuditUser()
                        {
                            @Override
                            public String getId()
                            {
                                return String.valueOf(userId);
                            }

                            @Override
                            public String getIpAddress()
                            {
                                return "0";
                            }
                        });
                        message.setId(messageId);

                        chatRooms.get(message.getRoom()).add(message);
                        JsonObject addMessage = createAddMessage(message);
                        sendToSessions(addMessage, getSessionsInRoom(message.getRoomId()));
                    }
                }
            }
        }.start();
    }

    public static void handleSessionAttributeRemoved(UserSession userSession)
    {
        Session socketSession = ChatSessionHandler.userIdToSocketSession.get(userSession.getUserId());
        if (socketSession != null)
        {
            ChatSessionHandler.removeSession(socketSession, userSession.getUserId());
            ChatSessionHandler.announceStatusChange(socketSession, userSession.getUserId());
        }
    }

    public static void addSession(Session session, Long userId)
    {
        userIdToSocketSession.put(userId, session);
        socketSessionToUserId.put(session, userId);
        sessions.put(session, new SocketSessionInfo(null, new Date(), "active"));
    }

    public static void removeSession(Session session, Long userId)
    {
        userIdToSocketSession.remove(userId);
        socketSessionToUserId.remove(session);
        sessions.remove(session);
    }

    private static void sendToAllConnectedSessions(JsonObject message, ChatRoom room)
    {
        for (Session session : sessions.keySet())
        {
            if (sessions.get(session).getRoomId() != null && sessions.get(session).getRoomId().equals(room.getId()))
                sendToSession(session, message);
        }
    }

    private static List<Session> getSessionsInRoom(Long roomId)
    {
        return sessions.keySet().stream()
                .filter(session -> sessions.get(session).getRoomId() != null && sessions.get(session).getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }

    private static List<Session> getSessionsVisibleFromSession(Long senderUserId)
    {
        return sessions.keySet().stream()
                .filter(session1 -> {
                    Long receiverUserId = socketSessionToUserId.get(session1);
                    return User.getAllVisible(receiverUserId).contains(User.getByUserId(senderUserId));
                })
                .collect(Collectors.toList());
    }

    private static void sendToSessions(JsonObject message, List<Session> sessions)
    {
        for (Session session : sessions)
            sendToSession(session, message);
    }

    private static void sendToSession(Session session, JsonObject message)
    {
        try
        {
            session.getRemote().sendString(message.toString());
        }
        catch (IOException ex)
        {
            sessions.remove(session);
            log.error(ex.getMessage());
        }
    }

    //
    // logic for specific handlers
    //

    public static void changeRoom(Session session, ChatRoom room)
    {
        if (sessions.get(session) == null)
            return;
        
        sessions.get(session).setRoomId(room.getId());

        // tell client about messages in the room
        List<ChatRoomMessage> messagesForRoom = chatRooms.get(room);
        if (messagesForRoom != null)
        {
            List<ChatRoomMessage> messages = messagesForRoom;
            if (messages.size() > 50)
                messages = messages.subList(messages.size() - 51, messages.size() - 1);

            for (ChatRoomMessage chatRoomMessage : messages)
            {
                JsonObject addMessage = createAddMessage(chatRoomMessage);
                sendToSession(session, addMessage);
            }
        }

        // tell client about people in the room
        ChatRoomUserMap.getByRoomId(room.getId()).forEach(chatRoomUserMap -> {
            JsonObject addRoomMember = createAddRoomMember(User.getByUserId(chatRoomUserMap.getUserId()));
            sendToSession(session, addRoomMember);
        });
    }

    public static void changeToPrivateRoom(Session session, Long otherUserId)
    {
        if (sessions.get(session) == null)
            return;

        Long userId = socketSessionToUserId.get(session);
        AuditUser auditUser = new AuditUser()
        {
            @Override
            public String getId()
            {
                return String.valueOf(userId);
            }

            @Override
            public String getIpAddress()
            {
                return "0";
            }
        };
        
//        String query = "select * from chat_room_user_maps where user_id in (1,3) and room_id in (select room_id from chat_room_user_maps group by room_id having count(*) = 2);";
        ChatRoom directChatRoom = ChatRoom.getDirectChat(userId, otherUserId);
        // create room if it doesn't exist
        if (directChatRoom == null)
        {
            directChatRoom = new ChatRoom();
            directChatRoom.setName("private: " + userId + " and " + otherUserId);
            directChatRoom.setDirectChat(true);
            directChatRoom.setDirectChatUserId1(userId);
            directChatRoom.setDirectChatUserId2(otherUserId);
            Long directChatRoomId = EOI.insert(directChatRoom, auditUser);
            directChatRoom.setId(directChatRoomId);

            // load an empty list into a new entry in the chatRooms map
            List<ChatRoomMessage> roomMessages = new CopyOnWriteArrayList<>(ChatRoomMessage.getByRoomId(directChatRoom.getId()));
            chatRooms.put(directChatRoom, roomMessages);

            ChatRoomUserMap chatRoomUserMap = new ChatRoomUserMap();
            chatRoomUserMap.setRoomId(directChatRoomId);
            chatRoomUserMap.setUserId(userId);
            EOI.insert(chatRoomUserMap, auditUser);
            chatRoomUserMap.setUserId(otherUserId);
            EOI.insert(chatRoomUserMap, auditUser);
        }

        sessions.get(session).setRoomId(directChatRoom.getId());

        // tell client about messages in the room
        List<ChatRoomMessage> messagesForRoom = chatRooms.get(ChatRoom.getById(directChatRoom.getId()));
        if (messagesForRoom != null)
        {
            List<ChatRoomMessage> messages = messagesForRoom;
            if (messages.size() > 50)
                messages = messages.subList(messages.size() - 51, messages.size() - 1);

            for (ChatRoomMessage chatRoomMessage : messages)
            {
                JsonObject addMessage = createAddMessage(chatRoomMessage);
                sendToSession(session, addMessage);
            }
        }

        // tell client about people in the room
        ChatRoomUserMap.getByRoomId(directChatRoom.getId()).forEach(chatRoomUserMap -> {
            JsonObject addRoomMember = createAddRoomMember(User.getByUserId(chatRoomUserMap.getUserId()));
            sendToSession(session, addRoomMember);
        });
    }

    public static void sendRooms(Session session, Long userId)
    {
        List<Long> groupIds = Group.getAllVisible(userId).stream().map(Group::getId).collect(Collectors.toList());

        chatRooms.keySet().forEach(room -> {
            boolean hasAccess = false;

            if (room.getDirectChat())
                return;

            JsonObject addRoomMessage = createAddRoom(room);

            // does group membership grant access?
            if (groupIds.contains(room.getGroupId()))
                hasAccess = true;

            // are we already a member of the room?
            if (room.getDirectChat())
            {
                List<Long> userIds = ChatRoomUserMap.getByRoomId(room.getId()).stream().map(ChatRoomUserMap::getUserId).collect(Collectors.toList());
                if (userIds.contains(userId))
                    hasAccess = true;
            }

            if (hasAccess)
                sendToSession(session, addRoomMessage);
        });
    }

    public static void sendPeople(Session session, Long userId)
    {
        // tell client about people
        User.getAllVisible(userId)
                .forEach(visibleUser -> {
                    JsonObject addPerson = createAddPerson(visibleUser);
                    sendToSession(session, addPerson);
                });
    }

    public static void announceStatusChange(Session session, Long announcerUserId)
    {
        // announce changes to person's status change
        getSessionsVisibleFromSession(announcerUserId).forEach(receiverSession -> {
            // update the 'person' object
            JsonObject updatePerson = createUpdatePerson(User.getByUserId(announcerUserId));
            sendToSession(receiverSession, updatePerson);

            // update the 'roomMember' object
            JsonObject updateRoomMember = createUpdateRoomMember(User.getByUserId(announcerUserId), receiverSession);
            sendToSession(receiverSession, updateRoomMember);
        });
    }

    public static void addChatRoomMessage(ChatRoomMessage chatRoomMessage)
    {
        chatRooms.get(chatRoomMessage.getRoom()).add(chatRoomMessage);

        // tell room members about new message
        JsonObject addMessage = createAddMessage(chatRoomMessage);
        sendToSessions(addMessage, getSessionsInRoom(chatRoomMessage.getRoomId()));
    }

    private static JsonObject createAddMessage(ChatRoomMessage message)
    {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "addMessage")
                .add("id", message.getId())
                .add("timestamp", new SimpleDateFormat("h:mm a").format(message.getTimestamp()))
                .add("author", message.getAuthor())
                .add("avatarBase64", User.getByUserId(message.getUserId()).getAvatar().getBase64())
                .add("contents", message.getContents())
                .build();
    }

    private static JsonObject createAddRoomMember(User user)
    {
        String statusClass = "is-dark";
        Session session = userIdToSocketSession.get(user.getId());
        if (session != null)
        {
            SocketSessionInfo socketSessionInfo = sessions.get(session);
            if (socketSessionInfo != null)
                statusClass = socketSessionInfo.getStatusClass();
        }

        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "addRoomMember")
                .add("id", user.getId())
                .add("name", user.getName())
                .add("avatarBase64", user.getAvatar().getBase64())
                .add("logonid", user.getLogonId())
                .add("statusClass", statusClass)
                .build();
    }

    private static JsonObject createAddPerson(User user)
    {
        String statusClass = "is-dark";
        String statusIcon = "far";
        Session session = userIdToSocketSession.get(user.getId());
        if (session != null)
        {
            SocketSessionInfo socketSessionInfo = sessions.get(session);
            if (socketSessionInfo != null)
            {
                statusClass = socketSessionInfo.getStatusClass();
                statusIcon = socketSessionInfo.getStatusIcon();
            }
        }

        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "addPerson")
                .add("id", user.getId())
                .add("name", user.getName())
                .add("avatarBase64", user.getAvatar().getBase64())
                .add("logonid", user.getLogonId())
                .add("statusClass", statusClass)
                .add("statusIcon", statusIcon)
                .build();
    }

    private static JsonObject createUpdatePerson(User user)
    {
        String statusClass = "is-dark";
        String statusIcon = "far";
        Session session = userIdToSocketSession.get(user.getId());
        if (session != null)
        {
            SocketSessionInfo socketSessionInfo = sessions.get(session);
            if (socketSessionInfo != null)
            {
                statusClass = socketSessionInfo.getStatusClass();
                statusIcon = socketSessionInfo.getStatusIcon();
            }
        }

        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "updatePerson")
                .add("id", user.getId())
                .add("name", user.getName())
                .add("avatarBase64", user.getAvatar().getBase64())
                .add("logonid", user.getLogonId())
                .add("statusClass", statusClass)
                .add("statusIcon", statusIcon)
                .build();
    }

    private static JsonObject createUpdateRoomMember(User user, Session announcee)
    {
        String statusClass = "is-dark";
        Session session = userIdToSocketSession.get(user.getId());
        if (session != null)
        {
            SocketSessionInfo socketSessionInfo = sessions.get(session);
            if (socketSessionInfo != null)
                statusClass = socketSessionInfo.getStatusClass();
        }

        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "updateRoomMember")
                .add("id", user.getId())
                .add("name", user.getName())
                .add("avatarBase64", user.getAvatar().getBase64())
                .add("logonid", user.getLogonId())
                .add("statusClass", statusClass)
                .build();
    }

    private static JsonObject createAddRoom(ChatRoom room)
    {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "addRoom")
                .add("id", room.getId())
                .add("name", room.getName())
                .build();
    }
}