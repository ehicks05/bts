package net.ehicks.bts.handlers;

import net.ehicks.bts.CommonIO;
import net.ehicks.bts.SystemInfo;
import net.ehicks.bts.UserSession;
import net.ehicks.common.Common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogHandler
{
    public static String showLogs(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        List<File> logs = new ArrayList<>(Arrays.asList(Paths.get("../logs").toFile().listFiles()));
        logs.removeIf(file -> !file.getName().contains("bts"));
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
}
