package troubleshooting.provider;
import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import troubleshooting.dao.Step;
import troubleshooting.dao.Workflow;
import troubleshooting.repo.StepRepository;
import troubleshooting.repo.WorkflowRepository;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StepDataProvider extends AbstractBackEndDataProvider<Step, CrudFilter> {
    private StepRepository stepRepository;
    private Consumer<Long> sizeChangeListener;
    public StepDataProvider(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    protected Stream<Step> fetchFromBackEnd(Query<Step, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();
        List<Step> steps = new LinkedList<>();
        stepRepository.findAll().forEach(steps::add);
//        stepRepository.findByWorkflowId();

        Stream<Step> stream;

        if (query.getFilter().isPresent()) {
            stream = steps.stream()
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        } else {
            stream = steps.stream();
        }
        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<Step, CrudFilter> query) {
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }

        return (int) count;
    }

    private static Comparator<Step> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream()
                .map(sortClause -> {
                    try {
                        Comparator<Step> comparator
                                = Comparator.comparing(step ->
                                (Comparable) valueOf(sortClause.getKey(), step));

                        if (sortClause.getValue() == SortDirection.DESCENDING) {
                            comparator = comparator.reversed();
                        }

                        return comparator;
                    } catch (Exception ex) {
                        return (Comparator<Step>) (o1, o2) -> 0;
                    }
                })
                .reduce(Comparator::thenComparing)
                .orElse((o1, o2) -> 0);
    }

    public void setSizeChangeListener(Consumer<Long> listener) {
        sizeChangeListener = listener;
    }

    public void persist(Step step) {

        stepRepository.save(step);
    }

    public Optional<Step> find(Long id) {
        return stepRepository.findById(id);
    }

    public void delete(Step step) {
        stepRepository.delete(step);
    }
    private static Predicate<Step> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<Step>) step -> {
                    try {
                        Object value = valueOf(constraint.getKey(), step);
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

    private static Object valueOf(String fieldName, Step step) {
        try {
            Field field = Step.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(step);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
