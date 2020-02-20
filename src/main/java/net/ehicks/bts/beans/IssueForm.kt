package net.ehicks.bts.beans

import net.ehicks.bts.model.SearchForm
import net.ehicks.bts.model.SearchResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet
import kotlin.jvm.Transient

@Entity
data class IssueForm @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        override var id: Long = 0,

        @ManyToOne var user: User,
        var formName: String = "",
        var onDash: Boolean = false,
        var containsText: String = "",
        var title: String = "",
        var description: String = "",
        @ManyToOne var issue: Issue? = null,

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
        var statuses: Set<Status> = HashSet()

) : SearchForm(), Serializable {
    override val endpoint: String
        get() = "/search/ajaxGetPageOfResults"

    @Transient
    var searchResult: SearchResult<Issue> = SearchResult(Collections.emptyList(), 0, 20, "1")

    fun getProjectIds(): List<Long> {return projects.map { it.getId() }}
    fun getGroupIds(): List<Long> {return groups.map { it.getId() }}
    fun getSeverityIds(): List<Long> {return severities.map { it.getId() }}
    fun getStatusIds(): List<Long> {return statuses.map { it.getId() }}
    fun getAssigneeIds(): List<Long> {return assignees.map { it.getId() }}
}

@Repository
interface IssueFormRepository : JpaRepository<IssueForm, Long> {
    fun findByUserId(userId: Long): List<IssueForm>
    fun findByUserIdAndOnDashTrue(userId: Long): List<IssueForm>
}