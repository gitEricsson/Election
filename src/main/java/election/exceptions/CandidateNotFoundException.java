package election.exceptions;

public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException() {
        super("Candidate not found");
    }
}
