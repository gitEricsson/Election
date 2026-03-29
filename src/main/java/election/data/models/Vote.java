package election.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "votes")
public class Vote {
    @Id
    private Long id;
    private Long userId;
    private Long electionId;
    private Long candidateId;
    private LocalDateTime timestamp;

    public Vote() {}

    public Vote(Long userId, Long electionId, Long candidateId) {
        this.userId = userId;
        this.electionId = electionId;
        this.candidateId = candidateId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getElectionId() { return electionId; }
    public void setElectionId(Long electionId) { this.electionId = electionId; }
    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
