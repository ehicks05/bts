package net.ehicks.bts.util;

import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.Group;

import java.util.List;

public class Security
{
    public static boolean hasAccess(UserSession userSession, Group group)
    {
        List<Group> allGroups = userSession.getUser().getAllGroups();
        return allGroups.contains(Group.getByName("Admin"))
                || allGroups.contains(group);
    }
}
