package election.exceptions;

public class ElectionAlreadyStartedException extends RuntimeException {
    public ElectionAlreadyStartedException(String message) {
        super(message);
    }
}
