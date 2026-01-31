package misc;

/**
 * Enum che rappresenta gli stati possibili di un appuntamento.
 */
public enum StatoAppuntamento {
    PRENOTATO("Prenotato"),
    CONFERMATO("Confermato"),
    IN_CORSO("In Corso"),
    COMPLETATO("Completato"),
    CANCELLATO_CLIENTE("Cancellato dal Cliente"),
    CANCELLATO_CONSULENTE("Cancellato dal Consulente");

    private final String displayName;

    StatoAppuntamento(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isCancellato() {
        return this == CANCELLATO_CLIENTE || this == CANCELLATO_CONSULENTE;
    }

    public boolean isAttivo() {
        return this == PRENOTATO || this == CONFERMATO || this == IN_CORSO;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
