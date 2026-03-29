package election.exceptions;

public class ElectionEndedException extends RuntimeException {
    public ElectionEndedException(String message) {
        super(message);
    }
}
