package net.ehicks.bts;

import net.ehicks.bts.beans.DBFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CommonIO
{
    private static final Logger log = LoggerFactory.getLogger(CommonIO.class);

    public static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (Exception e)
        {
            // do nothing
        }
    }

    public static void sendFileInResponse(HttpServletResponse response, File file, boolean inline) throws IOException
    {
        response.setContentType(URLConnection.guessContentTypeFromName(file.getName()));
        String contentDisposition = inline ? "inline" : "attachment";
        response.setHeader("Content-Disposition", String.format(contentDisposition + "; filename=%s", file.getName()));
        response.setContentLength(Long.valueOf(file.length()).intValue());

        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream())
        {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    public static void sendFileInResponse(HttpServletResponse response, DBFile dbFile, boolean inline) throws IOException
    {
        response.setContentType(URLConnection.guessContentTypeFromName(dbFile.getName()));
        String contentDisposition = inline ? "inline" : "attachment";
        response.setHeader("Content-Disposition", String.format(contentDisposition + "; filename=%s", dbFile.getName()));
        response.setContentLength(Long.valueOf(dbFile.getContent().length).intValue());

        try (InputStream inputStream = new ByteArrayInputStream(dbFile.getContent());
             OutputStream outputStream = response.getOutputStream())
        {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    public static List<FileItem> getFilesFromRequest(HttpServletRequest request)
    {
        List<FileItem> fileItems = new ArrayList<>();
        String responseMessage = "";
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
                    if (fileItem.getSize() > 1 * 1024 * 1024) // up to 1MB
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

                    if (!contentType.startsWith("image"))
                    {
                        responseMessage = "Not an image.";
                        continue;
                    }
                }
            }
            catch (FileUploadException e)
            {
                log.error(e.getMessage(), e);
            }
        }

        return fileItems;
    }
}
