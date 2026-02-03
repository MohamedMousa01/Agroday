package misc;

/**
 * Costanti per gli eventi degli appuntamenti.
 */
public final class EventoAppuntamento {

    public static final String CONFERMATO = "CONFERMATO";
    public static final String COMPLETATO = "COMPLETATO";
    public static final String CANCELLATO_CLIENTE = "CANCELLATO_CLIENTE";
    public static final String CANCELLATO_CONSULENTE = "CANCELLATO_CONSULENTE";
    public static final String MODIFICATO = "MODIFICATO";
    public static final String CREATO = "CREATO";

    private EventoAppuntamento() {
        // Classe di costanti - non istanziabile
    }
}
