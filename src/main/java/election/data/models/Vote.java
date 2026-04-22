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
    private String id;
    private String userId;
    private String electionId;
    private String optionId;
    private LocalDateTime timestamp;

    public Vote(String userId, String electionId, String optionId) {
        this.userId = userId;
        this.electionId = electionId;
        this.optionId = optionId;
    }
}
