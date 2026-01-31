package engclasses.exceptions;

/**
 * Eccezione per errori relativi alla gestione degli appuntamenti.
 */
public class AppuntamentoException extends Exception {

    public AppuntamentoException(String message) {
        super(message);
    }

    public AppuntamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}
