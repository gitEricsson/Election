package election.dtos.requests;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class VoteRequest {
    private Long userId;
    private Long electionId;
    private Long candidateId;
}