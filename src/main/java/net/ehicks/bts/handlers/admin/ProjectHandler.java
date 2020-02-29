package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.beans.Project;
import net.ehicks.bts.beans.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProjectHandler
{
    private ProjectRepository projectRepository;

    public ProjectHandler(ProjectRepository projectRepository)
    {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/admin/projects/form")
    public ModelAndView showManageProjects()
    {
        return new ModelAndView("admin/projects")
                .addObject("projects", projectRepository.findAll());
    }

    @PostMapping("/admin/projects/create")
    public ModelAndView createProject(@RequestParam String fldName, @RequestParam String fldPrefix)
    {
        projectRepository.save(new Project(0, fldName, fldPrefix));
        return new ModelAndView("redirect:/admin/projects/form");
    }

    @GetMapping("/admin/projects/delete")
    public ModelAndView deleteProject(@RequestParam Long projectId)
    {
        projectRepository.findById(projectId)
                .ifPresent(project -> projectRepository.delete(project));

        return new ModelAndView("redirect:/admin/projects/form");
    }

    @GetMapping("/admin/projects/modify/form")
    public ModelAndView showModifyProject(@RequestParam Long projectId)
    {
        return new ModelAndView("admin/modifyProject")
                .addObject("project", projectRepository.findById(projectId).orElse(null));
    }

    @PostMapping("/admin/projects/modify/modify")
    public ModelAndView modifyProject(@RequestParam Long projectId, @RequestParam String name,
                                      @RequestParam String prefix)
    {
        projectRepository.findById(projectId).ifPresent(project -> {
            project.setPrefix(prefix);
            project.setName(name);
            projectRepository.save(project);
        });

        return new ModelAndView("redirect:/admin/projects/modify/form?projectId=" + projectId);
    }
}
