package net.ehicks.bts.beans

import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.persistence.*

@Entity
@Table(indexes = [
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

        @ManyToOne(fetch = FetchType.LAZY) var group: Group,
        @ManyToOne(fetch = FetchType.LAZY) var issueType: IssueType,
        @ManyToOne(fetch = FetchType.LAZY) var project: Project,
        @ManyToOne(fetch = FetchType.LAZY) var assignee: User,
        @ManyToOne(fetch = FetchType.LAZY) var reporter: User,
        @ManyToOne(fetch = FetchType.LAZY) var severity: Severity,
        @ManyToOne(fetch = FetchType.LAZY) var status: Status,

        @ManyToMany
        @JoinTable(name = "issue_watcher",
                joinColumns = [JoinColumn(name = "issue_id")],
                inverseJoinColumns = [JoinColumn(name = "watcher_id")])
        var watchers: Set<User> = HashSet()
) : Serializable {
    @OneToMany(mappedBy = "issue")
    @OrderBy("createdOn, id asc")
    var attachments: Set<Attachment> = HashSet()
    @OneToMany(mappedBy = "issue")
    @OrderBy("createdOn, id asc")
    var comments: Set<Comment> = HashSet()

    fun getTimeSinceCreation(): String {
        return getTimeSince(createdOn)
    }

    fun getTimeSinceUpdate(): String {
        return getTimeSince(lastUpdatedOn)
    }

    val key: String
        get() = project.prefix + "-" + id

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
interface IssueRepository : JpaRepository<Issue, Long> {
    fun findFirstByOrderById(): Issue
}