package net.ehicks.bts.handlers;

import net.ehicks.bts.CommonIO;
import net.ehicks.bts.SystemInfo;
import net.ehicks.bts.UserSession;
import net.ehicks.common.Common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LogHandler
{
    public static String showLogs(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<File> logs = new ArrayList<>(Arrays.asList(Paths.get("../logs").toFile().listFiles()));
        logs.removeIf(file -> !file.getName().contains("bts"));
        Collections.reverse(logs);
        request.setAttribute("logs", logs);

        return "/WEB-INF/webroot/admin/logs.jsp";
    }

    public static void deleteLog(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String logName = Common.getSafeString(request.getParameter("logName"));
        File file = new File(SystemInfo.INSTANCE.getLogDirectory() + logName);
        file.delete();
        response.sendRedirect("view?tab1=admin&tab2=logs&action=form");
    }

    public static void viewLog(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String logName = Common.getSafeString(request.getParameter("logName"));
        File file = new File(SystemInfo.INSTANCE.getLogDirectory() + logName);

        CommonIO.sendFileInResponse(response, file);
    }

    public static String viewLogPretty(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String logName = Common.getSafeString(request.getParameter("logName"));
        File file = new File(SystemInfo.INSTANCE.getLogDirectory() + logName);

        List<List<String>> lines = new ArrayList<>();
        for (String line : Files.readAllLines(file.toPath(), Charset.defaultCharset()))
        {
            try
            {
                parseLine(lines, line);
            }
            catch (Exception e)
            {
                parseLineRaw(lines, line);
            }
        }

        request.setAttribute("lines", lines);
        request.setAttribute("logName", logName);
        return "/WEB-INF/webroot/admin/viewLog.jsp";
    }

    private static void parseLine(List<List<String>> lines, String line)
    {
        String date = line.substring(0, 12);
        line = line.substring(13);

        int openCount = 1;
        int closeIndex = 1;
        while (openCount > 0)
        {
            if (line.charAt(closeIndex) == ']')
                openCount--;
            if (line.charAt(closeIndex) == '[')
                openCount++;
            closeIndex++;
        }
        String thread = line.substring(0, closeIndex + 1);
        String threadWithoutBrackets = thread.substring(1, thread.length() - 2);

        line = line.substring(closeIndex + 1);
        String level = line.substring(0, line.indexOf(" "));

        line = line.substring(line.indexOf(" ") + 2);
        String myClass = line.substring(0, line.indexOf(" "));
        String classWithoutPackage = myClass.substring(myClass.lastIndexOf(".") + 1);

        line = line.substring(line.indexOf(" ") + 4);
        String message = line;

        lines.add(Arrays.asList(date, threadWithoutBrackets, level, classWithoutPackage, message));
    }

    private static void parseLineRaw(List<List<String>> lines, String line)
    {
        lines.add(Arrays.asList("", "", "", "", line));
    }
}
