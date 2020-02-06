package troubleshooting.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class StepDto {
    private Long id;
    private String stepName;
    private String question;
    private List<OptionDto> options = new LinkedList<>();
    private boolean isFinal;

    public StepDto(String stepName) {
        this.stepName = stepName;
    }

    public void addOption(OptionDto option) {
        options.add(option);
    }

    public String getDefaultOption() {
        return "";
    }
}
