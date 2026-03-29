package election.exceptions;

public class DuplicateCandidateException extends RuntimeException {
    public DuplicateCandidateException(String message) {
        super(message);
    }
}
