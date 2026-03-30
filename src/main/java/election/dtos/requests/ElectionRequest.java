package election.dtos.requests;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ElectionRequest {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
}