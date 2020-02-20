package net.ehicks.bts.beans

import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Audited
data class Attachment(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        var name: String = "",
        @ManyToOne val issue: Issue,
        @ManyToOne val dbFile: DBFile,
        @CreatedDate
        val createdOn: LocalDateTime
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