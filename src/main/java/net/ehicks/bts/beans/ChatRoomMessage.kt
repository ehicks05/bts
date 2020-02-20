//package net.ehicks.bts.beans
//
//import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.stereotype.Repository
//import java.io.Serializable
//import java.time.LocalDateTime
//import javax.persistence.Entity
//import javax.persistence.GeneratedValue
//import javax.persistence.Id
//
//@Entity
//data class ChatRoomMessage(
//        @Id @GeneratedValue(strategy = GenerationType.AUTO)
//        val id: Long = 0,
//        val roomId: Long,
//        val userId: Long,
//        val author: String = "",
//        val contents: String = "",
//        val createdOn: LocalDateTime
//) : Serializable, Comparable<ChatRoomMessage> {
//    override operator fun compareTo(other: ChatRoomMessage): Int {
//        return this.createdOn.compareTo(other.createdOn)
//    }
//}
//
//@Repository
//interface ChatRoomMessageRepository : JpaRepository<ChatRoomMessage, Long> {
//    fun findByRoomId(roomId: Long): List<ChatRoomMessage>
//    fun findByUserId(userId: Long): List<ChatRoomMessage>
//    fun findByRoomIdAndUserId(roomId: Long, userId: Long): List<ChatRoomMessage>
//}