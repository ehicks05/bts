package net.ehicks.bts.beans

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Attachment(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        var name: String = "",
        @ManyToOne(fetch = FetchType.LAZY) val issue: Issue,
        @ManyToOne(fetch = FetchType.LAZY) val dbFile: DBFile,
        @CreatedDate
        val createdOn: LocalDateTime,
        @ManyToOne(fetch = FetchType.LAZY) val createdBy: User?
) {
    fun getShortName(): String {
        return if (name.length > 32) name.substring(0, 26) else name
    }
}

@Repository
interface AttachmentRepository : JpaRepository<Attachment, Long> {
    fun findByIssue_id(issueId: Long): List<Attachment>
    fun findByDbFile_id(dbFileId: Long): List<Attachment>
}