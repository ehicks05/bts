package net.ehicks.bts.beans

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
data class BtsSystem @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        var siteName: String = "BugCo Industries",
        var logonMessage: String = "Welcome!",
        @OneToOne(fetch = FetchType.LAZY) var defaultAvatar: Avatar = Avatar(),
        var theme: String = "",
        var emailFromAddress: String = "",
        var emailFromName: String = "noreply",
        var backupDir: String = "backups"
)

@Repository
interface BtsSystemRepository : JpaRepository<BtsSystem, Long> {
        fun findFirstBy(): BtsSystem
}