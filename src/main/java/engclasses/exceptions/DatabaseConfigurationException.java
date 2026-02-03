package engclasses.exceptions;

/**
 * Eccezione lanciata quando ci sono problemi con la configurazione del database.
 */
public class DatabaseConfigurationException extends RuntimeException {

    public DatabaseConfigurationException(String message) {
        super(message);
    }

    public DatabaseConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
