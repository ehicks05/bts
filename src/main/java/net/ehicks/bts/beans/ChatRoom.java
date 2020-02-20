//package net.ehicks.bts.beans;
//
//import net.ehicks.eoi.EOI;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.List;
//
//@Entity
//@Table(name = "chat_rooms")
//public class ChatRoom implements Serializable
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    private Long groupId;
//
//    @Column(nullable = false)
//    private Boolean directChat;
//
//    private Long directChatUserId1;
//    private Long directChatUserId2;
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof ChatRoom)) return false;
//        ChatRoom that = (ChatRoom) obj;
//        return this.id.equals(that.getId());
//    }
//
//    @Override
//    public int hashCode()
//    {
//        return 17 * 37 * id.intValue();
//    }
//
//    public String toString()
//    {
//        return this.getClass().getSimpleName() + ":" + id.toString();
//    }
//
//    public static List<ChatRoom> getAll()
//    {
//        return EOI.executeQuery("select * from chat_rooms");
//    }
//
//    public static ChatRoom getByGroupId(Long groupId)
//    {
//        return EOI.executeQueryOneResult("select * from chat_rooms where group_id=?", Arrays.asList(groupId));
//    }
//
//    public static ChatRoom getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from chat_rooms where id=?", Arrays.asList(id));
//    }
//
//    public static ChatRoom getDirectChat(Long userId1, Long userId2)
//    {
//        return EOI.executeQueryOneResult("select * from chat_rooms where direct_chat=? and direct_chat_user_id1=? and direct_chat_user_id2=?", Arrays.asList(true, userId1, userId2));
//    }
//
//    // -------- Getters / Setters ----------
//
//    public Long getId()
//    {
//        return id;
//    }
//
//    public void setId(Long id)
//    {
//        this.id = id;
//    }
//
//    public String getName()
//    {
//        return name;
//    }
//
//    public void setName(String name)
//    {
//        this.name = name;
//    }
//
//    public Long getGroupId()
//    {
//        return groupId;
//    }
//
//    public void setGroupId(Long groupId)
//    {
//        this.groupId = groupId;
//    }
//
//    public Boolean getDirectChat()
//    {
//        return directChat;
//    }
//
//    public void setDirectChat(Boolean directChat)
//    {
//        this.directChat = directChat;
//    }
//
//    public Long getDirectChatUserId1()
//    {
//        return directChatUserId1;
//    }
//
//    public void setDirectChatUserId1(Long directChatUserId1)
//    {
//        this.directChatUserId1 = directChatUserId1;
//    }
//
//    public Long getDirectChatUserId2()
//    {
//        return directChatUserId2;
//    }
//
//    public void setDirectChatUserId2(Long directChatUserId2)
//    {
//        this.directChatUserId2 = directChatUserId2;
//    }
//}
