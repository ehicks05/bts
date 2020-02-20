package net.ehicks.bts.beans

import net.ehicks.bts.mail.EmailAction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.persistence.*

@Entity
data class EmailEvent(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        @ManyToOne var user: User,
        @ManyToOne var issue: Issue?,
        @ManyToOne var comment: Comment?,
        var actionId: Long,
        var status: String = "CREATED",
        var description: String = "",
        var toAddress: String = "",
        var previousValue: String = "",
        var newValue: String = ""

) : Serializable {
    fun getEmailAction(): EmailAction {
        return EmailAction.getById(actionId)
    }

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
        val verb = if (actionId == EmailAction.ADD_COMMENT.id)
            " added a comment to "
        else if (actionId == EmailAction.EDIT_COMMENT.id)
            " edited a comment on "
        else ""

        if (actionId == EmailAction.ADD_COMMENT.id || actionId == EmailAction.EDIT_COMMENT.id) {
            return user.username + verb + issue?.project?.prefix + "-" + issue?.id + " " + issue?.title
        }
        if (actionId == EmailAction.TEST.id)
            return "Test Email"

        return ""
    }

    fun getBody(): String {
        return "todo"
    }
}

@Repository
interface EmailEventRepository : JpaRepository<EmailEvent, Long>