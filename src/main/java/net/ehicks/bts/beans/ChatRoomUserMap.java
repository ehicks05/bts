package net.ehicks.bts.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "chat_room_user_maps")
public class ChatRoomUserMap implements Serializable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "room_id", nullable = false, unique = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false, unique = false)
    private Long userId;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ChatRoomUserMap)) return false;
        ChatRoomUserMap that = (ChatRoomUserMap) obj;
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

    public static List<ChatRoomUserMap> getAll()
    {
        return EOI.executeQuery("select * from chat_room_user_maps");
    }

    public static List<ChatRoomUserMap> getByRoomId(Long roomId)
    {
        return EOI.executeQuery("select * from chat_room_user_maps where room_id=?", Arrays.asList(roomId));
    }

    public static List<ChatRoomUserMap> getByUserId(Long userId)
    {
        return EOI.executeQuery("select * from chat_room_user_maps where user_id=?", Arrays.asList(userId));
    }

    public static ChatRoomUserMap getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from chat_room_user_maps where id=?", Arrays.asList(id));
    }

    public static ChatRoomUserMap getByRoomIdAndUserId(Long roomId, Long userId)
    {
        return EOI.executeQueryOneResult("select * from chat_room_user_maps where room_id=? and user_id=?", Arrays.asList(roomId, userId));
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
}
