package troubleshooting.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vaadin.flow.spring.annotation.SpringComponent;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import troubleshooting.dao.QStep;
import troubleshooting.dao.QWorkflow;
import troubleshooting.dao.Step;
import org.springframework.stereotype.Repository;
import troubleshooting.dto.StepDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Stream;

@SpringComponent
@Repository
public class StepService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MapperFactory mapperFactory;
    private BoundMapperFacade<Step,StepDto> mapper;
    private QStep step = QStep.step;
    private QWorkflow workflow = QWorkflow.workflow;
    private JPAQueryFactory queryFactory;
    private JPAQuery query;

    public Stream<StepDto> getAllStepsDto(String filter, int offset, int limit) {
        this.mapper = mapperFactory.getMapperFacade(Step.class, StepDto.class);
        queryFactory = new JPAQueryFactory(entityManager);
        List<Step> steps = queryFactory.selectFrom(step)
                .where(step.stepName.contains(filter))
                .offset(offset)
                .limit(limit)
                .fetch();

        return steps.stream().map(mapper::map);
    }

    public int count(String filter) {
        queryFactory = new JPAQueryFactory(entityManager);
        this.mapper = mapperFactory.getMapperFacade(Step.class, StepDto.class);
        return (int) queryFactory.selectFrom(step)
                .where(step.stepName.contains(filter))
                .fetchCount();
    }

    public List<Step> getAllSteps() {
        queryFactory = new JPAQueryFactory(entityManager);
        query = new JPAQuery<>(entityManager);
        return query.from(step).fetch();
    }

    @Transactional
    public List<Step> findByWorkflowId(Long id) {
        queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory.selectFrom(step).where(step.workflows.any().id.eq(id)).fetch();
    }
}
