package net.ehicks.bts.beans

import com.sksamuel.diffpatch.DiffMatchPatch
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
data class IssueEvent @JvmOverloads constructor(
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

    fun getRenderMap(): RenderMap {
        val emailContext = "http://localhost:8082" // todo figure this out

        val titleLink = emailContext + "/issue/form?issueId=" + issue.id
        val titleText = issue.key + " " + issue.title
        val userAvatarSource = emailContext + "/avatar/" + user.avatar.id
        val userProfileLink = emailContext + "/profile/form?profileUserId=" + user.getId()
        val userProfileText = user.username
        val summary = eventType.verb + " " + propertyName + ":"

        val diffs = mutableListOf<Diff>()
        if (eventType === EventType.ADD) diffs.add(Diff(DiffMatchPatch.Operation.INSERT, newValue))
        if (eventType === EventType.UPDATE) {
            if (listOf("title", "description", "comment").contains(propertyName)) {
                val diffMatchPatch = DiffMatchPatch()
                val updateDiffs = diffMatchPatch.diff_main(oldValue, newValue)
                diffMatchPatch.diff_cleanupSemantic(updateDiffs)
                diffs.addAll(updateDiffs.map { Diff(it.operation, it.text) })
            } else {
                diffs.add(Diff(DiffMatchPatch.Operation.DELETE, oldValue))
                diffs.add(Diff(DiffMatchPatch.Operation.INSERT, newValue))
            }
        }
        if (eventType === EventType.REMOVE) diffs.add(Diff(DiffMatchPatch.Operation.DELETE, oldValue))

        return RenderMap(
                titleLink,
                titleText,
                userAvatarSource,
                userProfileLink,
                userProfileText,
                summary,
                diffs
        )
    }
}

class RenderMap(
        val titleLink: String,
        val titleText: String,
        val userAvatarSource: String,
        val userProfileLink: String,
        val userProfileText: String,
        val summary: String,
        val diffs: List<Diff>
)

class Diff(
        val operation: DiffMatchPatch.Operation,
        val text: String
)

@Repository
interface IssueEventRepository : JpaRepository<IssueEvent, Long> {
    fun findByIssueId(issueId: Long): List<IssueEvent>
}