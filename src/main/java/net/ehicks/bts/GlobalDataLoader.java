package net.ehicks.bts;

import net.ehicks.bts.beans.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GlobalDataLoader
{
    BtsSystemRepository btsSystemRepository;
    ProjectRepository projectRepository;
    GroupRepository groupRepository;
    SeverityRepository severityRepository;
    StatusRepository statusRepository;
    UserRepository userRepository;
    IssueTypeRepository issueTypeRepository;

    public GlobalDataLoader(BtsSystemRepository btsSystemRepository, ProjectRepository projectRepository,
                              GroupRepository groupRepository, SeverityRepository severityRepository,
                              StatusRepository statusRepository, UserRepository userRepository,
                              IssueTypeRepository issueTypeRepository)
    {
        this.btsSystemRepository = btsSystemRepository;
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.severityRepository = severityRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.issueTypeRepository = issueTypeRepository;
    }

    public Map<String, Object> loadData()
    {
        Map<String, Object> model = new HashMap<>();
        // not sensitive
        model.put("severities", severityRepository.findAll());
        model.put("statuses", statusRepository.findAll());
        model.put("issueTypes", issueTypeRepository.findAll());
        model.put("projects", projectRepository.findAll());

        // todo what in btsSystem is public vs sensitive?
        model.put("btsSystem", btsSystemRepository.findFirstBy());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.isAdmin()) {
            model.put("users", userRepository.findAll());
            model.put("groups", groupRepository.findAll());
        }
        else {
            model.put("users", userRepository.findByGroupsIn(user.getGroups()));
            model.put("groups", groupRepository.findAllById(user.getGroupIds()));
        }

        return model;
    }
}
