package election.dtos.responses;
 
import lombok.*;
 
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponse {
    private String id;
    private String name;
}
