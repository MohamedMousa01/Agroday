package engclasses.exceptions;

public class LoginFallitoException extends Exception {

    public LoginFallitoException(String message) {

        super(message);
    }

    public LoginFallitoException(String message, Throwable cause) {
        super(message, cause);
    }

}