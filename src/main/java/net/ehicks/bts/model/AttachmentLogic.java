package net.ehicks.bts.model;

import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.Attachment;
import net.ehicks.bts.beans.IssueAudit;
import net.ehicks.bts.util.Security;
import net.ehicks.eoi.EOI;

public class AttachmentLogic
{
    public static void deleteAttachment(UserSession userSession, Long attachmentId)
    {
        Attachment attachment = Attachment.getById(attachmentId);
        if (attachment == null)
            return;

        if (!Security.hasAccess(userSession, attachment.getIssue().getGroup()))
            return;

        EOI.executeDelete(attachment, userSession);

        IssueAudit issueAudit = new IssueAudit(attachment.getIssueId(), userSession, "removed", attachment.toString());
        EOI.insert(issueAudit, userSession);

        DBFileLogic.deleteDBFile(userSession, attachment.getDbFileId());
    }
}
