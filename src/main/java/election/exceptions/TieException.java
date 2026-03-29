package election.exceptions;

public class TieException extends RuntimeException {
    public TieException(String message) {
        super(message);
    }
}
