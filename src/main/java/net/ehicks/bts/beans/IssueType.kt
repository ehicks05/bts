package net.ehicks.bts.beans

import net.ehicks.bts.ISelectTagSupport
import org.hibernate.envers.Audited
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.persistence.*

@Entity
@Audited
data class IssueType(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        @Column(unique = true)
        val name: String = ""
) : Serializable, ISelectTagSupport {
    override fun getText(): String {
        return name
    }

    override fun getValue(): String {
        return id.toString()
    }
}

@Repository
interface IssueTypeRepository : JpaRepository<IssueType, Long> {
    fun findByIdIn(ids: List<Long>): Set<IssueType>

}