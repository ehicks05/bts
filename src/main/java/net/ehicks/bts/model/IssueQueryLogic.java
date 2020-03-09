package net.ehicks.bts.model;

import net.ehicks.bts.beans.*;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IssueQueryLogic
{
    private EntityManager em;

    public IssueQueryLogic(EntityManager em)
    {
        this.em = em;
    }

    public SearchResult<Issue> query(IssueForm issueForm)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> query = cb.createQuery(Issue.class);
        Root<Issue> root = query.from(Issue.class);

        List<Predicate> predicates = new ArrayList<>();

        // group-based access control
        if (!issueForm.getUser().isAdmin())
        {
            Path<Group> path = root.get("group");
            List<Long> userGroupIds = issueForm.getUser().getGroups().stream().map(Group::getId).collect(Collectors.toList());
            Predicate inClause = path.in(userGroupIds);
            predicates.add(inClause);
        }

        if (issueForm.getContainsText().length() > 0)
        {
            Predicate titleClause = cb.like(root.get("title"), "%" + issueForm.getContainsText().toLowerCase() + "%");
            Predicate descClause = cb.like(root.get("description"), "%" + issueForm.getContainsText().toLowerCase() + "%");
            predicates.add(cb.or(titleClause, descClause));
        }

        if (issueForm.getProjects().size() > 0)
        {
            Path<Project> path = root.get("project");
            Predicate inClause = path.in(issueForm.getProjects().stream().map(Project::getId).collect(Collectors.toList()));
            predicates.add(inClause);
        }

        if (issueForm.getGroups().size() > 0)
        {
            Path<Group> path = root.get("group");
            Predicate inClause = path.in(issueForm.getGroups().stream().map(Group::getId).collect(Collectors.toList()));
            predicates.add(inClause);
        }

        if (issueForm.getSeverities().size() > 0)
        {
            Path<Severity> path = root.get("severity");
            Predicate inClause = path.in(issueForm.getSeverities().stream().map(Severity::getId).collect(Collectors.toList()));
            predicates.add(inClause);
        }

        if (issueForm.getStatuses().size() > 0)
        {
            Path<Status> path = root.get("status");
            Predicate inClause = path.in(issueForm.getStatuses().stream().map(Status::getId).collect(Collectors.toList()));
            predicates.add(inClause);
        }

        if (issueForm.getAssignees().size() > 0)
        {
            Path<Long> path = root.get("assignee");
            Predicate inClause = path.in(issueForm.getAssignees().stream().map(User::getId).collect(Collectors.toList()));
            predicates.add(inClause);
        }

        if (issueForm.getFromCreatedDate() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdOn"), issueForm.getFromCreatedDate()));
        if (issueForm.getToCreatedDate() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("createdOn"), issueForm.getToCreatedDate()));
        if (issueForm.getFromUpdatedDate() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastUpdatedOn"), issueForm.getFromUpdatedDate()));
        if (issueForm.getToUpdatedDate() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("lastUpdatedOn"), issueForm.getToUpdatedDate()));

        List<Order> orderList = new ArrayList<>();

        if (issueForm.getSortDirection().equals("asc"))
            orderList.add(cb.asc(root.get(issueForm.getSortColumn())));
        else
            orderList.add(cb.desc(root.get(issueForm.getSortColumn())));

        query.select(root)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(orderList);

        List<Issue> results = em.createQuery(query)
                .setFirstResult((Integer.parseInt(issueForm.getPage()) - 1) * 20)
                .setMaxResults(20)
                .getResultList();

        // count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Issue.class)))
                .where(predicates.toArray(new Predicate[]{}));

        long size = em.createQuery(countQuery).getSingleResult();

        return new SearchResult<>(results, size, 20, issueForm.getPage());
    }
}
