package net.ehicks.bts.beans

import net.ehicks.bts.model.SearchForm
import net.ehicks.bts.model.SearchResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
data class IssueEventForm @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        override var id: Long = 0,

        @ManyToOne(fetch = FetchType.LAZY) var user: User,
        var issueId: Long? = null,
        var formName: String = "New Form",
        var onDash: Boolean = false,
        var propertyName: String = "",
        @ManyToOne(fetch = FetchType.LAZY) var eventUser: User? = null,
        var fromEventDate: LocalDateTime? = null,
        var toEventDate: LocalDateTime? = null,

        override var sortColumn: String = "eventDate",
        override var sortDirection: String = "desc"
) : SearchForm(), Serializable {
    override val endpoint: String
        get() = "/admin/audit/ajaxGetPageOfResults"

    @Transient
    var searchResult: SearchResult<IssueEvent> =
            SearchResult(Collections.emptyList(), 0, 20, "1")
}

@Repository
interface IssueEventFormRepository : JpaRepository<IssueEventForm, Long> {
    fun findByUserId(userId: Long): List<IssueEventForm>
    fun findByUserIdAndOnDashTrue(userId: Long): List<IssueEventForm>
}