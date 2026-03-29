package election.exceptions;

public class InvalidCandidateForElectionException extends RuntimeException {
    public InvalidCandidateForElectionException(String message) {
        super(message);
    }
}
