package election.exceptions;

public class ElectionNotStartedException extends RuntimeException {
    public ElectionNotStartedException(String message) {
        super(message);
    }
}
