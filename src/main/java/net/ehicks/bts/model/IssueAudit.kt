package net.ehicks.bts.model

import net.ehicks.bts.MyRevisionEntity
import net.ehicks.bts.beans.Issue
import net.ehicks.bts.beans.User
import org.hibernate.envers.RevisionType

class IssueAudit(
        val user: User?,
        val before: Issue?,
        val after: Issue,
        val myRevisionEntity: MyRevisionEntity,
        val revisionType: RevisionType,
        val changedProperties: HashSet<String>
) {
    fun getDescription(): String {
        return ""
    }
}