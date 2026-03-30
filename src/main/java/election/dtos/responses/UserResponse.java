package election.dtos.responses;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
}