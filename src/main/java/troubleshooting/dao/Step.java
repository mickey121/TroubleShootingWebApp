package troubleshooting.dao;

import lombok.Data;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "steps")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stepName;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "step_to_option",
            joinColumns = @JoinColumn(name = "step_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<Option> options;

    @ManyToMany(mappedBy = "steps")
    @Lazy
    private List<Workflow> workflows;

    public Step() {
        this.options = new LinkedList<>();
    }

    public Step(String stepName) {
        this.options = new LinkedList<>();
        this.stepName = stepName;
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public void removeOption(String option) {
        options.remove(option);
    }

}
