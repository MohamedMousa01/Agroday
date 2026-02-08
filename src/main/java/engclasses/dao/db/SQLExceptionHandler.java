package engclasses.dao.db;

import org.slf4j.Logger;

import java.sql.SQLException;

/**
 * Utility class per gestire SQLException in modo centralizzato.
 * Riduce le duplicazioni nei DAO DB.
 */
public final class SQLExceptionHandler {

    private SQLExceptionHandler() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Logga un errore SQL e restituisce un messaggio user-friendly.
     *
     * @param logger il logger da usare
     * @param operazione descrizione dell'operazione fallita
     * @param e l'eccezione SQL
     */
    public static void logError(Logger logger, String operazione, SQLException e) {
        logger.error("Errore durante {}: {}", operazione, e.getMessage(), e);
    }

    /**
     * Gestisce SQLException comune per operazioni di lettura.
     *
     * @param logger il logger da usare
     * @param operazione descrizione dell'operazione
     * @param e l'eccezione SQL
     */
    public static void handleReadError(Logger logger, String operazione, SQLException e) {
        logger.error("Errore nel recupero di {}", operazione, e);
    }

    /**
     * Gestisce SQLException comune per operazioni di scrittura.
     *
     * @param logger il logger da usare
     * @param operazione descrizione dell'operazione
     * @param e l'eccezione SQL
     * @return false (convenzione per indicare operazione fallita)
     */
    public static boolean handleWriteError(Logger logger, String operazione, SQLException e) {
        logger.error("Errore durante {}", operazione, e);
        return false;
    }
}
