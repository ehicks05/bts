package net.ehicks.bts.beans

import net.ehicks.bts.Named
import org.hibernate.envers.Audited
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.persistence.*

@Entity
@Audited
data class Subscription @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @ManyToOne
        var user: User,

        @ManyToMany
        @JoinTable(name = "subscription_group",
                joinColumns = [JoinColumn(name = "subscription_id")],
                inverseJoinColumns = [JoinColumn(name = "group_id")]
        )
        var groups: Set<Group> = HashSet(),

        @ManyToMany
        @JoinTable(name = "subscription_issue_type",
                joinColumns = [JoinColumn(name = "subscription_id")],
                inverseJoinColumns = [JoinColumn(name = "issue_type_id")]
        )
        var issueTypes: Set<IssueType> = HashSet(),

        @ManyToMany
        @JoinTable(name = "subscription_project",
                joinColumns = [JoinColumn(name = "subscription_id")],
                inverseJoinColumns = [JoinColumn(name = "project_id")]
        )
        var projects: Set<Project> = HashSet(),

        @ManyToMany
        @JoinTable(name = "subscription_assignee",
                joinColumns = [JoinColumn(name = "subscription_id")],
                inverseJoinColumns = [JoinColumn(name = "user_id")]
        )
        var assignees: Set<User> = HashSet(),

        @ManyToMany
        @JoinTable(name = "subscription_reporter",
                joinColumns = [JoinColumn(name = "subscription_id")],
                inverseJoinColumns = [JoinColumn(name = "user_id")]
        )
        var reporters: Set<User> = HashSet(),

        @ManyToMany
        @JoinTable(name = "subscription_severity",
                joinColumns = [JoinColumn(name = "subscription_id")],
                inverseJoinColumns = [JoinColumn(name = "severity_id")]
        )
        var severities: Set<Severity> = HashSet(),

        @ManyToMany
        @JoinTable(name = "subscription_status",
                joinColumns = [JoinColumn(name = "subscription_id")],
                inverseJoinColumns = [JoinColumn(name = "status_id")]
        )
        var statuses: Set<Status> = HashSet()


) : Serializable {
    fun getDescription(): String {
        var description = ""

        description = buildClause(description, reporters, " were reported by ")
        description = buildClause(description, assignees, " are assigned to ")
        description = buildClause(description, groups, " are in group ")
        description = buildClause(description, projects, " are in project ")
        description = buildClause(description, severities, " have a severity of ")
        description = buildClause(description, statuses, " have a status of ")

        return "Subscribe to issues that $description."
    }

    private fun buildClause(description: String, items: Set<Named>, clauseText: String): String {
        var desc = description
        if (items.isEmpty()) return desc
        if (desc.isNotEmpty()) desc += " AND "
        desc += clauseText
        var clause = ""
        for (item in items) {
            if (clause.isNotEmpty()) {
                val isLast = items.indexOf(item) == items.size - 1
                val isTwo = items.size == 2
                if (isLast) {
                    if (!isTwo) clause += ","
                    clause += " or "
                } else clause += ", "
            }
            clause += item.getName()
        }
        desc += clause
        return desc
    }
}

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, Long> {
    fun findByUser_Id(userId: Long): List<Subscription>
}