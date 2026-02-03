package misc;

/**
 * Costanti per i messaggi comuni dell'applicazione.
 */
public final class MessageConstants {

    // Messaggi funzionalità
    public static final String FUNZIONALITA_IN_SVILUPPO = "(Funzionalità in sviluppo)";
    
    // Messaggi errore
    public static final String ERRORE_APERTURA_FINESTRA = "Errore nell'apertura della finestra: ";
    
    // Filtri e opzioni
    public static final String FILTRO_TUTTI = "Tutti";
    
    // Formattazione output
    public static final String SEPARATOR_LINE = "===========================================";
    public static final String LABEL_ID = " ID: ";
    public static final String LABEL_DATA = " Data: ";
    public static final String LABEL_ORARIO = " Orario: ";
    public static final String LABEL_STATO = " Stato: ";
    public static final String LABEL_LUOGO = " Luogo: ";
    public static final String LABEL_DURATA = " Durata: ";
    public static final String LABEL_MOTIVO = " Motivo: ";
    
    // Prefissi
    public static final String ERRORE_PREFIX = "✗ Errore: ";
    public static final String ERRORE_GENERICO = "Errore: ";
    
    private MessageConstants() {
        // Classe di costanti - non istanziabile
    }
}
