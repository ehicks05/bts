package net.ehicks.bts.beans

import net.ehicks.common.Common
import org.hibernate.envers.Audited
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Entity
@Audited
data class DBFile @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        val content: ByteArray = ByteArray(0),
        val hash: Int = 0,
        val mediaType: String = "",
        @OneToOne(fetch = FetchType.LAZY) val thumbnail: DBFile? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DBFile

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun getLengthPretty(): String {
        return Common.toMetric(content.size.toLong())
    }

    fun getPreviewIcon(): String {
        val iconClasses = mapOf(
                // Media
                Pair("image", "fa-file-image"),
                Pair("audio", "fa-file-audio"),
                Pair("video", "fa-file-video"),
                // Documents
                Pair("application/pdf", "fa-file-pdf"),
                Pair("application/msword", "fa-file-word"),
                Pair("application/vnd.ms-word", "fa-file-word"),
                Pair("application/vnd.oasis.opendocument.text", "fa-file-word"),
                Pair("application/vnd.openxmlformatsfficedocument.wordprocessingml", "fa-file-word"),
                Pair("application/vnd.ms-excel", "fa-file-excel"),
                Pair("application/vnd.openxmlformatsfficedocument.spreadsheetml", "fa-file-excel"),
                Pair("application/vnd.oasis.opendocument.spreadsheet", "fa-file-excel"),
                Pair("application/vnd.ms-powerpoint", "fa-file-powerpoint"),
                Pair("application/vnd.openxmlformatsfficedocument.presentationml", "fa-file-powerpoint"),
                Pair("application/vnd.oasis.opendocument.presentation", "fa-file-powerpoint"),
                Pair("text/plain", "fa-file-text"),
                Pair("text/html", "fa-file-code"),
                Pair("application/json", "fa-file-code"),
                // Archives
                Pair("application/gzip", "fa-file-archive"),
                Pair("application/zip", "fa-file-archive")
        )

        return iconClasses[mediaType]?:"fa-file"
    }
}

@Repository
interface DBFileRepository : JpaRepository<DBFile, Long> {
    fun findByHash(hash: Int): Optional<DBFile>
}