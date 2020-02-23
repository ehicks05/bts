package net.ehicks.bts.handlers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class ChatHandler
{
    @GetMapping("/chat/form")
    public ModelAndView showProfile()
    {
        return new ModelAndView("chat");
    }
}
