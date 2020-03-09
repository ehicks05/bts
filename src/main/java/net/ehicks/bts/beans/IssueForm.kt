package net.ehicks.bts.beans

import net.ehicks.bts.model.SearchForm
import net.ehicks.bts.model.SearchResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
data class IssueForm @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        override var id: Long = 0,

        @ManyToOne var user: User,
        var formName: String = "New Form",
        var onDash: Boolean = false,
        var containsText: String = "",

        var fromCreatedDate: LocalDateTime? = null,
        var toCreatedDate: LocalDateTime? = null,

        var fromUpdatedDate: LocalDateTime? = null,
        var toUpdatedDate: LocalDateTime? = null,

        @ManyToMany
        @JoinTable(name = "issue_form_group",
                joinColumns = [JoinColumn(name = "issue_form_id")],
                inverseJoinColumns = [JoinColumn(name = "group_id")])
        var groups: Set<Group> = HashSet(),

        @ManyToMany
        @JoinTable(name = "issue_form_issue_type",
                joinColumns = [JoinColumn(name = "issue_form_id")],
                inverseJoinColumns = [JoinColumn(name = "issue_type_id")])
        var issueTypes: Set<IssueType> = HashSet(),

        @ManyToMany
        @JoinTable(name = "issue_form_project",
                joinColumns = [JoinColumn(name = "issue_form_id")],
                inverseJoinColumns = [JoinColumn(name = "project_id")])
        var projects: Set<Project> = HashSet(),

        @ManyToMany
        @JoinTable(name = "issue_form_assignee",
                joinColumns = [JoinColumn(name = "issue_form_id")],
                inverseJoinColumns = [JoinColumn(name = "assignee_id")])
        var assignees: Set<User> = HashSet(),

        @ManyToMany
        @JoinTable(name = "issue_form_reporter",
                joinColumns = [JoinColumn(name = "issue_form_id")],
                inverseJoinColumns = [JoinColumn(name = "reporter_id")])
        var reporters: Set<User> = HashSet(),

        @ManyToMany
        @JoinTable(name = "issue_form_severity",
                joinColumns = [JoinColumn(name = "issue_form_id")],
                inverseJoinColumns = [JoinColumn(name = "severity_id")])
        var severities: Set<Severity> = HashSet(),

        @ManyToMany
        @JoinTable(name = "issue_form_status",
                joinColumns = [JoinColumn(name = "issue_form_id")],
                inverseJoinColumns = [JoinColumn(name = "status_id")])
        var statuses: Set<Status> = HashSet(),

        override var sortColumn: String = "id",
        override var sortDirection: String = "asc"
) : SearchForm(), Serializable {
    override var endpoint: String = "/search/ajaxGetPageOfResults"

    @Transient
    var searchResult: SearchResult<Issue> =
            SearchResult(Collections.emptyList(), 0, 20, "1")
}

@Repository
interface IssueFormRepository : JpaRepository<IssueForm, Long> {
    fun findByUserId(userId: Long): List<IssueForm>
    fun findByUserIdAndOnDashTrueOrderById(userId: Long): List<IssueForm>
}