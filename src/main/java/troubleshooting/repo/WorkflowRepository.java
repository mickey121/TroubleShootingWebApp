package troubleshooting.repo;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import troubleshooting.dao.Step;
import troubleshooting.dao.Workflow;

@SpringComponent
@Repository
public interface WorkflowRepository extends PagingAndSortingRepository<Workflow, Long>, CrudRepository<Workflow, Long>,
        QuerydslPredicateExecutor<Workflow> {
}
