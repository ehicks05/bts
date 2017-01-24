package net.ehicks.bts;

import net.ehicks.bts.beans.DBFile;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

public class CommonIO
{
    public static void sendFileInResponse(HttpServletResponse response, File file) throws IOException
    {
        response.setContentType(URLConnection.guessContentTypeFromName(file.getName()));
        response.setHeader("Content-Disposition", String.format("inline; filename=%s", file.getName()));
        response.setContentLength(Long.valueOf(file.length()).intValue());

        InputStream inputStream = new FileInputStream(file);
        IOUtils.copy(inputStream, response.getOutputStream());
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(response.getOutputStream());
    }

    public static void sendFileInResponse(HttpServletResponse response, DBFile dbFile) throws IOException
    {
        response.setContentType(URLConnection.guessContentTypeFromName(dbFile.getName()));
        response.setHeader("Content-Disposition", String.format("inline; filename=%s", dbFile.getName()));
        response.setContentLength(Long.valueOf(dbFile.getContent().length).intValue());

        InputStream inputStream = new ByteArrayInputStream(dbFile.getContent());
        IOUtils.copy(inputStream, response.getOutputStream());
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(response.getOutputStream());
    }
}
