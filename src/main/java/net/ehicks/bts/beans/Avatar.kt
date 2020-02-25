package net.ehicks.bts.beans

import net.ehicks.bts.ISelectTagSupport
import org.hibernate.envers.Audited
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Audited
data class Avatar @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        var name: String = "",
        @OneToOne(fetch = FetchType.LAZY) var dbFile: DBFile = DBFile(),
        var publicUse: Boolean = false
) : ISelectTagSupport {
    override fun getText(): String {
        return name
    }

    override fun getValue(): String {
        return dbFile.id.toString()
    }
}

@Repository
interface AvatarRepository: JpaRepository<Avatar, Long> {
    fun findAllByPublicUseTrue(): List<Avatar>
    fun findByDbFile_id(dbFileId: Long): List<Avatar>
    fun findByName(name: String): Avatar
}