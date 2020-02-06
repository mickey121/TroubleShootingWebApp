package troubleshooting.provider;

import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import troubleshooting.dao.Workflow;
import troubleshooting.repo.WorkflowRepository;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WorkflowDataProvider extends AbstractBackEndDataProvider<Workflow, CrudFilter> {
    private WorkflowRepository workflowRepository;
    private Consumer<Long> sizeChangeListener;
    public WorkflowDataProvider(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    @Override
    protected Stream<Workflow> fetchFromBackEnd(Query<Workflow, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();
        List<Workflow> workflows = new LinkedList<>();
        workflowRepository.findAll().forEach(workflows::add);

        Stream<Workflow> stream;

        if (query.getFilter().isPresent()) {
            stream = workflows.stream()
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        } else {
            stream = workflows.stream();
        }
        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<Workflow, CrudFilter> query) {
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }

        return (int) count;
    }

    private static Comparator<Workflow> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream()
                .map(sortClause -> {
                    try {
                        Comparator<Workflow> comparator
                                = Comparator.comparing(workflow ->
                                (Comparable) valueOf(sortClause.getKey(), workflow));

                        if (sortClause.getValue() == SortDirection.DESCENDING) {
                            comparator = comparator.reversed();
                        }

                        return comparator;
                    } catch (Exception ex) {
                        return (Comparator<Workflow>) (o1, o2) -> 0;
                    }
                })
                .reduce(Comparator::thenComparing)
                .orElse((o1, o2) -> 0);
    }

    public void setSizeChangeListener(Consumer<Long> listener) {
        sizeChangeListener = listener;
    }

    public void persist(Workflow workflow) {

        workflowRepository.save(workflow);
    }

    public Optional<Workflow> find(Long id) {
        return workflowRepository.findById(id);
    }

    public void delete(Workflow workflow) {
        workflowRepository.delete(workflow);
    }
    private static Predicate<Workflow> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<Workflow>) workflow -> {
                    try {
                        Object value = valueOf(constraint.getKey(), workflow);
                        return value != null && value.toString().toLowerCase()
                                .contains(constraint.getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .reduce(Predicate::and)
                .orElse(e -> true);
    }

    private static Object valueOf(String fieldName, Workflow workflow) {
        try {
            Field field = Workflow.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(workflow);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
