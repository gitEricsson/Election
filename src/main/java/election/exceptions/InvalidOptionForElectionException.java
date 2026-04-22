package election.exceptions;
 
public class InvalidOptionForElectionException extends RuntimeException {
    public InvalidOptionForElectionException(String message) {
        super(message);
    }
}
