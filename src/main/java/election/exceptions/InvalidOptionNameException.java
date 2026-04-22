package election.exceptions;
 
public class InvalidOptionNameException extends RuntimeException {
    public InvalidOptionNameException(String message) {
        super(message);
    }
}
