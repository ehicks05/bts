package net.ehicks.bts.beans

import net.ehicks.bts.ISelectTagSupport
import net.ehicks.bts.Named
import org.hibernate.envers.Audited
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.persistence.*

@Entity
@Audited
data class Severity(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        private val id: Long = 0,
        @Column(unique = true)
        private val name: String = ""
) : Serializable, ISelectTagSupport, Named {
    override fun getText(): String {
        return name
    }

    override fun getValue(): String {
        return id.toString()
    }

    override fun getId(): Long { return id}
    override fun getName(): String { return name}
}

@Repository
interface SeverityRepository : JpaRepository<Severity, Long> {
    fun findByName(name: String): Severity
    fun findByIdIn(ids: List<Long>): Set<Severity>
}