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
data class Project(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        private var id: Long = 0,
        @Column(unique = true)
        private var name: String = "",
        @Column(unique = true)
        var prefix: String = ""

) : Serializable, ISelectTagSupport, Named {
    @ManyToMany(mappedBy = "projects", fetch = FetchType.EAGER)
    var users: Set<User> = HashSet()

    override fun getText(): String {
        return name
    }

    override fun getValue(): String {
        return id.toString()
    }

    override fun getId(): Long { return id}
    override fun getName(): String { return name}
    fun setName(name: String) { this.name = name}
}

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {
    fun findByName(name: String): Project
    fun findByUsers_Id(userId: Long): List<Project>
    fun findByIdIn(ids: List<Long>): Set<Project>
}