package troubleshooting.dao;

import lombok.Data;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @ManyToMany(mappedBy = "options")
    @Lazy
    private List<Step> steps;

    private String nextStep;

    private boolean isFinal;

    public Option() {
        steps = new LinkedList<>();
    }

    public void addToStep(Step step) {
        steps.add(step);
    }
}
