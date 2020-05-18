package net.ehicks.bts.beans

import net.ehicks.bts.ISelectTagSupport
import net.ehicks.bts.Named
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "groups")
data class Group(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        private var id: Long = 0,
        @Column(nullable = false, unique = true)
        private var name: String = "",
        var admin: Boolean,
        var support: Boolean

) : Serializable, ISelectTagSupport, Named {
    @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER)
    var users: Set<User> = HashSet()

    override fun getValue(): String {
        return id.toString()
    }

    override fun getText(): String {
        return name
    }

    override fun getId(): Long { return id}
    override fun getName(): String { return name}
    fun setName(name: String) { this.name = name}
}

@Repository
interface GroupRepository : JpaRepository<Group, Long> {
    fun findByName(name: String): Group
    fun findByIdIn(ids: List<Long>): Set<Group>
}