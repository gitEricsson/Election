package election.dtos.responses;

import java.util.Map;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
public class ElectionResultResponse {
    private String electionTitle;
    private Map<String, Integer> candidateVotes;
    private String winnerName;
}
