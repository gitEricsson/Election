package election.dtos.responses;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectionResultResponse {
    private String electionTitle;
    private Map<String, Integer> candidateVotes;
    private String winnerName;
}
