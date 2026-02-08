package misc;

/**
 * Utility class per validazioni comuni.
 * Centralizza la logica di validazione per evitare duplicazioni.
 */
public final class ValidationUtils {

    private ValidationUtils() {
        // Private constructor to hide the implicit public one
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Verifica se una stringa è nulla o vuota (dopo trim).
     * 
     * @param value la stringa da validare
     * @return true se la stringa è null o vuota
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Valida che una stringa non sia nulla o vuota.
     * 
     * @param value la stringa da validare
     * @param fieldName nome del campo (per il messaggio di errore)
     * @return messaggio di errore se non valido, null altrimenti
     */
    public static String validateNotEmpty(String value, String fieldName) {
        if (isNullOrEmpty(value)) {
            return fieldName + " non può essere vuoto";
        }
        return null;
    }

    /**
     * Valida che un valore numerico sia positivo.
     * 
     * @param value il valore da validare
     * @param fieldName nome del campo (per il messaggio di errore)
     * @return messaggio di errore se non valido, null altrimenti
     */
    public static String validatePositive(int value, String fieldName) {
        if (value <= 0) {
            return fieldName + " deve essere maggiore di zero";
        }
        return null;
    }

    /**
     * Valida che una data non sia nulla.
     * 
     * @param value la data da validare
     * @param fieldName nome del campo (per il messaggio di errore)
     * @return messaggio di errore se non valido, null altrimenti
     */
    public static String validateNotNull(Object value, String fieldName) {
        if (value == null) {
            return fieldName + " è obbligatorio";
        }
        return null;
    }
}
