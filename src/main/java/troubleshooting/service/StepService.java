package troubleshooting.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vaadin.flow.spring.annotation.SpringComponent;
import troubleshooting.dao.QStep;
import troubleshooting.dao.QWorkflow;
import troubleshooting.dao.Step;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringComponent
@Repository
public class StepService {
    @PersistenceContext
    private EntityManager entityManager;

    private QStep step = QStep.step;
    private QWorkflow workflow = QWorkflow.workflow;
    private JPAQueryFactory queryFactory;
    private JPAQuery query;

    public List<Step> getAllSteps() {
        query = new JPAQuery<>(entityManager);
        return query.from(step).fetch();
    }

    public List<Step> findByWorkflowId(Long id) {
        queryFactory = new JPAQueryFactory(entityManager);
        query = new JPAQuery<>(entityManager);
        return queryFactory.selectFrom(step).where(step.workflows.any().id.eq(id)).fetch();
    }

}
