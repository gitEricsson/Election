package election.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "elections")
public class Election {
    @Id
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isStarted;
    private boolean isEnded;

    public Election() {}

    public Election(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public boolean isStarted() { return isStarted; }
    public void setStarted(boolean started) { isStarted = started; }
    public boolean isEnded() { return isEnded; }
    public void setEnded(boolean ended) { isEnded = ended; }
}