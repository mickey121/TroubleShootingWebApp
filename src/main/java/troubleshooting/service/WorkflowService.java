package troubleshooting.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;
import troubleshooting.dao.QWorkflow;
import troubleshooting.dao.Workflow;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringComponent
@Repository
public class WorkflowService {
    @PersistenceContext
    private EntityManager entityManager;

    private QWorkflow workflow = QWorkflow.workflow;
    private JPAQuery query;

    public List<Workflow> getAllSteps() {
        query = new JPAQuery<>(entityManager);
        return query.from(workflow).fetch();
    }

}
