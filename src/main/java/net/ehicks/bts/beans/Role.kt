package net.ehicks.bts.beans

import net.ehicks.bts.ISelectTagSupport
import org.hibernate.envers.Audited
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.persistence.*

@Entity
@Audited
data class Role(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        @Column(unique = true)
        var role: String

) : GrantedAuthority, Serializable, ISelectTagSupport {
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    var users: Set<User> = HashSet()

    override fun getText(): String {
        return role
    }

    override fun getValue(): String {
        return id.toString()
    }

    override fun getAuthority(): String {
        return role
    }
}

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByRole(role: String): Role
    fun findByUsers_Username(userName: String): List<Role>
    fun findByRoleAndUsers_Id(role: String, userId: Long): Role
}