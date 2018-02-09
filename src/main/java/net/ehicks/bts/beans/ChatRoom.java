package net.ehicks.bts.beans;

import net.ehicks.eoi.EOI;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom implements Serializable
{
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "bigint not null auto_increment primary key")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "direct_chat", nullable = false)
    private Boolean directChat;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ChatRoom)) return false;
        ChatRoom that = (ChatRoom) obj;
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

    public static List<ChatRoom> getAll()
    {
        return EOI.executeQuery("select * from chat_rooms");
    }

    public static ChatRoom getByGroupId(Long groupId)
    {
        return EOI.executeQueryOneResult("select * from chat_rooms where group_id=?", Arrays.asList(groupId));
    }

    public static ChatRoom getById(Long id)
    {
        return EOI.executeQueryOneResult("select * from chat_rooms where id=?", Arrays.asList(id));
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Boolean getDirectChat()
    {
        return directChat;
    }

    public void setDirectChat(Boolean directChat)
    {
        this.directChat = directChat;
    }
}
