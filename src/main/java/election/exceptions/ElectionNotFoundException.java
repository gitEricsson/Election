package election.exceptions;

public class ElectionNotFoundException extends RuntimeException {
    public ElectionNotFoundException() {
        super("Election not found");
    }
}
