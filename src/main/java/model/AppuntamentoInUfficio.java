package model;

import misc.StatoAppuntamento;
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
     * Costruttore completo per ricostruire un appuntamento dal database/file.
     */
    public AppuntamentoInUfficio(String idAppuntamento, String idCliente, String idConsulente,
                                  StatoAppuntamento stato, LocalDateTime dataOraInizio, LocalDateTime dataOraFine,
                                  String luogo, String note, String motivoCancellazione,
                                  LocalDateTime dataCreazione, LocalDateTime dataUltimaModifica,
                                  String googleCalendarEventId) {
        super(idAppuntamento, idCliente, idConsulente, TipoConsulenza.IN_UFFICIO, stato,
                dataOraInizio, dataOraFine, luogo, note, motivoCancellazione,
                dataCreazione, dataUltimaModifica, googleCalendarEventId);
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
