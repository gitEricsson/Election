package election.dtos.responses;
import java.time.LocalDate;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
public class ElectionResponse {
    private String id;
    private String title;
    private String description;
    private String status;
    private LocalDate endDate;
    private java.util.List<OptionResponse> options;
}
