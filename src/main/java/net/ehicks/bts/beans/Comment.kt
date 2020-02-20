package net.ehicks.bts.beans

import org.hibernate.envers.Audited
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(indexes = [
//    Index(name = "IDX_COMMENT_CREATED_ON", columnList = "created_on desc", unique = false),
    Index(name = "IDX_COMMENT_ISSUE_ID", columnList = "issue_id", unique = false)
])
@Audited
data class Comment(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        @ManyToOne var issue: Issue,
        @ManyToOne var author: User,
        @Column(length=10485760)
        var content: String = "",
        @OneToOne var visibleToGroup: Group,
        var createdOn: LocalDateTime = LocalDateTime.now(),
        var lastUpdatedOn: LocalDateTime = LocalDateTime.now()
)

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findTop10ByAuthorOrderByCreatedOnDesc(author: User): List<Comment>
}