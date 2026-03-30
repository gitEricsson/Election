package election.dtos.responses;
import java.time.LocalDate;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
public class ElectionResponse {
    private Long id;
    private String title;
    private String status;
    private LocalDate endDate;
}
