package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.beans.Group;
import net.ehicks.bts.beans.GroupRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GroupHandler
{
    private GroupRepository groupRepository;

    public GroupHandler(GroupRepository groupRepository)
    {
        this.groupRepository = groupRepository;
    }

    @GetMapping("/admin/groups/form")
    public ModelAndView showManageGroups()
    {
        return new ModelAndView("admin/groups")
                .addObject("groups", groupRepository.findAll());
    }

    @PostMapping("/admin/groups/create")
    public ModelAndView createGroup(@RequestParam String fldName)
    {
        groupRepository.save(new Group(0, fldName, false, false));
        return new ModelAndView("redirect:/admin/groups/form");
    }

    @GetMapping("/admin/groups/delete")
    public ModelAndView deleteGroup(@RequestParam Long groupId)
    {
        groupRepository.findById(groupId).ifPresent(group -> groupRepository.delete(group));
        return new ModelAndView("redirect:/admin/groups/form");
    }

    @GetMapping("/admin/groups/modify/form")
    public ModelAndView showModifyGroup(@RequestParam Long groupId)
    {
        return new ModelAndView("admin/modifyGroup")
                .addObject("group", groupRepository.findById(groupId).orElse(null));
    }

    @PostMapping("/admin/groups/modify/modify")
    public ModelAndView modifyGroup(@RequestParam Long groupId, @RequestParam String name)
    {
        groupRepository.findById(groupId).ifPresent(group -> {
            group.setName(name);
            groupRepository.save(group);
        });

        return new ModelAndView("redirect:/admin/groups/modify/form?groupId=" + groupId);
    }
}
