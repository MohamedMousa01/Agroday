package model;

import misc.TipoConsulenza;
import java.time.LocalDateTime;

/**
 * Sottoclasse concreta per appuntamenti di tipo IN_UFFICIO.
 * Creata tramite il pattern Factory Method.
 */
public class AppuntamentoInUfficio extends Appuntamento {

    private static final long serialVersionUID = 1L;
    private static final int DURATA_PREDEFINITA_MINUTI = 90;

    /**
     * Costruttore principale per creare un nuovo appuntamento in ufficio.
     */
    public AppuntamentoInUfficio(String idCliente, String idConsulente,
                                  LocalDateTime dataOraInizio, LocalDateTime dataOraFine, String luogo) {
        super(idCliente, idConsulente, TipoConsulenza.IN_UFFICIO, dataOraInizio, dataOraFine, luogo);
    }

    /**
     * Costruttore con ID per ricaricare dal database.
     */
    public AppuntamentoInUfficio(String idAppuntamento, String idCliente, String idConsulente,
                                  LocalDateTime dataOraInizio, LocalDateTime dataOraFine, String luogo) {
        super(new Builder(idCliente, idConsulente, TipoConsulenza.IN_UFFICIO, dataOraInizio, dataOraFine)
                .idAppuntamento(idAppuntamento)
                .luogo(luogo));
    }

    /**
     * Costruttore che accetta un Builder (per DAO e uso avanzato).
     */
    public AppuntamentoInUfficio(Builder builder) {
        super(builder);
    }

    /**
     * Valida che l'indirizzo dell'ufficio sia specificato.
     */
    public boolean isIndirizzoValido() {
        return getLuogo() != null && !getLuogo().trim().isEmpty();
    }

    /**
     * Restituisce la durata predefinita per consulenze in ufficio.
     */
    public static int getDurataPredefinitaMinuti() {
        return DURATA_PREDEFINITA_MINUTI;
    }

    @Override
    public String toString() {
        return "AppuntamentoInUfficio{" +
                "id='" + getIdAppuntamento() + '\'' +
                ", stato=" + getStato() +
                ", data=" + getDataOraInizio() +
                ", indirizzo='" + getLuogo() + '\'' +
                '}';
    }
}
