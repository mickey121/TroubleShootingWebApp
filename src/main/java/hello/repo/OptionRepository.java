package hello.repo;

import com.vaadin.flow.spring.annotation.SpringComponent;
import hello.dao.Option;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@SpringComponent
@Repository
public interface OptionRepository extends PagingAndSortingRepository<Option, Long>, CrudRepository<Option, Long>,
        QuerydslPredicateExecutor<Option> {
}