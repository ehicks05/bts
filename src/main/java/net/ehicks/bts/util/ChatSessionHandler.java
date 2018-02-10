package net.ehicks.bts.util;

import net.ehicks.bts.beans.*;
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

    public static final Map<Session, ChatRoom> sessions = new ConcurrentHashMap<>();
    public static final Map<ChatRoom, List<ChatRoomMessage>> chatRooms = new ConcurrentHashMap<>();

    public static void init()
    {
        List<ChatRoom> rooms = ChatRoom.getAll();
        for (ChatRoom room : rooms)
        {
            chatRooms.put(room, new CopyOnWriteArrayList<>(ChatRoomMessage.getByRoomId(room.getId())));
            
        }

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
                        message.setContents(Comment.getAll().get(new Random().nextInt(Comment.getAll().size())).getContent());
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
                        sendToAllConnectedSessions(addMessage, message.getRoom());
                    }
                }
            }
        }.start();
    }

    public static void addSession(Session session)
    {
        sessions.put(session, new ChatRoom());
    }

    public static void removeSession(Session session)
    {
        sessions.remove(session);
    }

    private static void sendToAllConnectedSessions(JsonObject message, ChatRoom room)
    {
        for (Session session : sessions.keySet())
        {
            if (sessions.get(session).getId() != null && sessions.get(session).equals(room))
                sendToSession(session, message);
        }
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
        sessions.put(session, room);

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

    public static void sendRooms(Session session, Long userId)
    {
        List<Long> groupIds = Group.getAllVisible(userId).stream().map(Group::getId).collect(Collectors.toList());

        chatRooms.keySet().forEach(room -> {
            boolean hasAccess = false;

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

    public static void addChatRoomMessage(ChatRoomMessage chatRoomMessage)
    {
        chatRooms.get(chatRoomMessage.getRoom()).add(chatRoomMessage);

        // tell room members about new message
        JsonObject addMessage = createAddMessage(chatRoomMessage);
        sendToAllConnectedSessions(addMessage, chatRoomMessage.getRoom());
    }

    private static JsonObject createAddMessage(ChatRoomMessage message)
    {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "addMessage")
                .add("id", message.getId())
                .add("timestamp", new SimpleDateFormat("h:mm a").format(message.getTimestamp()))
                .add("author", message.getAuthor())
                .add("contents", message.getContents())
                .build();
    }

    private static JsonObject createAddRoomMember(User user)
    {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "addRoomMember")
                .add("id", user.getId())
                .add("name", user.getName())
                .add("logonid", user.getLogonId())
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