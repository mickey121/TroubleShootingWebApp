package hello.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.vaadin.flow.spring.annotation.SpringComponent;
import hello.dto.QStep;
import hello.dao.Step;
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
    private JPAQuery query;

    public List<Step> getAllSteps() {
        query = new JPAQuery<>(entityManager);
        return query.from(step).fetch();
    }

}
