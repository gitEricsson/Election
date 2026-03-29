package election.exceptions;

public class InvalidCandidateNameException extends RuntimeException {
    public InvalidCandidateNameException(String message) {
        super(message);
    }
}
