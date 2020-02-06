package troubleshooting.dto;

import lombok.Data;
import org.hibernate.jdbc.Work;

import java.util.LinkedList;
import java.util.List;

@Data
public class WorkflowDto {
    private Long id;
    private String workflowName;
    private String question;
    private List<StepDto> steps = new LinkedList<>();
    private boolean isFinal;

    public WorkflowDto(String arg) {
        workflowName = arg;
    }

    public void addStep(StepDto step) {
        steps.add(step);
    }

    public String getDefaultOption() {
        return "";
    }
}
