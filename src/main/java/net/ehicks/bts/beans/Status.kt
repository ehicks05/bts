package net.ehicks.bts.beans

import net.ehicks.bts.ISelectTagSupport
import net.ehicks.bts.Named
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.persistence.*

@Entity
data class Status(
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
interface StatusRepository : JpaRepository<Status, Long> {
    fun findByName(name: String): Status
    fun findByIdIn(ids: List<Long>): Set<Status>
}