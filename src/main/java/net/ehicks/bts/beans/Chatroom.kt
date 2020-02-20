package net.ehicks.bts.beans

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.persistence.*

@Entity
data class ChatRoom(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        @Column(unique = true)
        var name: String = "",
        @ManyToOne var group: Group,
        var directChat: Boolean,
        @ManyToOne var directChatUser1: User,
        @ManyToOne var directChatUser2: User
) : Serializable

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
    fun findByGroup_id(groupId: Long): ChatRoom
    fun findByDirectChatUser1_idAndDirectChatUser2_id(directChatUserId1: Long, directChatUserId2: Long): ChatRoom
}