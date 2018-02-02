package net.ehicks.bts.handlers;

import net.ehicks.bts.util.CommonIO;
import net.ehicks.bts.routing.Route;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.Attachment;
import net.ehicks.bts.beans.DBFile;
import net.ehicks.bts.beans.Group;
import net.ehicks.bts.beans.IssueAudit;
import net.ehicks.bts.model.AttachmentLogic;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class AttachmentHandler
{
    private static final Logger log = LoggerFactory.getLogger(AttachmentHandler.class);

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "retrieveAttachment")
    public static void retrieveAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long attachmentId = Common.stringToLong(request.getParameter("attachmentId"));

        Attachment attachment = Attachment.getById(attachmentId);
        if (attachment != null)
        {
            Group issueGroup = attachment.getIssue().getGroup();
            if (!Group.getAllVisible(userSession.getUserId()).contains(issueGroup))
                return;

            DBFile dbFile = attachment.getDbFile();

            CommonIO.sendFileInResponse(response, dbFile, true);
        }
    }

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "addAttachment")
    public static void addAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String responseMessage = "";
        Long issueId = Common.stringToLong(request.getParameter("issueId"));
        long dbFileId = 0;

        for (FileItem fileItem : CommonIO.getFilesFromRequest(request))
        {
            if (!CommonIO.isValidSize(fileItem))
            {
                responseMessage = "File size too large.";
                continue;
            }

            String fileName = CommonIO.getName(fileItem);

            long thumbnailId = 0;
            if (CommonIO.isImage(fileItem))
            {
                byte[] scaledBytes = CommonIO.getThumbnail(fileItem);

                DBFile thumbnail = new DBFile(fileName, scaledBytes);
                thumbnailId = EOI.insert(thumbnail, userSession);
            }

            DBFile dbFile = new DBFile(fileName, fileItem.get());
            dbFile.setThumbnailId(thumbnailId);
            dbFileId = EOI.insert(dbFile, userSession);

            responseMessage = "Attachment added.";
        }

        if (dbFileId > 0)
        {
            Attachment attachment = new Attachment(issueId, dbFileId, userSession.getUserId());
            long attachmentId = EOI.insert(attachment, userSession);
            attachment = Attachment.getById(attachmentId);

            IssueAudit issueAudit = new IssueAudit(issueId, userSession, "added", attachment.toString());
            EOI.insert(issueAudit, userSession);
        }

        request.getSession().setAttribute("responseMessage", responseMessage);
        response.sendRedirect("view?tab1=issue&action=form&issueId=" + issueId);
    }

    @Route(tab1 = "issue", tab2 = "", tab3 = "", action = "deleteAttachment")
    public static void deleteAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long issueId = Common.stringToLong(request.getParameter("issueId"));
        Long attachmentId = Common.stringToLong(request.getParameter("attachmentId"));

        AttachmentLogic.deleteAttachment(userSession, attachmentId);

        response.sendRedirect("view?tab1=issue&action=form&issueId=" + issueId);
    }
}
