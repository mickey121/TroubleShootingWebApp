package troubleshooting.dto;

import lombok.Data;
import troubleshooting.dao.Step;

import java.util.LinkedList;
import java.util.List;


@Data
public class OptionDto {
    private Long id;
    private String text;
    List<String> stepNames = new LinkedList<>();
    private String nextStep;
}
