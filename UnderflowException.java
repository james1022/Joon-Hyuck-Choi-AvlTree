/**
 * An exception class representing a situation where a
 * collection of items is empty.
*/
public class UnderflowException extends RuntimeException {

    /**
     * Construct a new exception with default message.
     */
    public UnderflowException() {
        super("***Exception: collection is empty***");
    }

    /**
     * Construct a new exception with custom message.
     * @param message  the message to include in the exception
     */
    public UnderflowException(String message) {
        super(message);  //calls RuntimeException's constructor
    }

}