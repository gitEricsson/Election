package election.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "elections")
public class Election {
    @Id
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isStarted;
    private boolean isEnded;

    public Election(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}