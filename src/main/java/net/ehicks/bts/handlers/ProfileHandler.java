package net.ehicks.bts.handlers;

import net.ehicks.bts.beans.BtsSystemRepository;
import net.ehicks.bts.beans.CommentRepository;
import net.ehicks.bts.beans.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileHandler
{
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private BtsSystemRepository btsSystemRepository;

    public ProfileHandler(UserRepository userRepository, CommentRepository commentRepository,
                          BtsSystemRepository btsSystemRepository)
    {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.btsSystemRepository = btsSystemRepository;
    }

    @GetMapping("/profile/form")
    public ModelAndView showProfile(@RequestParam Long profileUserId)
    {
        ModelAndView mav = new ModelAndView("profile");
        userRepository.findById(profileUserId).ifPresent(profileUser -> {

            //todo check access
//            if (!userId.equals(userSession.getUserId()) &&
//                    !User.getAllVisible(userSession.getUserId()).contains(user))
//                return "/webroot/error.jsp";
//
            mav.addObject("user", profileUser);
            mav.addObject("recentComments", commentRepository.findTop10ByAuthorOrderByCreatedOnDescIdDesc(profileUser));
            mav.addObject("defaultAvatar", btsSystemRepository.findFirstBy().getDefaultAvatar());
        });

        return mav;
    }
}
