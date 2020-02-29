package net.ehicks.bts.handlers;

import net.ehicks.bts.beans.BtsSystem;
import net.ehicks.bts.beans.BtsSystemRepository;
import net.ehicks.bts.beans.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController
{
    private UserRepository userRepo;
    private BtsSystemRepository btsSystemRepo;
    private PasswordEncoder passwordEncoder;

    public LoginController(UserRepository userRepo, BtsSystemRepository btsSystemRepo,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.btsSystemRepo = btsSystemRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute("btsSystem")
    public BtsSystem loonSystem()
    {
        if (btsSystemRepo.findAll().isEmpty())
            return new BtsSystem(0, "Loading...", "Loading...");
        return btsSystemRepo.findFirstBy();
    }

    @GetMapping
    public ModelAndView loginForm(@RequestParam(required = false) String error) {
        ModelAndView mav = new ModelAndView("login");
        if (error != null) {
            mav.addObject("error", "Invalid username and password!");
        }
        return mav;
    }
}