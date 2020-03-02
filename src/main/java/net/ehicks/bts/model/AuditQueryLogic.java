package net.ehicks.bts.model;

import net.ehicks.bts.beans.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditQueryLogic
{
    IssueRepository issueRepository;
    EntityManager em;

    public AuditQueryLogic(IssueRepository issueRepository, EntityManager em)
    {
        this.issueRepository = issueRepository;
        this.em = em;
    }

    public SearchResult<IssueEvent> query(IssueEventForm searchForm)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IssueEvent> query = cb.createQuery(IssueEvent.class);
        Root<IssueEvent> root = query.from(IssueEvent.class);

        List<Predicate> predicates = new ArrayList<>();

        // group-based access control
        if (!searchForm.getUser().isAdmin())
        {
            Path<Group> path = root.get("group");
            List<Long> userGroupIds = searchForm.getUser().getGroups().stream().map(Group::getId).collect(Collectors.toList());
            Predicate inClause = path.in(userGroupIds);
            predicates.add(inClause);
        }

        if (searchForm.getIssueId() != null)
            predicates.add(cb.equal(root.get("issue").get("id"), searchForm.getIssueId()));
        if (searchForm.getPropertyName().length() > 0)
            predicates.add(cb.equal(root.get("propertyName"), searchForm.getPropertyName()));
        if (searchForm.getEventUser() != null)
            predicates.add(cb.equal(root.get("eventUser"), searchForm.getEventUser()));
        if (searchForm.getFromEventDate() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("fromEventDate"), searchForm.getFromEventDate()));
        if (searchForm.getToEventDate() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("toEventDate"), searchForm.getToEventDate()));

        List<Order> orderList = new ArrayList<>();

        if (searchForm.getSortDirection().equals("asc"))
            orderList.add(cb.asc(root.get(searchForm.getSortColumn())));
        else
            orderList.add(cb.desc(root.get(searchForm.getSortColumn())));

        query.select(root)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(orderList);

        List<IssueEvent> results = em.createQuery(query)
                .setFirstResult((Integer.parseInt(searchForm.getPage()) - 1) * 20)
                .setMaxResults(20)
                .getResultList();

        // count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(IssueEvent.class)))
                .where(predicates.toArray(new Predicate[]{}));

        long size = em.createQuery(countQuery).getSingleResult();

        return new SearchResult<>(results, size, 20, searchForm.getPage());
    }
}
