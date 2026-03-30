package election.dtos.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteRequest {
    private Long userId;
    private Long electionId;
    private Long candidateId;
}