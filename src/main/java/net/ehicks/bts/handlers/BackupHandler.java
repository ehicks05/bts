package net.ehicks.bts.handlers;

import net.ehicks.bts.BackupDbTask;
import net.ehicks.bts.CommonIO;
import net.ehicks.bts.SystemInfo;
import net.ehicks.bts.UserSession;
import net.ehicks.common.Common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BackupHandler
{
    public static String showBackups(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        File backupDir = new File(SystemInfo.INSTANCE.getBackupDirectory());
        List<File> backups = new ArrayList<>();
        if (backupDir.exists() && backupDir.isDirectory())
            backups = Arrays.asList(backupDir.listFiles());
        backups.removeIf(file -> !file.getName().contains("bts"));
        Collections.reverse(backups);
        request.setAttribute("backups", backups);

        return "/WEB-INF/webroot/admin/backups.jsp";
    }

    public static void createBackup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        BackupDbTask.backupToZip();

        response.sendRedirect("view?tab1=admin&tab2=backups&action=form");
    }

    public static void deleteBackup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String backupName = Common.getSafeString(request.getParameter("backupName"));
        File file = new File(SystemInfo.INSTANCE.getBackupDirectory() + backupName);
        boolean result = file.delete();
        response.sendRedirect("view?tab1=admin&tab2=backups&action=form");
    }

    public static void viewBackup(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String backupName = Common.getSafeString(request.getParameter("backupName"));
        File file = new File(SystemInfo.INSTANCE.getBackupDirectory() + backupName);

        CommonIO.sendFileInResponse(response, file, false);
    }
}
