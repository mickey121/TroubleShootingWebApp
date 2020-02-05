package hello.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class StepDto {
    private Long id;
    private String stepName;
    private String question;
    private List<OptionDto> options = new LinkedList<>();
    private boolean isFinal;

    public void addOption(OptionDto option) {
        options.add(option);
    }

    public String getDefaultOption() {
        return "";
    }
}
