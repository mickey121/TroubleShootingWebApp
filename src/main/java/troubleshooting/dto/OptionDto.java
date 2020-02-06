package troubleshooting.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;


@Data
@NoArgsConstructor
public class OptionDto {
    private Long id;
    private String text;
    List<String> stepNames = new LinkedList<>();
    private String nextStep;

    public OptionDto(String text) {
        this.text = text;
    }
}
