package com.hicks;

import com.hicks.beans.*;
import net.ehicks.eoi.EOI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultDataLoader
{
    static void createDemoData()
    {
        List result = EOI.executeQueryOneResult("select count(*) from bts_users", new ArrayList<>());
        long rows = (Long) result.get(0);
        if (rows == 0)
        {
            User user = new User();
            user.setLogonId("***REMOVED***");
            user.setPassword("eric");
            user.setCreatedOn(new Date());
            user.setUpdatedOn(new Date());
            EOI.insert(user);

            user = new User();
            user.setLogonId("***REMOVED***");
            user.setPassword("val");
            user.setCreatedOn(new Date());
            EOI.insert(user);

            user = new User();
            user.setLogonId("valeric@yahoo.com");
            user.setPassword("valeric");
            user.setCreatedOn(new Date());
            user.setUpdatedOn(new Date());
            EOI.insert(user);
        }

        result = EOI.executeQueryOneResult("select count(*) from bts_roles", new ArrayList<>());
        rows = (Long) result.get(0);
        if (rows == 0)
        {
            Role role = new Role();
            User user = User.getByUserId(1L);
            role.setLogonId(user.getLogonId());
            role.setUserId(user.getId());
            role.setRoleName("user");
            EOI.insert(role);

            role = new Role();
            user = User.getByUserId(1L);
            role.setLogonId(user.getLogonId());
            role.setUserId(user.getId());
            role.setRoleName("admin");
            EOI.insert(role);

            role = new Role();
            user = User.getByUserId(2L);
            role.setLogonId(user.getLogonId());
            role.setUserId(user.getId());
            role.setRoleName("user");
            EOI.insert(role);
        }

        result = EOI.executeQueryOneResult("select count(*) from projects", new ArrayList<>());
        rows = (Long) result.get(0);
        if (rows == 0)
        {
            Project project = new Project();
            project.setName("Genesis");
            project.setPrefix("GS");
            EOI.insert(project);

            project = new Project();
            project.setName("SchoolFI");
            project.setPrefix("SF");
            EOI.insert(project);
        }

        List<Severity> severities = Severity.getAll();
        if (severities.size() == 0)
        {
            Severity severity = new Severity();
            severity.setName("High");
            EOI.insert(severity);
            severity.setName("Low");
            EOI.insert(severity);
        }

        result = EOI.executeQueryOneResult("select count(*) from issues", new ArrayList<>());
        rows = (Long) result.get(0);
        if (rows == 0)
        {
            Issue issue = new Issue();
            issue.setTitle("Thing is Broken");
            issue.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras quis lorem ac dui scelerisque gravida. Nulla efficitur turpis nec augue finibus, a dictum neque elementum. Ut eget aliquam nisl. Cras ultricies semper blandit. Nulla in semper leo. Phasellus cursus metus tortor, non lacinia purus hendrerit vitae. Etiam venenatis erat in magna maximus volutpat. Aenean dapibus nisi nisl, vitae viverra nulla cursus a. Nullam efficitur quam non elementum aliquet. Cras odio risus, dapibus ac mauris ut, malesuada pellentesque nunc.\n" +
                    "<br><br>" +
                    "Suspendisse consectetur augue dolor, et dignissim libero dapibus non. Nulla accumsan sollicitudin hendrerit. Aliquam ex eros, volutpat ac lobortis id, aliquet quis odio. Curabitur vel suscipit lorem. Sed a felis justo. Phasellus lacinia lorem eget sem venenatis dapibus. Nulla facilisi. Donec finibus urna sit amet dui porttitor, non laoreet enim luctus. Mauris luctus, tellus vitae tincidunt congue, purus dui faucibus eros, sit amet venenatis lectus orci eget felis. Maecenas tempus, urna nec varius bibendum, ligula libero egestas sapien, et varius eros odio ut libero. Nullam scelerisque consectetur purus, a auctor dui pharetra ac. Sed congue vel risus non bibendum.");
            issue.setProjectId(1L);
            issue.setZoneId(1L);
            issue.setAssigneeUserId(2L);
            issue.setReporterUserId(1L);
            issue.setIssueTypeId(1L);
            issue.setSeverityId(1L);
            issue.setStatus("OPEN");
            issue.setCreatedOn(new Date());
            issue.setLastUpdatedOn(new Date());
            EOI.insert(issue);
            EOI.insert(issue);
            EOI.insert(issue);
            EOI.insert(issue);
            EOI.insert(issue);
            EOI.insert(issue);

            issue = new Issue();
            issue.setTitle("We Would Like This New Thing");
            issue.setDescription("Aliquam nec rhoncus lorem. Curabitur maximus ligula lectus, id fermentum mauris tempus nec. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed vel neque accumsan, sodales velit sed, gravida ipsum. Aliquam erat volutpat. Fusce et nulla dui. Nulla congue dolor vitae nulla imperdiet scelerisque. In iaculis dapibus dolor, id tempus erat sagittis a. Vivamus sed facilisis leo, vel consectetur libero. Pellentesque quis faucibus nisl, in lobortis justo. Donec id quam consectetur, euismod ex nec, porttitor mauris. Quisque sollicitudin arcu nec ex vehicula, quis tempus nibh imperdiet. Vestibulum cursus dui ut diam interdum, eu cursus justo hendrerit. Aliquam molestie dui ex, eu eleifend ipsum pulvinar ut. Mauris mollis, justo at finibus porttitor, tortor diam porttitor lectus, eget varius augue ex at arcu. Maecenas sit amet tellus accumsan, feugiat ligula in, egestas nisl.");
            issue.setProjectId(2L);
            issue.setZoneId(2L);
            issue.setAssigneeUserId(1L);
            issue.setReporterUserId(2L);
            issue.setIssueTypeId(2L);
            issue.setSeverityId(2L);
            issue.setStatus("OPEN");
            issue.setCreatedOn(new Date());
            issue.setLastUpdatedOn(new Date());
            EOI.insert(issue);
            EOI.insert(issue);
            EOI.insert(issue);
            EOI.insert(issue);
            EOI.insert(issue);
            EOI.insert(issue);
        }

        List<Zone> zones = Zone.getAll();
        if (zones.size() == 0)
        {
            Zone zone = new Zone();
            zone.setName("Readington");
            EOI.insert(zone);

            zone = new Zone();
            zone.setName("Bridgewater");
            EOI.insert(zone);

            zone = new Zone();
            zone.setName("Califon");
            EOI.insert(zone);

            zone = new Zone();
            zone.setName("Flemington");
            EOI.insert(zone);
        }

        List<ZoneMap> zoneMaps = ZoneMap.getAll();
        if (zoneMaps.size() == 0)
        {
            User user = User.getByUserId(1L);
            zones = Zone.getAll();
            for (Zone zone : zones)
            {
                ZoneMap zoneMap = new ZoneMap();
                zoneMap.setUserId(user.getId());
                zoneMap.setZoneId(zone.getId());
                EOI.insert(zoneMap);
            }
            user = User.getByUserId(2L);
            for (Zone zone : zones)
            {
                ZoneMap zoneMap = new ZoneMap();
                zoneMap.setUserId(user.getId());
                zoneMap.setZoneId(zone.getId());
                EOI.insert(zoneMap);
            }
        }

        List<IssueType> issueTypes = IssueType.getAll();
        if (issueTypes.size() == 0)
        {
            IssueType issueType = new IssueType();
            issueType.setName("Bug");
            EOI.insert(issueType);
            issueType.setName("New Feature");
            EOI.insert(issueType);
            issueType.setName("Question");
            EOI.insert(issueType);
        }

        List<Comment> comments = Comment.getAll();
        if (comments.size() == 0)
        {
            Comment comment = new Comment();
            comment.setIssueId(2L);
            comment.setZoneId(2L);
            comment.setCreatedByUserId(1L);
            comment.setCreatedOn(new Date());
            comment.setContent("I think we can do that.");
            EOI.insert(comment);

            comment = new Comment();
            comment.setIssueId(2L);
            comment.setZoneId(2L);
            comment.setCreatedByUserId(2L);
            comment.setCreatedOn(new Date());
            comment.setContent("OK AWESOME TO HEAR SO.");
            EOI.insert(comment);
        }

        List<WatcherMap> watcherMaps = WatcherMap.getAll();
        if (watcherMaps.size() == 0)
        {
            WatcherMap watcherMap = new WatcherMap();
            watcherMap.setUserId(2L);
            watcherMap.setIssueId(2L);
            EOI.insert(watcherMap);

            watcherMap = new WatcherMap();
            watcherMap.setUserId(3L);
            watcherMap.setIssueId(2L);
            EOI.insert(watcherMap);
        }
    }
}
