package net.ehicks.bts.beans

import org.hibernate.envers.Audited
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.persistence.*

@Entity
@Table(indexes = [
//    Index(name = "TRGM_IDX_ISSUE_TITLE", columnList = "title gin_trgm_ops"),
//    Index(name = "TRGM_IDX_ISSUE_DESCRIPTION", columnList = "description gin_trgm_ops"),
//    Index(name = "IDX_ISSUE_CREATED_ON", columnList = "created_on"),
//    Index(name = "IDX_ISSUE_LAST_UPDATED_ON", columnList = "last_updated_on"),
    Index(name = "IDX_ISSUE_TITLE", columnList = "title"),
    Index(name = "IDX_ISSUE_DESCRIPTION", columnList = "description"),
    Index(name = "IDX_ISSUE_GROUP_ID", columnList = "group_id"),
    Index(name = "IDX_ISSUE_ISSUE_TYPE_ID", columnList = "issue_type_id"),
    Index(name = "IDX_ISSUE_PROJECT_ID", columnList = "project_id"),
    Index(name = "IDX_ISSUE_ASSIGNEE_USER_ID", columnList = "assignee_id"),
    Index(name = "IDX_ISSUE_REPORTER_USER_ID", columnList = "reporter_id"),
    Index(name = "IDX_ISSUE_SEVERITY_ID", columnList = "severity_id"),
    Index(name = "IDX_ISSUE_STATUS_ID", columnList = "status_id")
])
@Audited(withModifiedFlag = true)
data class Issue @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        @Column(length = 2000)
        var title: String = "",
        @Column(length=10485760)
        var description: String = "",
        var createdOn: LocalDateTime = LocalDateTime.now(),
        @LastModifiedDate
        var lastUpdatedOn: LocalDateTime = LocalDateTime.now(),

        @ManyToOne var group: Group,
        @ManyToOne var issueType: IssueType,
        @ManyToOne var project: Project,
        @ManyToOne var assignee: User,
        @ManyToOne var reporter: User,
        @ManyToOne var severity: Severity,
        @ManyToOne var status: Status,

        @ManyToMany
        @JoinTable(name = "issue_watcher",
                joinColumns = [JoinColumn(name = "issue_id")],
                inverseJoinColumns = [JoinColumn(name = "watcher_id")])
        var watchers: Set<User> = HashSet()
) : Serializable {
    @OneToMany(mappedBy = "issue")
    var attachments: Set<Attachment> = HashSet()
    @OneToMany(mappedBy = "issue")
    @OrderBy("createdOn asc")
    var comments: Set<Comment> = HashSet()

    fun getTimeSinceCreation(): String {
        return getTimeSince(createdOn)
    }

    fun getTimeSinceUpdate(): String {
        return getTimeSince(lastUpdatedOn)
    }

    private fun getTimeSince(since: LocalDateTime): String {
        val now: LocalDateTime = LocalDateTime.now()
        val chronos = ChronoUnit.values()
                .slice(ChronoUnit.MILLIS.ordinal..ChronoUnit.YEARS.ordinal)
                .filterNot { it == ChronoUnit.HALF_DAYS }
                .reversed()
        for (chrono in chronos)
        {
            val amount = chrono.between(since, now)
            if (amount > 0)
            {
                var unit = chrono.toString()
                if (amount == 1L) unit = unit.dropLast(1)
                return "$amount $unit"
            }
        }
        return "undefined"
    }
}

@Repository
interface IssueRepository : JpaRepository<Issue, Long>, RevisionRepository<Issue, Long, Int> {
    fun findFirstByOrderById(): Issue
}