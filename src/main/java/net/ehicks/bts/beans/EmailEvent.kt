package net.ehicks.bts.beans

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

enum class EventType(val verb: String) {
    ADD("added"),
    UPDATE("updated"),
    REMOVE("removed")
}

@Entity
data class EmailEvent @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        @ManyToOne var user: User,
        @ManyToOne var issue: Issue,

        @Enumerated(EnumType.STRING)
        var eventType: EventType,
        var propertyName: String = "",

        var oldValue: String = "",
        var newValue: String = "",

        @ManyToOne var comment: Comment? = null,
        var createdOn: LocalDateTime = LocalDateTime.now(),
        var description: String = "",
        var status: String = "CREATED"

) : Serializable {
    fun getStatusIcon(): String {
        val statusToIcon = mapOf(
                Pair("CREATED", "hourglass-start has-text-info"),
                Pair("WAITING", "hourglass-half has-text-warning"),
                Pair("SENT", "check has-text-success"),
                Pair("FAILED", "ban has-text-danger")
        )
        return statusToIcon.getOrDefault(status, "question is-warning")
    }

    fun getSubject(): String {
        val verb = "${propertyName.capitalizeWords()} ${eventType.verb.capitalize()}"
        val issueId = issue.project.prefix + "-" + issue.id
        return "$verb ($issueId): ${issue.title}"
    }

    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }
}

@Repository
interface EmailEventRepository : JpaRepository<EmailEvent, Long>