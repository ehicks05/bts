package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.beans.*;
import net.ehicks.bts.security.PasswordEncoder;
import net.ehicks.bts.util.PdfCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserHandler
{
    private static final Logger log = LoggerFactory.getLogger(AdminHandler.class);

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private ProjectRepository projectRepository;
    private AvatarRepository avatarRepository;
    private PasswordEncoder passwordEncoder;
    private BtsSystemRepository btsSystemRepository;

    public UserHandler(UserRepository userRepository, GroupRepository groupRepository,
                       ProjectRepository projectRepository, AvatarRepository avatarRepository,
                       PasswordEncoder passwordEncoder, BtsSystemRepository btsSystemRepository)
    {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.projectRepository = projectRepository;
        this.avatarRepository = avatarRepository;
        this.passwordEncoder = passwordEncoder;
        this.btsSystemRepository = btsSystemRepository;
    }

    @GetMapping("/admin/users/form")
    public ModelAndView showManageUsers()
    {
        return new ModelAndView("admin/users")
                .addObject("users", userRepository.findAll());
    }

    @PostMapping("/admin/users/create")
    public ModelAndView createUser(@RequestParam String username, @RequestParam String password,
                                   @RequestParam String firstName, @RequestParam String lastName)
    {
        ModelAndView mav = new ModelAndView("redirect:/admin/users/form");

        if (userRepository.findByUsername(username).isPresent()) {
            return mav;
        }

        userRepository.save(
                new User(0, username, passwordEncoder.encoder().encode(password), firstName, lastName,
                        btsSystemRepository.findFirstBy().getDefaultAvatar()));

        return new ModelAndView("redirect:/admin/users/form");
    }

    @GetMapping("/admin/users/delete")
    public ModelAndView deleteUser(@RequestParam Long userId)
    {
        userRepository.findById(userId).ifPresent(user -> userRepository.delete(user));
        return new ModelAndView("redirect:/admin/users/form");
    }

    @GetMapping("/admin/users/print")
    @ResponseBody
    public ResponseEntity<byte[]> printUsers(@AuthenticationPrincipal User authUser)
    {
        List<List<Object>> userData = new ArrayList<>();
        userData.add(Arrays.asList("Object Id", "Logon Id", "Last", "First", "Created On"));

        for (User user : userRepository.findAll())
            userData.add(Arrays.asList(user.getId(), user.getUsername(),
                    user.getLastName(), user.getFirstName(), user.getCreatedOn()));

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) PdfCreator.createPdf(authUser.getUsername(), "Users Report", "", userData);

        return outputStream != null
                ? ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(outputStream.toByteArray())
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/users/modify/form")
    public ModelAndView showModifyUser(@RequestParam Long userId)
    {
        List<Long> userProjects = projectRepository.findByUsers_Id(userId)
                .stream().map(Project::getId).collect(Collectors.toList());
        return new ModelAndView("admin/modifyUser")
                .addObject("user", userRepository.findById(userId).get())
                .addObject("groups", groupRepository.findAll())
                .addObject("projects", projectRepository.findAll())
                .addObject("userProjects", userProjects)
                .addObject("publicAvatars", avatarRepository.findAllByPublicUseTrue());
    }

    @PostMapping("/admin/users/modify/modify")
    public ModelAndView modifyUser(@RequestParam Long userId,
                                   @RequestParam String logonId,
//                                   @RequestParam Long avatarId,
                                   @RequestParam String firstName,
                                   @RequestParam String lastName,
                                   @RequestParam Boolean enabled,
                                   @RequestParam List<Long> groupIds,
                                   @RequestParam List<Long> projectIds)
    {
        userRepository.findById(userId).ifPresent(user -> {
            user.setUsername(logonId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(enabled);
//            avatarRepository.findById(avatarId).ifPresent(user::setAvatar);
            user.setGroups(groupRepository.findByIdIn(groupIds));
            user.setProjects(projectRepository.findByIdIn(projectIds));
            userRepository.save(user);
        });

        return new ModelAndView("redirect:/admin/users/modify/form?userId=" + userId);
    }

    @PostMapping("/admin/users/changePassword")
    public ModelAndView changePassword(@RequestParam Long userId, @RequestParam String password)
    {
        userRepository.findById(userId).ifPresent(user -> {
            if (password.length() > 0)
            {
                user.setPassword(passwordEncoder.encoder().encode(password));
                userRepository.save(user);
            }
        });

        return new ModelAndView("redirect:/admin/users/modify/form?userId=" + userId);
    }
}
