package net.ehicks.bts.beans

import net.ehicks.bts.ISelectTagSupport
import net.ehicks.bts.Named
import org.hibernate.envers.Audited
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
@Table(name = "puffin_users")
@Audited
data class User @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        private val id: Long = 0,

        private var username: String,
        private var password: String,

        var firstName: String = "",
        var lastName: String = "",
        var enabled: Boolean = false,
        var createdOn: LocalDateTime = LocalDateTime.now(),
        @LastModifiedDate
        var lastUpdatedOn: LocalDateTime = LocalDateTime.now(),

        @ManyToOne(fetch = FetchType.LAZY) var avatar: Avatar,

        @ManyToMany
        @JoinTable(name = "user_projects",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "project_id", referencedColumnName = "id")])
        var projects: Set<Project> = HashSet(),

        @ManyToMany
        @JoinTable(name = "user_groups",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "group_id", referencedColumnName = "id")])
        var groups: Set<Group> = HashSet()

//        @OneToMany(mappedBy = "author")
//        var comments: Set<Comment> = HashSet()

): UserDetails, ISelectTagSupport, Named {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
    var roles: Set<Role> = HashSet()

    override fun getUsername(): String { return username}
    override fun getPassword(): String { return password}
    fun setUsername(username: String) { this.username = username}
    fun setPassword(password: String) { this.password = password}
    override fun getId(): Long { return id}
    override fun getName(): String { return username}

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return roles
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return enabled
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getValue(): String {
        return id.toString()
    }

    override fun getText(): String {
        return firstName + " " + lastName
    }

    val isAdmin: Boolean
        get() = roles.stream().anyMatch { role: Role -> role.role.equals("ROLE_ADMIN") }

    val isSupport: Boolean
        get() = roles.stream().anyMatch { role: Role -> role.role.equals("ROLE_SUPPORT") }

    val groupIds: List<Long>
        get() = groups.map { it.getId() }
    val projectIds: List<Long>
        get() = projects.map { it.getId() }
}

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
    fun findAllByOrderById(): List<User>
    fun findByIdIn(ids: List<Long>): Set<User>
    fun findByGroupsIn(ids: Set<Group>): Set<User>
}