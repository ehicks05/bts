package net.ehicks.bts;

import net.ehicks.bts.beans.*;
import net.ehicks.common.Timer;
import net.ehicks.eoi.EOI;
import org.apache.catalina.realm.MessageDigestCredentialHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

public class DefaultDataLoader
{
    private static final Logger log = LoggerFactory.getLogger(DefaultDataLoader.class);

    private static int issueCount = 1_024;
    private static int cryptoIterations = 200_000;

    private static List<String> latin = Arrays.asList("annus", "ante meridiem", "aqua", "bene", "canis", "caput", "circus", "cogito",
            "corpus", "de facto", "deus", "ego", "equus", "ergo", "est", "hortus", "id", "in", "index", "iris", "latex",
            "legere", "librarium", "locus", "magnus", "mare", "mens", "murus", "musica", "nihil", "non", "nota", "novus",
            "opus", "orbus", "placebo", "post", "post meridian", "primus", "pro", "sanus", "solus", "sum", "tacete",
            "tempus", "terra", "urbs", "veni", "vici", "vidi");

    static void createDemoData()
    {
        log.info("Seeding dummy data");
        Timer timer = new Timer();

        createUsers();
        log.debug(timer.printDuration("createUsers"));

        createGroups();
        log.debug(timer.printDuration("createGroups"));

        createGroupMaps();
        log.debug(timer.printDuration("createGroupMaps"));

        createProjects();
        log.debug(timer.printDuration("createProjects"));

        createStatuses();
        log.debug(timer.printDuration("createStatuses"));

        createSeverities();
        log.debug(timer.printDuration("createSeverities"));

        createIssueTypes();
        log.debug(timer.printDuration("createIssueTypes"));

        createProjectMaps();
        log.debug(timer.printDuration("createProjectMaps"));

        createDBFiles();
        log.debug(timer.printDuration("createDBFiles"));

        createIssues();
        log.debug(timer.printDuration("createIssues"));

        createAttachments();
        log.debug(timer.printDuration("createAttachments"));

        createIssueForms();
        log.debug(timer.printDuration("createIssueForms"));

        createWatcherMaps();
        log.debug(timer.printDuration("createWatcherMaps"));

        createComments();
        log.debug(timer.printDuration("createComments"));

        log.info(timer.printDuration("Done seeding dummy data"));
    }

    private static void createIssueForms()
    {
        for (User user : User.getAll())
        {
            IssueForm issueForm = new IssueForm();
            issueForm.setFormName("All Issues");
            issueForm.setOnDash(true);
            issueForm.setUserId(user.getId());
            EOI.insert(issueForm);

            issueForm.setFormName("Assigned To Me");
            issueForm.setAssigneeUserIds(String.valueOf(user.getId()));
            EOI.insert(issueForm);
        }
    }

    private static void createDBFiles()
    {
        File avatarDir = Paths.get(SystemInfo.INSTANCE.getServletContext().getRealPath("/images/avatars/png/")).toFile();
        if (avatarDir.exists() && avatarDir.isDirectory())
        {
            List<File> avatars = Arrays.asList(avatarDir.listFiles());
            Collections.sort(avatars);
            for (File avatarFile : avatars)
            {
                if (avatarFile.exists() && avatarFile.isFile())
                {
                    byte[] content = null;
                    try
                    {
                        content = Files.readAllBytes(avatarFile.toPath());
                    }
                    catch (IOException e)
                    {
                        log.error(e.getMessage(), e);
                    }

                    DBFile dbFile = new DBFile();
                    dbFile.setName(avatarFile.getName());
                    dbFile.setContent(content);
                    dbFile.setLength((long) content.length);
                    long dbFileId = EOI.insert(dbFile);

                    Avatar avatar = new Avatar();
                    avatar.setDbFileId(dbFileId);
                    avatar.setCreatedOn(new Date());
                    EOI.insert(avatar);
                }
            }
        }
    }

    private static void createWatcherMaps()
    {
        List<User> users = User.getAll();
        Random r = new Random();
        EOI.startTransaction();

        List<WatcherMap> watcherMaps = new ArrayList<>();
        Issue.getAll().forEach(issue -> {
            List<Integer> selectedUserIndexes = new ArrayList<>();
            for (int i = 0; i < r.nextInt(3) + 1; i++)
            {
                int userIndex = r.nextInt(users.size());
                if (!selectedUserIndexes.contains(userIndex))
                    selectedUserIndexes.add(userIndex);
            }

            for (int userIndex : selectedUserIndexes)
            {
                WatcherMap watcherMap = new WatcherMap();
                watcherMap.setUserId(users.get(userIndex).getId());
                watcherMap.setIssueId(issue.getId());
                watcherMaps.add(watcherMap);
            }
        });
        EOI.batchInsert(watcherMaps);
        EOI.commit();
    }

    private static void createComments()
    {
        Random r = new Random();
        int users = User.getAll().size();

        EOI.startTransaction();
        List<Comment> comments = new ArrayList<>();
        Issue.getAll().forEach(issue -> {
            long issueId = issue.getId();
            if (issueId == 2)
                return;

            for (int i = 0; i < r.nextInt(8); i++)
            {
                Comment comment = new Comment();
                comment.setIssueId(issueId);
                comment.setCreatedByUserId((long) r.nextInt(users) + 1);
                comment.setCreatedOn(new Date());

                String content = "";
                for (int j = 0; j < r.nextInt(32); j++)
                {
                    if (content.length() > 0)
                        content += " " ;
                    content += latin.get(r.nextInt(latin.size()));
                }
                comment.setContent(content);
                comments.add(comment);
            }
        });
        EOI.batchInsert(comments);
        EOI.commit();

        Comment comment = new Comment();
        comment.setIssueId(2L);
        comment.setCreatedByUserId(2L);
        comment.setCreatedOn(new Date());
        comment.setContent("I think we can do that.");
        EOI.insert(comment);

        comment = new Comment();
        comment.setIssueId(2L);
        comment.setCreatedByUserId(3L);
        comment.setCreatedOn(new Date());
        comment.setContent("OK that will be great :).");
        EOI.insert(comment);
    }

    private static void createProjectMaps()
    {
        Random r = new Random();
        for (User user : User.getAll())
        {
            long projectId = r.nextInt(Project.getAll().size()) + 1;

            ProjectMap projectMap = new ProjectMap();
            projectMap.setUserId(user.getId());
            projectMap.setProjectId(projectId);
            EOI.insert(projectMap);
        }
    }

    private static void createAttachments()
    {
        File imgDir = Paths.get(SystemInfo.INSTANCE.getServletContext().getRealPath("/images/")).toFile();
        if (imgDir.exists() && imgDir.isDirectory())
        {
            List<File> images = Arrays.asList(imgDir.listFiles());
            Collections.sort(images);
            for (File img : images)
            {
                if (img.exists() && img.isFile() && img.getName().contains("Adobe_PDF"))
                {
                    byte[] content = null;
                    try
                    {
                        content = Files.readAllBytes(img.toPath());
                    }
                    catch (IOException e)
                    {
                        log.error(e.getMessage(), e);
                    }

                    DBFile dbFile = new DBFile();
                    dbFile.setName(img.getName());
                    dbFile.setContent(content);
                    dbFile.setLength((long) content.length);
                    long dbFileId = EOI.insert(dbFile);

                    Attachment attachment = new Attachment();
                    attachment.setIssueId(2L);
                    attachment.setCreatedByUserId(2L);
                    attachment.setCreatedOn(new Date());
                    attachment.setDbFileId(dbFileId);
                    EOI.insert(attachment);
                }
            }
        }
    }

    private static void createIssues()
    {
        List<String> adjectives = Arrays.asList("deactivated", "decommissioned", "ineffective", "ineffectual", "useless "+
                        "inoperable", "unusable", "unworkable arrested", "asleep", "dormant", "fallow", "idle", "inert",
                "latent", "lifeless", "nonproductive", "quiescent", "sleepy", "stagnating", "unproductive", "vegetating");

        List<String> nouns = Arrays.asList("feature", "screen", "page", "report", "functionality");

        List<String> questions = Arrays.asList("Is", "How come I'm getting", "Why is", "What's with the", "Fix this",
                "Help with", "Problem regarding");

        int projects = Project.getAll().size() + 1;
        int groups = Group.getAll().size() + 1;
        int assigneeUserIds = User.getAll().size() + 1;
        int reporterUserIds = User.getAll().size() + 1;
        int issueTypes = IssueType.getAll().size() + 1;
        int severities = Severity.getAll().size() + 1;
        int statuses = Status.getAll().size() + 1;

        Random r = new Random();

        EOI.startTransaction();

        List<Issue> issues = new ArrayList<>();
        IntStream.range(0, issueCount).forEach(i ->
        {
            Issue issue = new Issue();
            long value = (long) r.nextInt(projects);
            issue.setProjectId(value > 0 ? value : 1);
            value = (long) r.nextInt(groups);
            issue.setGroupId(value > 0 ? value : 1);
            value = (long) r.nextInt(assigneeUserIds);
            issue.setAssigneeUserId(value > 0 ? value : 1);
            value = (long) r.nextInt(reporterUserIds);
            issue.setReporterUserId(value > 0 ? value : 1);
            value = (long) r.nextInt(issueTypes);
            issue.setIssueTypeId(value > 0 ? value : 1);
            value = (long) r.nextInt(severities);
            issue.setSeverityId(value > 0 ? value : 1);
            value = (long) r.nextInt(statuses);
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
            for (int j = 0; j < r.nextInt(32); j++)
            {
                if (description.length() > 0)
                    description += " " ;
                description += latin.get(r.nextInt(latin.size()));
            }
            issue.setDescription(description);

            Date createdOn = getRandomDateTime();
            issue.setCreatedOn(createdOn);
            issue.setLastUpdatedOn(getRandomDateTimeForward(createdOn));
            issues.add(issue);
        });

        EOI.batchInsert(issues);
        EOI.commit();
    }

    private static void createIssueTypes()
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

    private static void createSeverities()
    {
        Severity severity = new Severity();
        severity.setName("High");
        EOI.insert(severity);
        severity.setName("Low");
        EOI.insert(severity);
        severity.setName("Blocker");
        EOI.insert(severity);
    }

    private static void createStatuses()
    {
        Status status = new Status();
        status.setName("Open");
        EOI.insert(status);
        status.setName("Closed");
        EOI.insert(status);
        status.setName("Re-opened");
        EOI.insert(status);
    }

    private static void createProjects()
    {
        Project project = new Project();
        project.setName("Genesis");
        project.setPrefix("GS");
        EOI.insert(project);
        project.setName("SchoolFI");
        project.setPrefix("SF");
        EOI.insert(project);
        project.setName("Cinemang");
        project.setPrefix("CM");
        EOI.insert(project);
    }

    private static void createGroupMaps()
    {
        Group support = Group.getByName("Support");
        Group admin = Group.getByName("Admin");

        List<Group> customerGroups = new ArrayList<>();
        for (Group group : Group.getAll())
            if (!group.getAdmin() && !group.getSupport())
                customerGroups.add(group);

        Random r = new Random();

        for (User user : User.getAll())
        {
            GroupMap groupMap = new GroupMap();
            groupMap.setUserId(user.getId());
            if (user.getLogonId().equals("***REMOVED***") || user.getLogonId().equals("***REMOVED***"))
                groupMap.setGroupId(admin.getId());
            else if (user.getLogonId().equals("tupac@yahoo.com"))
                groupMap.setGroupId(support.getId());
            else
            {
                // pick a random element in customerGroups
                long groupId = customerGroups.get(r.nextInt(customerGroups.size())).getId();
                groupMap.setGroupId(groupId);
            }
            EOI.insert(groupMap);
        }
    }

    private static void createGroups()
    {
        Group group = new Group();

        group.setName("Readington");
        EOI.insert(group);
        group.setName("Bridgewater");
        EOI.insert(group);
        group.setName("Califon");
        EOI.insert(group);
        group.setName("Flemington");
        EOI.insert(group);

        group.setName("Support");
        group.setSupport(true);
        EOI.insert(group);

        group.setName("Admin");
        group.setSupport(false);
        group.setAdmin(true);
        EOI.insert(group);
    }

    private static void createUsers()
    {
        Map<String, List<String>> users = new HashMap<>();
        users.put("***REMOVED***", new ArrayList<>(Arrays.asList("eric", "2", "Eric", ***REMOVED***)));
        users.put("***REMOVED***", new ArrayList<>(Arrays.asList("steve", "15", "Steve", ***REMOVED***)));
        users.put("tupac@test.com", new ArrayList<>(Arrays.asList("test", "3", "2", "Pac")));
        users.put("bill@test.com", new ArrayList<>(Arrays.asList("test", "8", "Bill", "Smith")));
        users.put("john@test.com", new ArrayList<>(Arrays.asList("test", "5", "John", "Doe")));
        users.put("jane@test.com", new ArrayList<>(Arrays.asList("test", "10", "Jane", "Doe")));

        MessageDigestCredentialHandler credentialHandler = null;
        try
        {
            credentialHandler = new MessageDigestCredentialHandler();
            credentialHandler.setAlgorithm("SHA-256");
            credentialHandler.setIterations(cryptoIterations);
            credentialHandler.setSaltLength(32);
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error(e.getMessage(), e);
        }

        for (String key : users.keySet())
        {
            User user = new User();
            user.setLogonId(key);

            long start = System.currentTimeMillis();
            String rawPassword = users.get(key).get(0);
            String password = credentialHandler.mutate(rawPassword);
            log.debug("{} sha-256 iterations in {} ms", cryptoIterations, (System.currentTimeMillis() - start));

            user.setPassword(password);
            user.setEnabled(true);
            if (key.equals("thicks@yahoo.com"))
                user.setEnabled(false);
            user.setAvatarId(Long.valueOf(users.get(key).get(1)));
            user.setFirstName(users.get(key).get(2));
            user.setLastName(users.get(key).get(3));
            user.setCreatedOn(new Date());
            user.setUpdatedOn(new Date());
            long userId = EOI.insert(user);

            Role role = new Role();
            role.setLogonId(user.getLogonId());
            role.setUserId(userId);
            role.setRoleName("user");
            EOI.insert(role);

            if (user.getLogonId().equals("***REMOVED***"))
            {
                role.setRoleName("admin");
                EOI.insert(role);
            }
        }
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
