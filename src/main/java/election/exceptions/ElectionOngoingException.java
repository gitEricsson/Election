package election.exceptions;

public class ElectionOngoingException extends RuntimeException {
    public ElectionOngoingException(String message) {
        super(message);
    }
}