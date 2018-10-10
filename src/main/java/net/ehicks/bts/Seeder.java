package net.ehicks.bts;

import net.ehicks.bts.beans.*;
import net.ehicks.bts.util.CommonIO;
import net.ehicks.bts.util.PasswordUtil;
import net.ehicks.common.Timer;
import net.ehicks.eoi.EOI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class Seeder
{
    private static final Logger log = LoggerFactory.getLogger(Seeder.class);

    private static int issueCount = (int) Math.pow(1_024, 2);
    private static boolean useBatches = true;

    private static List<String> latin = Arrays.asList("annus", "ante meridiem", "aqua", "bene", "canis", "caput", "circus", "cogito",
            "corpus", "de facto", "deus", "ego", "equus", "ergo", "est", "hortus", "id", "in", "index", "iris", "latex",
            "legere", "librarium", "locus", "magnus", "mare", "mens", "murus", "musica", "nihil", "non", "nota", "novus",
            "opus", "orbus", "placebo", "post", "post meridian", "primus", "pro", "sanus", "solus", "sum", "tacete",
            "tempus", "terra", "urbs", "veni", "vici", "vidi");

    public static String getRandomLatinWords(Random random, int maxLength)
    {
        StringBuilder content = new StringBuilder();
        for (int j = 0; j < random.nextInt(maxLength); j++)
        {
            if (content.length() > 0)
                content.append(" ");
            content.append(latin.get(random.nextInt(latin.size())));
        }

        return content.toString();
    }

    static void createDemoData()
    {
        log.info("Seeding dummy data");
        Timer timer = new Timer();

        // use in production
        createBtsSystem();
        log.debug(timer.printDuration("createBtsSystem"));

        createUsers();
        log.debug(timer.printDuration("createUsers"));

        createGroups();
        log.debug(timer.printDuration("createGroups"));

        createGroupMaps();
        log.debug(timer.printDuration("createGroupMaps"));

        createProjects();
        log.debug(timer.printDuration("createProjects"));

        // use in production
        createStatuses();
        log.debug(timer.printDuration("createStatuses"));

        // use in production
        createSeverities();
        log.debug(timer.printDuration("createSeverities"));

        // use in production
        createIssueTypes();
        log.debug(timer.printDuration("createIssueTypes"));

        createProjectMaps();
        log.debug(timer.printDuration("createProjectMaps"));

        // use in production
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

        createSubscriptions();
        log.debug(timer.printDuration("createSubscriptions"));

        createChatObjects();
        log.debug(timer.printDuration("createSubscriptions"));

        log.info(timer.printDuration("Done seeding dummy data"));
    }

    private static void createBtsSystem()
    {
        BtsSystem btsSystem = BtsSystem.getSystem();
        if (btsSystem == null)
        {
            btsSystem = new BtsSystem();
            long id = EOI.insert(btsSystem, SystemTask.SEEDER);
            btsSystem = BtsSystem.getById(id);
        }
        btsSystem.setInstanceName("BugCo Industries");
        btsSystem.setLogonMessage("<span>Welcome to Puffin Issue Tracker.<br>Please contact Eric for a demo.</span>");
        btsSystem.setTheme("default");

        EOI.update(btsSystem, SystemTask.SEEDER);
    }

    private static void createIssueForms()
    {
        for (User user : User.getAll())
        {
            IssueForm issueForm = new IssueForm();
            issueForm.setFormName("All Issues");
            issueForm.setOnDash(true);
            issueForm.setUserId(user.getId());
            EOI.insert(issueForm, SystemTask.SEEDER);

            issueForm.setFormName("Assigned To Me");
            issueForm.setAssigneeUserIds(String.valueOf(user.getId()));
            EOI.insert(issueForm, SystemTask.SEEDER);

            if (user.getLogonId().equals("eric@test.com"))
            {
                issueForm.setFormName("Readington's Issues");
                issueForm.setAssigneeUserIds("");
                issueForm.setGroupIds(String.valueOf(Group.getByName("Readington").getId()));
                EOI.insert(issueForm, SystemTask.SEEDER);

                issueForm.setFormName("Issues with the word 'vici'");
                issueForm.setGroupIds("");
                issueForm.setContainsText("vici");
                EOI.insert(issueForm, SystemTask.SEEDER);

                issueForm.setFormName("Reopened Issues");
                issueForm.setContainsText("");
                issueForm.setStatusIds(String.valueOf(Status.getByName("Re-opened").getId()));
                EOI.insert(issueForm, SystemTask.SEEDER);

                issueForm.setFormName("All Cinemang Issues");
                issueForm.setStatusIds("");
                issueForm.setProjectIds(String.valueOf(Project.getByName("Cinemang").getId()));
                EOI.insert(issueForm, SystemTask.SEEDER);
            }
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

                    DBFile dbFile = new DBFile(avatarFile.getName(), content);
                    long dbFileId = EOI.insert(dbFile, SystemTask.SEEDER);

                    Avatar avatar = new Avatar();
                    avatar.setDbFileId(dbFileId);
                    avatar.setPublicUse(true);
                    avatar.setCreatedOn(new Date());
                    long avatarId = EOI.insert(avatar, SystemTask.SEEDER);

                    // set default avatar
                    if (dbFile.getName().equals("no_avatar.png"))
                    {
                        BtsSystem btsSystem = BtsSystem.getSystem();
                        btsSystem.setDefaultAvatar(avatarId);
                        EOI.update(btsSystem, SystemTask.SEEDER);
                    }
                }
            }
        }
    }

    private static void createWatcherMaps()
    {
        List<User> users = User.getAll();
        Random r = new Random();

        if (useBatches)
            EOI.startTransaction();

        List<WatcherMap> watcherMaps = new ArrayList<>();
        List<Issue> issues = Issue.getAll();
        for (int issueIndex = 0; issueIndex < issues.size(); issueIndex++)
        {
            Issue issue = issues.get(issueIndex);
            if (issueIndex % (issues.size() / 4) == 0)
                log.info("WatcherMap for issue " + issueIndex + " / " + issues.size());

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
                if (useBatches)
                    watcherMaps.add(watcherMap);
                else
                    EOI.insert(watcherMap, SystemTask.SEEDER);
            }
        }

        if (useBatches)
        {
            EOI.batchInsert(watcherMaps);
            EOI.commit();
        }
    }

    private static void createComments()
    {
        Random r = new Random();
        int users = User.getAll().size();

        if (useBatches)
            EOI.startTransaction();
        List<Comment> comments = new ArrayList<>();
        List<Issue> issues = Issue.getAll();
        for (int issueIndex = 0; issueIndex < issues.size(); issueIndex++)
        {
            Issue issue = issues.get(issueIndex);
            long issueId = issue.getId();
            if (issueId == 2)
                continue;

            if (issueIndex % (issues.size() / 4) == 0)
                log.info("Comment for issue " + issueIndex + " / " + issues.size());

            for (int i = 0; i < r.nextInt(8); i++)
            {
                Comment comment = new Comment();
                comment.setIssueId(issueId);
                comment.setCreatedByUserId((long) r.nextInt(users) + 1);
                comment.setCreatedOn(new Date());
                comment.setContent(getRandomLatinWords(r, 32));
                if (useBatches)
                    comments.add(comment);
                else
                {
                    long id = EOI.insert(comment, SystemTask.SEEDER);
                    comment = Comment.getById(id);
                    
                    IssueAudit issueAudit = new IssueAudit(issueId, SystemTask.SEEDER, "added", comment.toString());
                    EOI.insert(issueAudit, SystemTask.SEEDER);
                }
            }
        };
        if (useBatches)
        {
            EOI.batchInsert(comments);
            EOI.commit();
        }
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
            EOI.insert(projectMap, SystemTask.SEEDER);
        }
    }

    private static void createAttachments()
    {
        File imgDir = Paths.get(SystemInfo.INSTANCE.getServletContext().getRealPath("/images/mimetypes")).toFile();
        if (imgDir.exists() && imgDir.isDirectory())
        {
            List<File> images = Arrays.asList(imgDir.listFiles());
            Collections.sort(images);
            for (File img : images)
            {
                if (img.exists() && img.isFile() && img.getName().contains("Adobe.png"))
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

                    long thumbnailId = 0;
                    if (img.getName().contains("png"))
                    {
                        byte[] scaledBytes = new byte[0];
                        try
                        {
                            scaledBytes = CommonIO.getThumbnail(img);
                        }
                        catch (IOException e)
                        {
                            log.error(e.getMessage(), e);
                        }

                        DBFile thumbnail = new DBFile(img.getName(), scaledBytes);
                        thumbnailId = EOI.insert(thumbnail, SystemTask.SEEDER);
                    }

                    DBFile dbFile = new DBFile(img.getName(), content);
                    dbFile.setThumbnailId(thumbnailId);
                    long dbFileId = EOI.insert(dbFile, SystemTask.SEEDER);

                    Attachment attachment = new Attachment(1L, dbFileId, 2L);
                    long id = EOI.insert(attachment, SystemTask.SEEDER);
                    attachment = Attachment.getById(id);

                    IssueAudit issueAudit = new IssueAudit(1L, SystemTask.SEEDER, "added", attachment.toString());
                    EOI.insert(issueAudit, SystemTask.SEEDER);
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

        if (useBatches)
            EOI.startTransaction();

        AtomicLong start = new AtomicLong(System.currentTimeMillis());

        List<Issue> issues = new ArrayList<>();
        IntStream.range(0, issueCount).forEach(i ->
        {
            if (System.currentTimeMillis() - start.get() > 10_000 || i % (issueCount / 4) == 0)
            {
                log.info("Issue " + i + " / " + issueCount);
                if (System.currentTimeMillis() - start.get() > 10_000)
                    start.set(System.currentTimeMillis());
            }
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

            String description = getRandomLatinWords(r, 32);
            issue.setDescription("<p>" + description + "</p>");

            Date createdOn = getRandomDateTime();
            issue.setCreatedOn(createdOn);
            issue.setLastUpdatedOn(getRandomDateTimeForward(createdOn));
            if (useBatches)
                issues.add(issue);
            else
            {
                long id = EOI.insert(issue, SystemTask.SEEDER);
//                issue = Issue.getById(id);
//
//                IssueAudit issueAudit = new IssueAudit(id, SystemTask.SEEDER, "added", issue.toString());
//                EOI.insert(issueAudit, SystemTask.SEEDER);
            }
        });

        if (useBatches)
        {
            EOI.batchInsert(issues);
            EOI.commit();
        }
        else
        {
            Issue.getAll().forEach(issue -> {
                IssueAudit issueAudit = new IssueAudit(issue.getId(), SystemTask.SEEDER, "added", issue.toString());
                EOI.insert(issueAudit, SystemTask.SEEDER);
            });
        }
    }

    private static void createIssueTypes()
    {
        IssueType issueType = new IssueType();
        issueType.setName("Bug");
        EOI.insert(issueType, SystemTask.SEEDER);
        issueType.setName("New Feature");
        EOI.insert(issueType, SystemTask.SEEDER);
        issueType.setName("Question");
        EOI.insert(issueType, SystemTask.SEEDER);
        issueType.setName("Data Issue");
        EOI.insert(issueType, SystemTask.SEEDER);
    }

    private static void createSeverities()
    {
        Severity severity = new Severity();
        severity.setName("High");
        EOI.insert(severity, SystemTask.SEEDER);
        severity.setName("Medium");
        EOI.insert(severity, SystemTask.SEEDER);
        severity.setName("Low");
        EOI.insert(severity, SystemTask.SEEDER);
    }

    private static void createStatuses()
    {
        Status status = new Status();
        status.setName("Open");
        EOI.insert(status, SystemTask.SEEDER);
        status.setName("Closed");
        EOI.insert(status, SystemTask.SEEDER);
        status.setName("Re-opened");
        EOI.insert(status, SystemTask.SEEDER);
    }

    private static void createProjects()
    {
        Project project = new Project();
        project.setName("Genesis");
        project.setPrefix("GS");
        EOI.insert(project, SystemTask.SEEDER);
        project.setName("SchoolFI");
        project.setPrefix("SF");
        EOI.insert(project, SystemTask.SEEDER);
        project.setName("Cinemang");
        project.setPrefix("CM");
        EOI.insert(project, SystemTask.SEEDER);
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
            if (user.getLogonId().equals("eric@test.com") || user.getLogonId().equals("steve@test.com"))
                groupMap.setGroupId(admin.getId());
            else if (user.getLogonId().equals("tupac@test.com"))
                groupMap.setGroupId(support.getId());
            else
            {
                // pick a random element in customerGroups
                long groupId = customerGroups.get(r.nextInt(customerGroups.size())).getId();
                groupMap.setGroupId(groupId);
            }
            EOI.insert(groupMap, SystemTask.SEEDER);
        }
    }

    private static void createGroups()
    {
        Group group = new Group();

        group.setName("Readington");
        EOI.insert(group, SystemTask.SEEDER);
        group.setName("Bridgewater");
        EOI.insert(group, SystemTask.SEEDER);
        group.setName("Califon");
        EOI.insert(group, SystemTask.SEEDER);
        group.setName("Flemington");
        EOI.insert(group, SystemTask.SEEDER);

        group.setName("Support");
        group.setSupport(true);
        EOI.insert(group, SystemTask.SEEDER);

        group.setName("Admin");
        group.setSupport(false);
        group.setAdmin(true);
        EOI.insert(group, SystemTask.SEEDER);
    }

    private static void createUsers()
    {
        Map<String, List<String>> users = new LinkedHashMap<>();
        users.put("eric@test.com", new ArrayList<>(Arrays.asList("eric", "2", "Eric", "Tester")));
        users.put("steve@test.com", new ArrayList<>(Arrays.asList("steve", "15", "Steve", "Tester")));
        users.put("tupac@test.com", new ArrayList<>(Arrays.asList("test", "3", "2", "Pac")));
        users.put("bill@test.com", new ArrayList<>(Arrays.asList("test", "8", "Bill", "Smith")));
        users.put("john@test.com", new ArrayList<>(Arrays.asList("test", "5", "John", "Doe")));
        users.put("jane@test.com", new ArrayList<>(Arrays.asList("test", "10", "Jane", "Doe")));

        for (String key : users.keySet())
        {
            User user = new User();
            user.setLogonId(key);

            String rawPassword = users.get(key).get(0);
            String password = PasswordUtil.digestPassword(rawPassword);

            user.setPassword(password);
            user.setEnabled(true);
            if (key.equals("bill@test.com"))
                user.setEnabled(false);
            user.setAvatarId(Long.valueOf(users.get(key).get(1)));
            user.setFirstName(users.get(key).get(2));
            user.setLastName(users.get(key).get(3));
            user.setCreatedOn(new Date());
            user.setUpdatedOn(new Date());
            long userId = EOI.insert(user, SystemTask.SEEDER);

            Role role = new Role();
            role.setLogonId(user.getLogonId());
            role.setUserId(userId);
            role.setRoleName("user");
            EOI.insert(role, SystemTask.SEEDER);

            if (user.getLogonId().equals("eric@test.com"))
            {
                role.setRoleName("admin");
                EOI.insert(role, SystemTask.SEEDER);
            }
        }
    }

    private static void createSubscriptions()
    {
        User user = User.getByUserId(1L);
        if (user != null)
        {
            List<Subscription> subs = Subscription.getByUserId(user.getId());
            if (subs.size() == 0)
            {
                Subscription sub = new Subscription();
                sub.setUserId(user.getId());
                sub.setProjectIds("1");
                EOI.insert(sub, SystemTask.SEEDER);

                sub = new Subscription();
                sub.setUserId(user.getId());
                sub.setGroupIds("1,2");
                EOI.insert(sub, SystemTask.SEEDER);

                sub = new Subscription();
                sub.setUserId(user.getId());
                sub.setSeverityIds("1");
                sub.setStatusIds("1,3");
                EOI.insert(sub, SystemTask.SEEDER);
            }
        }
    }

    private static void createChatObjects()
    {
        User user = User.getByUserId(1L);
        if (user != null)
        {
            List<ChatRoom> rooms = ChatRoom.getAll();
            if (rooms.size() == 0)
            {
                ChatRoom room = new ChatRoom();
                room.setName("General");
                room.setGroupId(1L);
                room.setDirectChat(false);
                EOI.insert(room, SystemTask.SEEDER);

                room = new ChatRoom();
                room.setName("Troubleshooting");
                room.setGroupId(1L);
                room.setDirectChat(false);
                EOI.insert(room, SystemTask.SEEDER);

                room = new ChatRoom();
                room.setName("Off Topic");
                room.setGroupId(1L);
                room.setDirectChat(false);
                EOI.insert(room, SystemTask.SEEDER);

                ChatRoomUserMap chatRoomUserMap = new ChatRoomUserMap();
                chatRoomUserMap.setRoomId(1L);
                chatRoomUserMap.setUserId(1L);
                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);

                chatRoomUserMap = new ChatRoomUserMap();
                chatRoomUserMap.setRoomId(1L);
                chatRoomUserMap.setUserId(2L);
                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);

                chatRoomUserMap = new ChatRoomUserMap();
                chatRoomUserMap.setRoomId(1L);
                chatRoomUserMap.setUserId(3L);
                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);

                chatRoomUserMap = new ChatRoomUserMap();
                chatRoomUserMap.setRoomId(1L);
                chatRoomUserMap.setUserId(4L);
                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);

                chatRoomUserMap = new ChatRoomUserMap();
                chatRoomUserMap.setRoomId(2L);
                chatRoomUserMap.setUserId(1L);
                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);

                chatRoomUserMap = new ChatRoomUserMap();
                chatRoomUserMap.setRoomId(2L);
                chatRoomUserMap.setUserId(2L);
                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);

                chatRoomUserMap = new ChatRoomUserMap();
                chatRoomUserMap.setRoomId(3L);
                chatRoomUserMap.setUserId(1L);
                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);

                ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
                chatRoomMessage.setRoomId(1L);
                chatRoomMessage.setUserId(1L);
                chatRoomMessage.setAuthor("eric");
                chatRoomMessage.setContents("Welcome to room 1");
                chatRoomMessage.setTimestamp(new Date());
                EOI.insert(chatRoomMessage, SystemTask.SEEDER);

                chatRoomMessage = new ChatRoomMessage();
                chatRoomMessage.setRoomId(2L);
                chatRoomMessage.setUserId(1L);
                chatRoomMessage.setAuthor("eric");
                chatRoomMessage.setContents("Welcome to room 2");
                chatRoomMessage.setTimestamp(new Date());
                EOI.insert(chatRoomMessage, SystemTask.SEEDER);
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
