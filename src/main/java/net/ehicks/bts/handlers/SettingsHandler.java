package net.ehicks.bts.handlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SettingsHandler
{
    @GetMapping("/settings/form")
    public ModelAndView showSettings()
    {
        return new ModelAndView("/webroot/settings.jsp");
    }
}
