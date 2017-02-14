package net.ehicks.bts.handlers;

import net.ehicks.bts.CommonIO;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.Attachment;
import net.ehicks.bts.beans.DBFile;
import net.ehicks.bts.beans.Group;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AttachmentHandler
{
    private static final Logger log = LoggerFactory.getLogger(AttachmentHandler.class);

    public static void retrieveAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long attachmentId = Common.stringToLong(request.getParameter("attachmentId"));

        Attachment attachment = Attachment.getById(attachmentId);
        if (attachment != null)
        {
            Group issueGroup = attachment.getIssue().getGroup();
            if (!Group.getAllForUser(userSession.getUserId()).contains(issueGroup))
                return;

            DBFile dbFile = attachment.getDbFile();

            CommonIO.sendFileInResponse(response, dbFile, true);
        }
    }

    public static void addAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String responseMessage = "";
        Long issueId = Common.stringToLong(request.getParameter("issueId"));
        long dbFileId = 0;
        long thumbnailId = 0;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart)
        {
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Configure a repository (to ensure a secure temp location is used)
            ServletContext servletContext = request.getServletContext();
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            try
            {
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem fileItem : items)
                {
                    if (fileItem.getSize() > 10 * 1024 * 1024) // up to 10MB
                    {
                        responseMessage = "File size too large.";
                        continue;
                    }
                    
                    byte[] fileContents = fileItem.get();
                    String fileName = fileItem.getName();
                    if (fileName != null)
                        fileName = FilenameUtils.getName(fileName);

                    String contentType = fileItem.getContentType();
                    if (contentType.length() == 0)
                        contentType = URLConnection.guessContentTypeFromName(fileName);

                    if (Arrays.asList("image/bmp", "image/gif", "image/jpeg", "image/png").contains(contentType))
                    {
                        BufferedImage srcImage = ImageIO.read(fileItem.getInputStream()); // Load image
                        Scalr.Mode mode = srcImage.getWidth() > srcImage.getHeight() ? Scalr.Mode.FIT_TO_WIDTH : Scalr.Mode.FIT_TO_HEIGHT;
                        BufferedImage scaledImage = Scalr.resize(srcImage, mode, 200, 200); // Scale image
                        String formatName = contentType.replace("image/", "");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ImageIO.write(scaledImage, formatName, byteArrayOutputStream);
                        byte[] scaledBytes = byteArrayOutputStream.toByteArray();

                        DBFile dbFile = new DBFile();
                        dbFile.setName(fileName);
                        dbFile.setContent(scaledBytes);
                        dbFile.setLength((long) scaledBytes.length);
                        thumbnailId = EOI.insert(dbFile);
                    }

                    DBFile dbFile = new DBFile();
                    dbFile.setName(fileName);
                    dbFile.setContent(fileContents);
                    dbFile.setLength(fileItem.getSize());
                    dbFileId = EOI.insert(dbFile);

                    responseMessage = "Attachment added.";
                }
            }
            catch (FileUploadException e)
            {
                log.error(e.getMessage(), e);
            }
        }

        if (dbFileId > 0)
        {
            Attachment attachment = new Attachment();
            attachment.setIssueId(issueId);
            attachment.setDbFileId(dbFileId);
            attachment.setThumbnailDbFileId(thumbnailId);
            attachment.setCreatedByUserId(userSession.getUserId());
            attachment.setCreatedOn(new Date());
            EOI.insert(attachment);
        }

        request.getSession().setAttribute("responseMessage", responseMessage);
        response.sendRedirect("view?tab1=main&tab2=issue&action=form&issueId=" + issueId);
    }

    public static void deleteAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long issueId = Common.stringToLong(request.getParameter("issueId"));
        Long attachmentId = Common.stringToLong(request.getParameter("attachmentId"));

        Attachment attachment = Attachment.getById(attachmentId);
        if (attachment == null)
            return;

        Group issueGroup = attachment.getIssue().getGroup();
        if (!userSession.getUser().getAllGroups().contains(Group.getByName("Admin")) && !userSession.getUser().getAllGroups().contains(issueGroup))
            return;

        EOI.executeDelete(attachment);

        DBFile dbFile = attachment.getDbFile();
        if (dbFile == null)
            return;

        // check references
        List<Attachment> attachments = Attachment.getByDbFileId(dbFile.getId());
        if (attachments.size() > 0)
            return;

        EOI.executeDelete(dbFile);

        DBFile thumbNail = attachment.getThumbnailDbFile();
        if (thumbNail != null)
            EOI.executeDelete(thumbNail);

        response.sendRedirect("view?tab1=main&tab2=issue&action=form&issueId=" + issueId);
    }
}
