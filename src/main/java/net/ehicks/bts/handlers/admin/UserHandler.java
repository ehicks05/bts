package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.util.CommonIO;
import net.ehicks.bts.util.PdfCreator;
import net.ehicks.bts.routing.Route;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.*;
import net.ehicks.bts.util.PasswordUtil;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserHandler
{
    private static final Logger log = LoggerFactory.getLogger(AdminHandler.class);

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "form")
    public static String showManageUsers(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        request.setAttribute("users", User.getAll());

        return "/webroot/admin/users.jsp";
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "create")
    public static void createUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String logonId = Common.getSafeString(request.getParameter("fldLogonId"));
        User user = new User();
        user.setLogonId(logonId);
        long userId = EOI.insert(user, userSession);

        response.sendRedirect("view?tab1=admin&tab2=users&action=form");
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "delete")
    public static void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        if (user != null)
            EOI.executeDelete(user, userSession);

        response.sendRedirect("view?tab1=admin&tab2=users&action=form");
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "print")
    public static void printUsers(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        List<List> userData = new ArrayList<>();
        userData.add(Arrays.asList("Object Id", "Logon Id", "Last", "First", "Created On"));
        for (User user : User.getAll())
            userData.add(Arrays.asList(user.getId(), user.getLogonId(), user.getLastName(), user.getFirstName(), user.getCreatedOn()));
        File file = PdfCreator.createPdf("Me", "Users Report", "", userData);

        CommonIO.sendFileInResponse(response, file, true);
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "modify", action = "form")
    public static String showModifyUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        request.setAttribute("user", user);
        request.setAttribute("publicAvatars", Avatar.getAllPublic());

        return "/webroot/admin/modifyUser.jsp";
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "modify", action = "modify")
    public static void modifyUser(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        if (user != null)
        {
            String logonId = Common.getSafeString(request.getParameter("logonId"));
            Long avatarId = Common.stringToLong(request.getParameter("avatarId"));
            String firstName = Common.getSafeString(request.getParameter("firstName"));
            String lastName = Common.getSafeString(request.getParameter("lastName"));
            boolean enabled = request.getParameter("enabled") != null;
            user.setLogonId(logonId);
            if (avatarId > 0)
                user.setAvatarId(avatarId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(enabled);
            EOI.update(user, userSession);

            List<Long> selectedGroupIds = Arrays.stream(request.getParameterValues("groups")).map(Long::valueOf).collect(Collectors.toList());

            // remove existing groups that weren't selected
            for (GroupMap groupMap : GroupMap.getByUserId(userId))
                if (!selectedGroupIds.contains(groupMap.getGroupId()))
                    EOI.executeDelete(groupMap, userSession);
            // add new groups that were selected but didn't already exist
            for (Long groupId : selectedGroupIds)
            {
                GroupMap groupMap = GroupMap.getByUserIdAndGroupId(userId, groupId);
                if (groupMap == null)
                {
                    groupMap = new GroupMap();
                    groupMap.setUserId(user.getId());
                    groupMap.setGroupId(groupId);
                    EOI.insert(groupMap, userSession);
                }
            }

            List<Long> selectedProjectIds = Arrays.stream(request.getParameterValues("projects")).map(Long::valueOf).collect(Collectors.toList());

            // remove existing projects that weren't selected
            for (ProjectMap projectMap : ProjectMap.getByUserId(userId))
                if (!selectedProjectIds.contains(projectMap.getProjectId()))
                    EOI.executeDelete(projectMap, userSession);
            // add new projects that were selected but didn't already exist
            for (Long projectId : selectedProjectIds)
            {
                ProjectMap projectMap = ProjectMap.getByUserIdAndProjectId(userId, projectId);
                if (projectMap == null)
                {
                    projectMap = new ProjectMap();
                    projectMap.setUserId(user.getId());
                    projectMap.setProjectId(projectId);
                    EOI.insert(projectMap, userSession);
                }
            }
        }

        response.sendRedirect("view?tab1=admin&tab2=users&tab3=modify&action=form&userId=" + userId);
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "modify", action = "updateAvatar")
    public static void updateAvatar(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        if (user != null)
        {
            Long avatarId = Common.stringToLong(request.getParameter("fldAvatarId"));
            if (avatarId > 0)
            {
                user.setAvatarId(avatarId);
                EOI.update(user, userSession);
            }
        }

        response.sendRedirect("view?tab1=admin&tab2=users&tab3=modify&action=form&userId=" + userId);
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "modify", action = "uploadAvatar")
    public static void uploadAvatar(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String userId = request.getParameter("userId");

        String responseMessage = "";
        long dbFileId = 0;

        for (FileItem fileItem : CommonIO.getFilesFromRequest(request))
        {
            if (!CommonIO.isValidSize(fileItem))
            {
                responseMessage = "File size too large.";
                continue;
            }
            if (!CommonIO.isImage(fileItem))
            {
                responseMessage = "Not an image.";
                continue;
            }

            DBFile dbFile = new DBFile(CommonIO.getName(fileItem), fileItem.get());
            dbFileId = EOI.insert(dbFile, userSession);
        }

        if (dbFileId > 0)
        {
            Avatar avatar = new Avatar();
            avatar.setDbFileId(dbFileId);
            avatar.setCreatedByUserId(userSession.getUserId());
            avatar.setCreatedOn(new Date());
            long avatarId = EOI.insert(avatar, userSession);

            User user = User.getByUserId(Long.valueOf(userId));
            user.setAvatarId(avatarId);
            EOI.update(user, userSession);

            responseMessage = "Avatar updated.";
        }

        request.getSession().setAttribute("responseMessage", responseMessage);
        response.sendRedirect("view?tab1=admin&tab2=users&tab3=modify&action=form&userId=" + userId);
    }

    @Route(tab1 = "admin", tab2 = "users", tab3 = "", action = "changePassword")
    public static void changePassword(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);
        if (user != null)
        {
            String password = Common.getSafeString(request.getParameter("password"));
            if (password.length() > 0)
            {
                user.setPassword(PasswordUtil.digestPassword(password));
                EOI.update(user, userSession);
            }
        }

        response.sendRedirect("view?tab1=admin&tab2=users&tab3=modify&action=form&userId=" + userId);
    }
}
