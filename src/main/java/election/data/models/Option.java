package election.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "options")
public class Option {
    @Id
    private String id;
    private String electionId;
    private String name;

    public Option(String name) {
        this.name = name;
    }
}