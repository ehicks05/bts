package com.hicks;

import com.hicks.beans.*;
import net.ehicks.eoi.EOI;

import javax.persistence.criteria.Path;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

public class DefaultDataLoader
{
    static void createDemoData()
    {
        System.out.print("Seeding dummy data...");
        List result = EOI.executeQueryOneResult("select count(*) from bts_users", new ArrayList<>());
        long rows = (Long) result.get(0);
        if (rows == 0)
        {
            User user = new User();
            user.setLogonId("***REMOVED***");
            user.setPassword("eric");
            user.setAvatarId(2L);
            user.setCreatedOn(new Date());
            user.setUpdatedOn(new Date());
            EOI.insert(user);

            user = new User();
            user.setLogonId("***REMOVED***");
            user.setPassword("val");
            user.setAvatarId(3L);
            user.setCreatedOn(new Date());
            EOI.insert(user);

            user = new User();
            user.setLogonId("khicks@yahoo.com");
            user.setPassword("khicks");
            user.setAvatarId(8L);
            user.setCreatedOn(new Date());
            user.setUpdatedOn(new Date());
            EOI.insert(user);

            user = new User();
            user.setLogonId("***REMOVED***");
            user.setPassword("steve");
            user.setAvatarId(15L);
            user.setCreatedOn(new Date());
            user.setUpdatedOn(new Date());
            EOI.insert(user);

            user = new User();
            user.setLogonId("thicks@yahoo.com");
            user.setPassword("test");
            user.setAvatarId(5L);
            user.setCreatedOn(new Date());
            user.setUpdatedOn(new Date());
            EOI.insert(user);

            user = new User();
            user.setLogonId("bhicks@yahoo.com");
            user.setPassword("test");
            user.setAvatarId(10L);
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

            role = new Role();
            user = User.getByUserId(3L);
            role.setLogonId(user.getLogonId());
            role.setUserId(user.getId());
            role.setRoleName("user");
            EOI.insert(role);

            role = new Role();
            user = User.getByUserId(4L);
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

            project = new Project();
            project.setName("Cinemang");
            project.setPrefix("CM");
            EOI.insert(project);
        }

        result = EOI.executeQueryOneResult("select count(*) from statuses", new ArrayList<>());
        rows = (Long) result.get(0);
        if (rows == 0)
        {
            Status status = new Status();
            status.setName("Open");
            EOI.insert(status);

            status = new Status();
            status.setName("Closed");
            EOI.insert(status);

            status = new Status();
            status.setName("Re-opened");
            EOI.insert(status);
        }

        List<Severity> severities = Severity.getAll();
        if (severities.size() == 0)
        {
            Severity severity = new Severity();
            severity.setName("High");
            EOI.insert(severity);
            severity.setName("Low");
            EOI.insert(severity);
            severity.setName("Blocker");
            EOI.insert(severity);
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
            issueType.setName("Data Issue");
            EOI.insert(issueType);
        }

        List<String> adjectives = Arrays.asList("deactivated", "decommissioned", "ineffective", "ineffectual", "useless "+
                "inoperable", "unusable", "unworkable arrested", "asleep", "dormant", "fallow", "idle", "inert",
                "latent", "lifeless", "nonproductive", "quiescent", "sleepy", "stagnating", "unproductive", "vegetating");

        List<String> nouns = Arrays.asList("feature", "screen", "page", "report", "functionality");

        List<String> latin = Arrays.asList("annus", "ante meridiem", "aqua", "bene", "canis", "caput", "circus", "cogito",
                "corpus", "de facto", "deus", "ego", "equus", "ergo", "est", "hortus", "id", "in", "index", "iris", "latex",
                "legere", "librarium", "locus", "magnus", "mare", "mens", "murus", "musica", "nihil", "non", "nota", "novus",
                "opus", "orbus", "placebo", "post", "post meridian", "primus", "pro", "sanus", "solus", "sum", "tacete",
                "tempus", "terra", "urbs", "veni", "vici", "vidi");

        List<String> questions = Arrays.asList("Is", "How come I'm getting", "Why is", "What's with the", "Fix this",
                "Help with", "Problem regarding");

        Random r = new Random();

        result = EOI.executeQueryOneResult("select count(*) from issues", new ArrayList<>());
        rows = (Long) result.get(0);
        if (rows == 0)
        {
            IntStream.range(1, 4096).parallel().forEach(i ->
            {
                Issue issue = new Issue();
                long value = (long) r.nextInt(Project.getAll().size() + 1);
                issue.setProjectId(value > 0 ? value : 1);
                value = (long) r.nextInt(Zone.getAll().size() + 1);
                issue.setZoneId(value > 0 ? value : 1);
                value = (long) r.nextInt(User.getAll().size() + 1);
                issue.setAssigneeUserId(value > 0 ? value : 1);
                value = (long) r.nextInt(User.getAll().size() + 1);
                issue.setReporterUserId(value > 0 ? value : 1);
                value = (long) r.nextInt(IssueType.getAll().size() + 1);
                issue.setIssueTypeId(value > 0 ? value : 1);
                value = (long) r.nextInt(Severity.getAll().size() + 1);
                issue.setSeverityId(value > 0 ? value : 1);
                value = (long) r.nextInt(Status.getAll().size() + 1);
                issue.setStatusId(value > 0 ? value : 1);

                String noun = nouns.get(r.nextInt(nouns.size()));
                String adjective = adjectives.get(r.nextInt(adjectives.size()));
                String title = adjective + " " + noun;

                if (r.nextBoolean())
                    title = questions.get(r.nextInt(questions.size())) + " " + title;
                if (r.nextBoolean())
                    title = title.toUpperCase();
                if (r.nextBoolean())
                    title += "!";
                if (r.nextBoolean())
                    title += "?";
                if (r.nextInt(5) <= 1)
                    title += "...";
                issue.setTitle(title);

                String description = "";
                for (int j = 0; j < r.nextInt(128); j++)
                {
                    if (description.length() > 0)
                        description += " " ;
                    description += latin.get(r.nextInt(latin.size()));
                }
                issue.setDescription(description);

                Date createdOn = getRandomDateTime();
                issue.setCreatedOn(createdOn);
                issue.setLastUpdatedOn(getRandomDateTimeForward(createdOn));
                EOI.insert(issue);
            });
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

        List<Comment> comments = Comment.getAll();
        if (comments.size() == 0)
        {
            for (Issue issue : Issue.getAll())
            {
                long issueId = issue.getId();
                if (issueId == 2)
                    continue;

                long zoneId = issue.getZoneId();
                for (int i = 0; i < r.nextInt(24); i++)
                {
                    Comment comment = new Comment();
                    comment.setIssueId(issueId);
                    comment.setZoneId(zoneId);
                    comment.setCreatedByUserId((long) r.nextInt((User.getAll().size())) + 1);
                    comment.setCreatedOn(new Date());

                    String content = "";
                    for (int j = 0; j < r.nextInt(128); j++)
                    {
                        if (content.length() > 0)
                            content += " " ;
                        content += latin.get(r.nextInt(latin.size()));
                    }
                    comment.setContent(content);
                    EOI.insert(comment);
                }
            }

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
            comment.setContent("OK that will be great :).");
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

        List<DBFile> dbFiles = DBFile.getAll();
        if (dbFiles.size() == 0)
        {
            File avatarDir = Paths.get(SystemInfo.getServletContext().getRealPath("/images/avatars/png/")).toFile();
            if (avatarDir.exists() && avatarDir.isDirectory())
            {
                List<File> avatars = Arrays.asList(avatarDir.listFiles());
                Collections.sort(avatars);
                for (File avatar : avatars)
                {
                    if (avatar.exists() && avatar.isFile())
                    {
                        byte[] content = null;
                        try
                        {
                            content = Files.readAllBytes(avatar.toPath());
                        }
                        catch (IOException e)
                        {
                            System.out.println(e.getMessage());
                        }

                        DBFile dbFile = new DBFile();
                        dbFile.setName(avatar.getName());
                        dbFile.setContent(content);
                        EOI.insert(dbFile);
                    }
                }
            }
        }

        List<IssueForm> issueForms = IssueForm.getAll();
        if (issueForms.size() == 0)
        {
            IssueForm issueForm = new IssueForm();
            issueForm.setFormName("Assigned To Me");
            issueForm.setOnDash(true);
            issueForm.setUserId(1L);
            issueForm.setAssigneeUserIds("1");
            issueForm.setStatusIds("1,2");
            EOI.insert(issueForm);

            issueForm = new IssueForm();
            issueForm.setFormName("Assigned To Me");
            issueForm.setOnDash(true);
            issueForm.setUserId(2L);
            issueForm.setAssigneeUserIds("2");
            EOI.insert(issueForm);

            issueForm = new IssueForm();
            issueForm.setFormName("All Issues");
            issueForm.setOnDash(true);
            issueForm.setUserId(1L);
            EOI.insert(issueForm);
        }
        System.out.println("done");

    }

    private static Date getRandomDateTime()
    {
        LocalDateTime ldt = LocalDateTime.now();
        Random r = new Random();
        LocalDateTime createdOnLdt = ldt.minusYears(r.nextInt(5)).minusWeeks(r.nextInt(52))
                .minusDays(r.nextInt(7))
                .minusHours(r.nextInt(24))
                .minusMinutes(r.nextInt(60))
                .minusSeconds(r.nextInt(60));
        return Date.from(createdOnLdt.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Date getRandomDateTimeForward(Date ldt)
    {
        Random r = new Random();
        LocalDateTime createdOnLdt = LocalDateTime.ofInstant(ldt.toInstant(), ZoneId.systemDefault())
                .plusWeeks(r.nextInt(2))
                .plusDays(r.nextInt(7))
                .plusHours(r.nextInt(24))
                .plusMinutes(r.nextInt(60))
                .plusSeconds(r.nextInt(60));
        return Date.from(createdOnLdt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
