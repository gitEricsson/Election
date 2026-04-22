package election.exceptions;
 
public class DuplicateOptionException extends RuntimeException {
    public DuplicateOptionException(String message) {
        super(message);
    }
}
