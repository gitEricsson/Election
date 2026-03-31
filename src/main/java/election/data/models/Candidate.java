package election.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "candidates")
public class Candidate {
    @Id
    private Long id;
    private Long electionId;
    private String name;

    public Candidate(String name) {
        this.name = name;
    }
}