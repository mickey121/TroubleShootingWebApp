package hello.repo;

import com.vaadin.flow.spring.annotation.SpringComponent;
import hello.dao.Step;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@SpringComponent
@Repository
public interface StepRepository extends PagingAndSortingRepository<Step, Long>, CrudRepository<Step, Long>,
        QuerydslPredicateExecutor<Step> {
}
