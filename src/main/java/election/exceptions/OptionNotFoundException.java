package election.exceptions;
 
public class OptionNotFoundException extends RuntimeException {
    public OptionNotFoundException() {
        super("Option not found");
    }
}
