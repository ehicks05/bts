package net.ehicks.bts.handlers;

import net.ehicks.bts.beans.CommentRepository;
import net.ehicks.bts.beans.User;
import net.ehicks.bts.beans.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileHandler
{
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    public ProfileHandler(UserRepository userRepository, CommentRepository commentRepository)
    {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/profile/form")
    public ModelAndView showProfile(@AuthenticationPrincipal User user, @RequestParam Long profileUserId)
    {
        User profileUser = userRepository.findById(profileUserId).orElse(null);
        if (profileUser == null)
            return new ModelAndView("error");

        if (user.isAdmin() || user.getGroups().stream().anyMatch(userGroups -> profileUser.getGroups().contains(userGroups)))
        {
            return new ModelAndView("profile")
                    .addObject("user", profileUser)
                    .addObject("recentComments", commentRepository.findTop10ByAuthorOrderByCreatedOnDescIdDesc(profileUser));
        }
        else
            return new ModelAndView("error");
    }
}
