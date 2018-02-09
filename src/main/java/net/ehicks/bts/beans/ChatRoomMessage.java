package net.ehicks.bts.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "chat_room_messages")
public class ChatRoomMessage implements Serializable, Comparable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "time_stamp", nullable = false)
    private Date timestamp;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ChatRoomMessage)) return false;
        ChatRoomMessage that = (ChatRoomMessage) obj;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 17 * 37 * id.intValue();
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + ":" + id.toString();
    }

    @Override
    public int compareTo(Object o)
    {
        return this.getTimestamp().compareTo(((ChatRoomMessage) o).getTimestamp());
    }

    public ChatRoom getRoom()
    {
        return ChatRoom.getById(roomId);
    }

    public static List<ChatRoomMessage> getAll()
    {
        return EOI.executeQuery("select * from chat_room_messages");
    }

    public static List<ChatRoomMessage> getByRoomId(Long roomId)
    {
        return EOI.executeQuery("select * from chat_room_messages where room_id=?", Arrays.asList(roomId));
    }

    public static List<ChatRoomMessage> getByUserId(Long userId)
    {
        return EOI.executeQuery("select * from chat_room_messages where user_id=?", Arrays.asList(userId));
    }

    public static ChatRoomMessage getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from chat_room_messages where id=?", Arrays.asList(id));
    }

    public static ChatRoomMessage getByRoomIdAndUserId(Long roomId, Long userId)
    {
        return EOI.executeQueryOneResult("select * from chat_room_messages where room_id=? and user_id=?", Arrays.asList(roomId, userId));
    }

    // -------- Getters / Setters ----------

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getRoomId()
    {
        return roomId;
    }

    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}
