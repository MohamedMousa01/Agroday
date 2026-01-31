package misc;

/**
 * Enum che rappresenta i tipi di consulenza disponibili nel sistema.
 */
public enum TipoConsulenza {
    ONLINE("Online", "Consulenza tramite videochiamata"),
    IN_UFFICIO("In Ufficio", "Consulenza presso l'ufficio del consulente"),
    SUL_CAMPO("Sul Campo", "Consulenza presso il terreno/azienda del cliente");

    private final String displayName;
    private final String descrizione;

    TipoConsulenza(String displayName, String descrizione) {
        this.displayName = displayName;
        this.descrizione = descrizione;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
