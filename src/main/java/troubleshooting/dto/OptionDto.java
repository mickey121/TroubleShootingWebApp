package troubleshooting.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;


@Data
public class OptionDto {
    private Long id;
    private String text;
    List<String> stepNames = new LinkedList<>();
}
