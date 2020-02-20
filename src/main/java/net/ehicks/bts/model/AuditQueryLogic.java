package net.ehicks.bts.model;

import net.ehicks.bts.beans.Issue;
import net.ehicks.bts.beans.IssueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class AuditQueryLogic
{
    IssueRepository issueRepository;
    EntityManager entityManager;

    public AuditQueryLogic(IssueRepository issueRepository, EntityManager entityManager)
    {
        this.issueRepository = issueRepository;
        this.entityManager = entityManager;
    }

    public SearchResult<Revision<Integer, Issue>> query(AuditForm searchForm)
    {
//        List results = AuditReaderFactory.get( entityManager ).createQuery()
//                .forRevisionsOfEntityWithChanges( Issue.class, true )
//                .add( AuditEntity.id().eq( searchForm.getIssueId() ) )
//                .addOrder( AuditEntity.revisionNumber().asc() )
//                .getResultList();


//        Revisions<Integer, Issue> allRevisions = issueRepository.findRevisions(searchForm.getIssueId());

        int page = Integer.parseInt(searchForm.getPage());
        String sortColumn = searchForm.getSortColumn();

        PageRequest pageRequest = PageRequest.of(page - 1, 20, Sort.by(sortColumn));
        Page<Revision<Integer, Issue>> revisions = issueRepository.findRevisions(searchForm.getIssueId(), pageRequest);

        return new SearchResult<>(revisions.getContent(), revisions.getTotalElements(), 20, searchForm.getPage());
    }
}
