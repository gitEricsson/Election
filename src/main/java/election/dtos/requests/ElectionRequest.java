package election.dtos.requests;

import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ElectionRequest {
    private String title;
    private String description;
    private String creatorId;
    private LocalDate startDate;
    private LocalDate endDate;
    private java.util.List<String> options;
}