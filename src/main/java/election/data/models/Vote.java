package election.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "votes")
public class Vote {
    @Id
    private Long id;
    private Long userId;
    private Long electionId;
    private Long candidateId;
    private LocalDateTime timestamp;

    public Vote(Long userId, Long electionId, Long candidateId) {
        this.userId = userId;
        this.electionId = electionId;
        this.candidateId = candidateId;
    }
}
