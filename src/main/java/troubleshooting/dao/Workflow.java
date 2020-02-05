package troubleshooting.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "workflows")
public class Workflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String workflowName;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "workflow_to_step",
            joinColumns = @JoinColumn(name = "workflow_id"),
            inverseJoinColumns = @JoinColumn(name = "step_id")
    )
    private List<Step> steps;

    public Workflow() {
        this.steps = new LinkedList<>();
    }

    public Workflow(String workflowName) {
        this.steps = new LinkedList<>();
        this.workflowName = workflowName;
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    public String getDefaultOption() {
        return "";
    }

    public void removeStep(String step) {
        steps.remove(step);
    }

}
