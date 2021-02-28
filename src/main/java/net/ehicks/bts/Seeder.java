package net.ehicks.bts;

import kotlin.Pair;
import net.ehicks.bts.beans.*;
import net.ehicks.bts.security.PasswordEncoder;
import net.ehicks.bts.util.CommonIO;
import net.ehicks.common.Timer;
import org.hibernate.exception.SQLGrammarException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Component
public class Seeder
{
    private final Logger log = LoggerFactory.getLogger(Seeder.class);

    private int issueCount = (int) Math.pow(1_024, 1);
    private Random r = new Random();

    @Value("${admin_username}")
    String defaultAdminUsername;

    @Value("${admin_password}")
    String defaultAdminPassword;

    private List<String> latin = Arrays.asList("annus", "ante meridiem", "aqua", "bene", "canis", "caput", "circus", "cogito",
            "corpus", "de facto", "deus", "ego", "equus", "ergo", "est", "hortus", "id", "in", "index", "iris", "latex",
            "legere", "librarium", "locus", "magnus", "mare", "mens", "murus", "musica", "nihil", "non", "nota", "novus",
            "opus", "orbus", "placebo", "post", "post meridian", "primus", "pro", "sanus", "solus", "sum", "tacete",
            "tempus", "terra", "urbs", "veni", "vici", "vidi");

    private SubscriptionRepository subscriptionRepository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private IssueTypeRepository issueTypeRepository;
    private ProjectRepository projectRepository;
    private StatusRepository statusRepository;
    private SeverityRepository severityRepository;
    private BtsSystemRepository btsSystemRepository;
    private IssueFormRepository issueFormRepository;
    private DBFileRepository dbFileRepository;
    private AvatarRepository avatarRepository;
    private IssueRepository issueRepository;
    private CommentRepository commentRepository;
    private AttachmentRepository attachmentRepository;
    private RoleRepository roleRepository;
    private ChatRoomRepository chatRoomRepository;
    private PasswordEncoder passwordEncoder;
    private TikaService tikaService;
    private EntityManagerFactory entityManagerFactory;
    private IssueEventRepository issueEventRepository;

    public Seeder(SubscriptionRepository subscriptionRepository, UserRepository userRepository, 
                  GroupRepository groupRepository, IssueTypeRepository issueTypeRepository, 
                  ProjectRepository projectRepository, StatusRepository statusRepository, 
                  SeverityRepository severityRepository, BtsSystemRepository btsSystemRepository,
                  IssueFormRepository issueFormRepository, DBFileRepository dbFileRepository,
                  AvatarRepository avatarRepository, IssueRepository issueRepository,
                  CommentRepository commentRepository, AttachmentRepository attachmentRepository,
                  RoleRepository roleRepository, ChatRoomRepository chatRoomRepository,
                  PasswordEncoder passwordEncoder, TikaService tikaService,
                  EntityManagerFactory entityManagerFactory, IssueEventRepository issueEventRepository)
    {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.projectRepository = projectRepository;
        this.statusRepository = statusRepository;
        this.severityRepository = severityRepository;
        this.btsSystemRepository = btsSystemRepository;
        this.issueFormRepository = issueFormRepository;
        this.dbFileRepository = dbFileRepository;
        this.avatarRepository = avatarRepository;
        this.issueRepository = issueRepository;
        this.commentRepository = commentRepository;
        this.attachmentRepository = attachmentRepository;
        this.roleRepository = roleRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.passwordEncoder = passwordEncoder;
        this.tikaService = tikaService;
        this.entityManagerFactory = entityManagerFactory;
        this.issueEventRepository = issueEventRepository;
    }
    
    public String buildLatin(int maxLength)
    {
        StringBuilder sb = new StringBuilder();
        int words = r.nextInt(maxLength) + 1;
        for (int j = 0; j < words; j++)
        {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(latin.get(r.nextInt(latin.size())));
        }

        return sb.toString();
    }

    @Transactional
    void installExtensions()
    {
        try
        {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            Arrays.asList("pg_trgm", "pg_stat_statements")
                    .forEach(extension -> {
                        Query query = entityManager.createNativeQuery("select count(*) from pg_extension where extname='" + extension + "';");
                        BigInteger result = (BigInteger) query.getSingleResult();
                        if (result == null || result.equals(BigInteger.ZERO))
                        {
                            log.info("installing extension " + extension + "...");
                            entityManager.getTransaction().begin();
                            query = entityManager.createNativeQuery("CREATE EXTENSION " + extension + ";");
                            query.executeUpdate();
                            entityManager.getTransaction().commit();
                        }
                        else
                            log.info("extension " + extension + " already installed.");
                    });
        }
        catch (SQLGrammarException e)
        {
            log.error(e.getLocalizedMessage());
        }
    }

    void seed(boolean seedDbIfEmpty)
    {
        if (!seedDbIfEmpty)
            return;
        
        if (issueRepository.count() != 0)
            return;

        log.info("Seeding dummy data");
        Timer timer = new Timer();

        installExtensions();

        // no dependencies

        createDBFilesAndAvatars(); // use in production
        log.info(timer.printDuration("createDBFiles"));

        createStatuses(); // use in production
        log.info(timer.printDuration("createStatuses"));

        createSeverities(); // use in production
        log.info(timer.printDuration("createSeverities"));

        createIssueTypes(); // use in production
        log.info(timer.printDuration("createIssueTypes"));

        createDefaultRoles();  // use in production
        log.info(timer.printDuration("createDefaultRoles"));

        createProjects();
        log.info(timer.printDuration("createProjects"));

        // some dependencies

        createBtsSystem();  // use in production
        log.info(timer.printDuration("createBtsSystem"));

        createUsers();
        log.info(timer.printDuration("createUsers"));

        createGroups();
        log.info(timer.printDuration("createGroups"));

        createGroupMaps();
        log.info(timer.printDuration("createGroupMaps"));

        createProjectMaps();
        log.info(timer.printDuration("createProjectMaps"));

        createIssueForms();
        log.info(timer.printDuration("createIssueForms"));

        createSubscriptions();
        log.info(timer.printDuration("createSubscriptions"));

        createChatObjects();
        log.info(timer.printDuration("createChatObjects"));

        createIssues();
        log.info(timer.printDuration("createIssues"));

        createAttachments();
        log.info(timer.printDuration("createAttachments"));

        createAdditionalIssueIndexes();

        log.info(timer.printDuration("Done seeding dummy data"));
    }

    @Transactional
    void createAdditionalIssueIndexes() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        entityManager.getTransaction().begin();
        Query query = entityManager.createNativeQuery("CREATE INDEX TRGM_IDX_ISSUE_TITLE ON ISSUE USING gin (title gin_trgm_ops);");
        query.executeUpdate();

        query = entityManager.createNativeQuery("CREATE INDEX TRGM_IDX_ISSUE_DESCRIPTION ON ISSUE USING gin (description gin_trgm_ops);");
        query.executeUpdate();

        query = entityManager.createNativeQuery("CREATE INDEX IDX_ISSUE_CREATED_ON ON ISSUE (created_on);");
        query.executeUpdate();

        query = entityManager.createNativeQuery("CREATE INDEX IDX_ISSUE_LAST_UPDATED_ON ON ISSUE (last_updated_on);");
        query.executeUpdate();

        entityManager.getTransaction().commit();
    }

    private void createBtsSystem()
    {
        BtsSystem btsSystem;
        if (btsSystemRepository.findAll().isEmpty())
            btsSystem = new BtsSystem(0, "", "", avatarRepository.findByName("no_avatar.png"), "", "noreply@puffin.ehicks.net", "NO_REPLY");
        else
            btsSystem = btsSystemRepository.findFirstBy();
        
        btsSystem.setSiteName("BugCo Industries");
        btsSystem.setLogonMessage("<span>Welcome to Puffin Issue Tracker.<br>Please contact Eric for a demo.</span>");
        btsSystem.setTheme("default");
        btsSystemRepository.save(btsSystem);
    }

    private void createIssueForms()
    {
        userRepository.findAll().forEach(user -> {
            IssueForm issueForm = new IssueForm(0, user, "All Issues", true, 0);
            issueFormRepository.save(issueForm);

            issueForm = new IssueForm(0, user, "Assigned To Me", true, 1);
            issueForm.getAssignees().add(user);
            issueFormRepository.save(issueForm);

            if (user.getUsername().equals("eric@test.com"))
            {
                issueForm = new IssueForm(0, user, "Readington's Issues", true, 2);
                issueForm.getGroups().add(groupRepository.findByName("Readington"));
                issueFormRepository.save(issueForm);

                issueForm = new IssueForm(0, user, "Issues with the word 'vici'", true, 3);
                issueForm.setContainsText("vici");
                issueFormRepository.save(issueForm);

                issueForm = new IssueForm(0, user, "Reopened Issues", true, 4);
                issueForm.getStatuses().add(statusRepository.findByName("Re-opened"));
                issueFormRepository.save(issueForm);

                issueForm = new IssueForm(0, user, "All Cinemang Issues", true, 5);
                issueForm.getProjects().add(projectRepository.findByName("Cinemang"));
                issueFormRepository.save(issueForm);
            }
        });
    }

    private void createDBFilesAndAvatars()
    {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] avatars = null;
        try {
            avatars = resolver.getResources("classpath:/static/images/avatars/png/**");
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }

        if (avatars == null)
        {
            log.error("No Avatars found");
            return;
        }

        for (Resource avatarFile : avatars)
        {
            if (avatarFile.exists())
            {
                String name = avatarFile.getFilename();
                if (name == null || name.isEmpty())
                    continue;

                byte[] content = new byte[0];
                try (InputStream inputStream = avatarFile.getInputStream())
                {
                    content = inputStream.readAllBytes();
                }
                catch (IOException e)
                {
                    log.error(e.getMessage(), e);
                }

                DBFile dbFile = dbFileRepository.save(new DBFile(0, content, Arrays.hashCode(content), tikaService.detect(content, name), null));
                avatarRepository.save(new Avatar(0, name, dbFile, true));
            }
        }

        if (dbFileRepository.count() == 0) log.error("No DBFiles were created.");
        if (avatarRepository.count() == 0) log.error("No Avatars were created.");
    }

    private void createComments()
    {
        List<User> users = userRepository.findAll();
        List<Comment> comments = new ArrayList<>();

        long count = issueRepository.count();
        long pageSize = 1000;
        long pages = (count / pageSize);

        AtomicLong start = new AtomicLong(System.currentTimeMillis());

        AtomicLong issueIndex = new AtomicLong(0);
        LongStream.range(0, pages).forEach(page -> {

            Pageable pageable = PageRequest.of((int)page, (int)pageSize, Sort.by("id"));
            List<Issue> issues = issueRepository.findAll(pageable).getContent();

            for (Issue issue : issues)
            {
                if (System.currentTimeMillis() - start.get() > 10_000)
                {
                    start.set(System.currentTimeMillis());
                    log.info("  creating comments for issue " + issueIndex.get() + "/" + issueCount + "...");
                }

                for (int i = 0; i < r.nextInt(8); i++)
                    comments.add(new Comment(0, issue, users.get(r.nextInt(users.size())),
                            buildLatin(32), issue.getGroup(), LocalDateTime.now(), LocalDateTime.now()));

                if (comments.size() == 10_000) {
                    commentRepository.saveAll(comments);
                    comments.clear();
                }
                issueIndex.incrementAndGet();
            }
        });

        commentRepository.saveAll(comments);
    }

    private void createProjectMaps()
    {
        List<User> users = userRepository.findAll();
        List<Project> projects = projectRepository.findAll();

        users.forEach(user -> {
            Project project = projects.get(r.nextInt(projects.size()));

            user.getProjects().add(project);
            project.getUsers().add(user);

            userRepository.save(user);
            projectRepository.save(project);
        });
    }

    private void createAttachments()
    {
        File imgDir = null;
        try {
            imgDir = new ClassPathResource("/static/images/avatars/png").getFile();
        } catch (IOException e) {

        }

        if (imgDir != null && imgDir.exists() && imgDir.isDirectory())
        {
            List<File> images = Arrays.asList(imgDir.listFiles());
            Collections.sort(images);
            for (File img : images)
            {
                if (img.exists() && img.isFile() && img.getName().contains("avatar-01.png"))
                {
                    byte[] content = new byte[0];
                    try
                    {
                        content = Files.readAllBytes(img.toPath());
                    }
                    catch (IOException e)
                    {
                        log.error(e.getMessage(), e);
                    }

                    byte[] scaledBytes = new byte[0];
                    try
                    {
                        scaledBytes = CommonIO.getThumbnail(new FileInputStream(img));
                    }
                    catch (IOException e)
                    {
                        log.error(e.getMessage(), e);
                    }

                    DBFile thumbnail = dbFileRepository.save(new DBFile(0, scaledBytes, Arrays.hashCode(scaledBytes), tikaService.detect(scaledBytes, img.getName()), null));

                    DBFile dbFile = new DBFile(0, content, Arrays.hashCode(content), tikaService.detect(content, img.getName()), thumbnail);
                    dbFile = dbFileRepository.save(dbFile);

                    Issue issue = issueRepository.findFirstByOrderById();
                    attachmentRepository.save(new Attachment(0, img.getName(), issue, dbFile, LocalDateTime.now(), issue.getReporter()));

                    issueEventRepository.save(new IssueEvent(0, issue.getReporter(), issue, EventType.ADD, "attachment", "", img.getName()));
                }
            }
        }
    }

    private void createIssues()
    {
        List<String> adjectives = Arrays.asList("deactivated", "decommissioned", "ineffective", "ineffectual", "useless "+
                        "inoperable", "unusable", "unworkable arrested", "asleep", "dormant", "fallow", "idle", "inert",
                "latent", "lifeless", "nonproductive", "quiescent", "sleepy", "stagnating", "unproductive", "vegetating");

        List<String> nouns = Arrays.asList("feature", "screen", "page", "report", "functionality");

        List<String> questions = Arrays.asList("Is", "How come I'm getting", "Why is", "What's with the", "Fix this",
                "Help with", "Problem regarding");

        List<Comment> comments = new ArrayList<>();
        List<IssueEvent> issueEvents = new ArrayList<>();

        List<Project> projects = projectRepository.findAll();
        List<Group> groups = groupRepository.findAll();
        List<User> users = userRepository.findAll();
        List<IssueType> issueTypes = issueTypeRepository.findAll();
        List<Severity> severities = severityRepository.findAll();
        List<Status> statuses = statusRepository.findAll();

        List<Issue> issues = new ArrayList<>();
        AtomicLong start = new AtomicLong(System.currentTimeMillis());
        IntStream.range(0, issueCount).forEach(i ->
        {
            if (System.currentTimeMillis() - start.get() > 1000)
            {
                start.set(System.currentTimeMillis());
                log.info("  creating issue " + i + "/" + issueCount + "...");
            }
            Issue issue = new Issue();
            issue.setTitle(buildIssueTitle(adjectives, nouns, questions));
            issue.setDescription("<p>" + buildLatin(32) + "</p>");
            issue.setCreatedOn(getRandomLocalDateTime(false));
            issue.setLastUpdatedOn(issue.getCreatedOn());
            issue.setGroup(groups.get(r.nextInt(groups.size())));
            issue.setIssueType(issueTypes.get(r.nextInt(issueTypes.size())));
            issue.setProject(projects.get(r.nextInt(projects.size())));
            issue.setAssignee(users.get(r.nextInt(users.size())));
            issue.setReporter(users.get(r.nextInt(users.size())));
            issue.setSeverity(severities.get(r.nextInt(severities.size())));
            issue.setStatus(statuses.get(r.nextInt(statuses.size())));

            issue.setWatchers(r.ints(3, 0, users.size())
                    .mapToObj(users::get).collect(Collectors.toSet()));

            issues.add(issue);
            if (issues.size() >= 100) {
                issueRepository.saveAll(issues);

                issues.forEach(savedIssue -> makeComments(savedIssue, users, comments, issueEvents));

                issues.clear();
            }
        });

        issueRepository.saveAll(issues);

        issues.forEach(savedIssue -> makeComments(savedIssue, users, comments, issueEvents));
        commentRepository.saveAll(comments);
    }

    private void makeComments(Issue savedIssue, List<User> users, List<Comment> comments, List<IssueEvent> issueEvents)
    {
        if (r.nextDouble() < .1) // create comments on 1/10 issues
            for (int j = 0; j < r.nextInt(3) + 1; j++) // create 1-3 comments
            {
                Comment comment = new Comment(0, savedIssue, users.get(r.nextInt(users.size())),
                        buildLatin(32), savedIssue.getGroup(), LocalDateTime.now(), LocalDateTime.now());
                comments.add(comment);

                issueEvents.add(new IssueEvent(0, comment.getAuthor(), savedIssue, EventType.ADD, "comment", "", comment.getContent()));
            }

        if (comments.size() >= 100) {
            commentRepository.saveAll(comments);
            comments.clear();

            issueEventRepository.saveAll(issueEvents);
            issueEvents.clear();
        }
    }

    @NotNull
    private String buildIssueTitle(List<String> adjectives, List<String> nouns, List<String> questions)
    {
        StringBuilder t = new StringBuilder();
        t.append(adjectives.get(r.nextInt(adjectives.size())));
        t.append(" ").append(nouns.get(r.nextInt(nouns.size())));

        if (r.nextBoolean()) t.insert(0, questions.get(r.nextInt(questions.size())) + " ");
        if (r.nextBoolean()) t.append("!");
        if (r.nextBoolean()) t.append("?");
        if (r.nextInt(5) <= 1) t.append("...");
        return r.nextBoolean() ? t.toString().toUpperCase() : t.toString();
    }

    private void createIssueTypes()
    {
        Arrays.asList("Bug", "New Feature", "Question", "Data Issue")
                .forEach(name -> issueTypeRepository.save(new IssueType(0, name)));
    }

    private void createSeverities()
    {
        Arrays.asList("High", "Medium", "Low")
                .forEach(name -> severityRepository.save(new Severity(0, name)));
    }

    private void createStatuses()
    {
        Arrays.asList("Open", "Closed", "Re-opened")
                .forEach(name -> statusRepository.save(new Status(0, name)));
    }

    private void createProjects()
    {
        new ArrayList<>(Arrays.asList(
                new Pair<>("Loon", "LO"),
                new Pair<>("Puffin", "PU"),
                new Pair<>("Cinemang", "CM"),
                new Pair<>("TabHunter", "TH")
        )).forEach(pair -> projectRepository.save(
                new Project(0, pair.getFirst(), pair.getSecond())
        ));
    }

    private void createGroupMaps()
    {
        Group admin = groupRepository.findByName("Admin");
        Group support = groupRepository.findByName("Support");
        List<Group> customerGroups = groupRepository.findAll();
        customerGroups.remove(admin);
        customerGroups.remove(support);

        userRepository.findAll().forEach(user -> {
            Group group;
            switch (user.getUsername()) {
                case "eric@test.com": group = admin; break;
                case "steve@test.com": group = support; break;
                default: group = customerGroups.get(r.nextInt(customerGroups.size()));
            }

            user.getGroups().add(group);
            group.getUsers().add(user);

            userRepository.save(user);
            groupRepository.save(group);
        });
    }

    private void createGroups()
    {
        Arrays.asList("Readington", "Bridgewater", "Califon", "Flemington")
                .forEach(name -> groupRepository.save(
                        new Group(0, name, false, false)
                ));

        groupRepository.save(new Group(0, "Support", false, true));
        groupRepository.save(new Group(0, "Admin", true, false));
    }

    public void createDefaultRoles()
    {
        if (roleRepository.count() > 0)
            return;

        Arrays.asList("ROLE_USER", "ROLE_ADMIN")
                .forEach((r) -> {
                    Role role = new Role();
                    role.setRole(r);
                    roleRepository.save(role);
                });
    }

    private void createUsers()
    {
        List<UserData> users = new ArrayList<>(Arrays.asList(
                new UserData("steve@test.com", "steve", "Steve", "Tester", true, false),
                new UserData("jill@test.com", "test", "Jill", "Jones", true, false),
                new UserData("bill@test.com", "test", "Bill", "Smith", false, false),
                new UserData("john@test.com", "test", "John", "Doe", true, false),
                new UserData("jane@test.com", "test", "Jane", "Doe", true, false)
        ));

        if (defaultAdminUsername != null && !defaultAdminUsername.isEmpty() &&
                defaultAdminPassword != null && !defaultAdminPassword.isEmpty())
            users.add(new UserData(defaultAdminUsername, defaultAdminPassword, "Admin", "Admin", true, true));
        else
            users.add(new UserData("eric@test.com", "eric", "Eric", "Tester", true, true));

        Role userRole = roleRepository.findByRole("ROLE_USER");
        Role adminRole = roleRepository.findByRole("ROLE_ADMIN");

        List<Avatar> avatars = avatarRepository.findAll();

        for (UserData userData : users)
        {
            String password = passwordEncoder.encoder().encode(userData.password);
            Avatar avatar = avatars.get(r.nextInt(avatars.size()));

            User user = new User(0, userData.username, password,
                    userData.first, userData.last, userData.enabled, LocalDateTime.now(), avatar);

            user.getRoles().add(userRole);
            if (userData.admin)
            {
                user.getRoles().add(adminRole);
                adminRole.getUsers().add(user);
                roleRepository.save(adminRole);
            }
            user = userRepository.save(user);

            userRole.getUsers().add(user);
            roleRepository.save(userRole);
        }
    }

    static class UserData
    {
        String username;
        String password;
        String first;
        String last;
        boolean enabled;
        boolean admin;

        public UserData(String username, String password, String first, String last, boolean enabled, boolean admin)
        {
            this.username = username;
            this.password = password;
            this.first = first;
            this.last = last;
            this.enabled = enabled;
            this.admin = admin;
        }
    }

    private void createSubscriptions()
    {
        List<Project> projects = projectRepository.findAll();
        List<Group> groups = groupRepository.findAll();
        List<Status> statuses = statusRepository.findAll();
        List<Severity> severities = severityRepository.findAll();

        userRepository.findAll().forEach(user -> {
            List<Subscription> subs = subscriptionRepository.findByUser_Id(user.getId());
            if (subs.size() == 0)
            {
                Subscription sub = new Subscription(user);
                sub.getProjects().add(projects.get(r.nextInt(projects.size())));
                subscriptionRepository.save(sub);

                sub = new Subscription(user);
                sub.getGroups().add(groups.get(r.nextInt(groups.size())));
                sub.getGroups().add(groups.get(r.nextInt(groups.size())));
                subscriptionRepository.save(sub);

                sub = new Subscription(user);
                sub.getSeverities().add(severities.get(r.nextInt(severities.size())));;
                sub.getStatuses().add(statuses.get(r.nextInt(statuses.size())));
                sub.getStatuses().add(statuses.get(r.nextInt(statuses.size())));
                subscriptionRepository.save(sub);
            }
        });
    }

    private void createChatObjects()
    {
        userRepository.findById(1L).ifPresent(user -> {
            List<ChatRoom> rooms = chatRoomRepository.findAll();
            if (rooms.size() == 0)
            {
                ChatRoom room = new ChatRoom();
                room.setName("General");
                room.setGroup(groupRepository.findById(1L).get());
                room.setDirectChat(false);
                chatRoomRepository.save(room);

                room = new ChatRoom();
                room.setName("Troubleshooting");
                room.setGroup(groupRepository.findById(1L).get());
                room.setDirectChat(false);
                chatRoomRepository.save(room);

                room = new ChatRoom();
                room.setName("Off Topic");
                room.setGroup(groupRepository.findById(1L).get());
                room.setDirectChat(false);
                chatRoomRepository.save(room);

//                ChatRoomUserMap chatRoomUserMap = new ChatRoomUserMap();
//                chatRoomUserMap.setRoomId(1L);
//                chatRoomUserMap.setUserId(1L);
//                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);
//
//                chatRoomUserMap = new ChatRoomUserMap();
//                chatRoomUserMap.setRoomId(1L);
//                chatRoomUserMap.setUserId(2L);
//                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);
//
//                chatRoomUserMap = new ChatRoomUserMap();
//                chatRoomUserMap.setRoomId(1L);
//                chatRoomUserMap.setUserId(3L);
//                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);
//
//                chatRoomUserMap = new ChatRoomUserMap();
//                chatRoomUserMap.setRoomId(1L);
//                chatRoomUserMap.setUserId(4L);
//                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);
//
//                chatRoomUserMap = new ChatRoomUserMap();
//                chatRoomUserMap.setRoomId(2L);
//                chatRoomUserMap.setUserId(1L);
//                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);
//
//                chatRoomUserMap = new ChatRoomUserMap();
//                chatRoomUserMap.setRoomId(2L);
//                chatRoomUserMap.setUserId(2L);
//                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);
//
//                chatRoomUserMap = new ChatRoomUserMap();
//                chatRoomUserMap.setRoomId(3L);
//                chatRoomUserMap.setUserId(1L);
//                EOI.insert(chatRoomUserMap, SystemTask.SEEDER);
//
//                ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
//                chatRoomMessage.setRoomId(1L);
//                chatRoomMessage.setUserId(1L);
//                chatRoomMessage.setAuthor("eric");
//                chatRoomMessage.setContents("Welcome to room 1");
//                chatRoomMessage.setTimestamp(new Date());
//                EOI.insert(chatRoomMessage, SystemTask.SEEDER);
//
//                chatRoomMessage = new ChatRoomMessage();
//                chatRoomMessage.setRoomId(2L);
//                chatRoomMessage.setUserId(1L);
//                chatRoomMessage.setAuthor("eric");
//                chatRoomMessage.setContents("Welcome to room 2");
//                chatRoomMessage.setTimestamp(new Date());
//                EOI.insert(chatRoomMessage, SystemTask.SEEDER);
            }
        });
    }

    public static LocalDateTime getRandomLocalDateTime(boolean future) {
        long firstCommit = LocalDateTime.of(2016, 7, 11, 20, 42).toEpochSecond(ZoneOffset.UTC);
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long yearFromNow = LocalDateTime.now().plusYears(1).toEpochSecond(ZoneOffset.UTC);
        long random = ThreadLocalRandom
                .current()
                .nextLong(future ? now : firstCommit, future ? yearFromNow : now);

        return LocalDateTime.ofEpochSecond(random, 0, ZoneOffset.UTC);
    }
}
