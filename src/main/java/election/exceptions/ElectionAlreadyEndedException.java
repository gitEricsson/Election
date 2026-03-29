package election.exceptions;

public class ElectionAlreadyEndedException extends RuntimeException {
    public ElectionAlreadyEndedException(String message) {
        super(message);
    }
}
