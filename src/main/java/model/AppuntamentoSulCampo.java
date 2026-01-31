package model;

import misc.StatoAppuntamento;
import misc.TipoConsulenza;

import java.time.LocalDateTime;

/**
 * Sottoclasse concreta per appuntamenti di tipo SUL_CAMPO.
 * Creata tramite il pattern Factory Method.
 */
public class AppuntamentoSulCampo extends Appuntamento {

    private static final long serialVersionUID = 1L;
    private static final int DURATA_PREDEFINITA_MINUTI = 120;
    private static final int DURATA_MINIMA_MINUTI = 60;

    /**
     * Costruttore principale per creare un nuovo appuntamento sul campo.
     */
    public AppuntamentoSulCampo(String idCliente, String idConsulente,
                                 LocalDateTime dataOraInizio, LocalDateTime dataOraFine, String luogo) {
        super(idCliente, idConsulente, TipoConsulenza.SUL_CAMPO, dataOraInizio, dataOraFine, luogo);
    }

    /**
     * Costruttore completo per ricostruire un appuntamento dal database/file.
     */
    public AppuntamentoSulCampo(String idAppuntamento, String idCliente, String idConsulente,
                                 StatoAppuntamento stato, LocalDateTime dataOraInizio, LocalDateTime dataOraFine,
                                 String luogo, String note, String motivoCancellazione,
                                 LocalDateTime dataCreazione, LocalDateTime dataUltimaModifica,
                                 String googleCalendarEventId) {
        super(idAppuntamento, idCliente, idConsulente, TipoConsulenza.SUL_CAMPO, stato,
                dataOraInizio, dataOraFine, luogo, note, motivoCancellazione,
                dataCreazione, dataUltimaModifica, googleCalendarEventId);
    }

    /**
     * Valida che l'indirizzo del terreno/azienda sia specificato.
     */
    public boolean isIndirizzoValido() {
        return getLuogo() != null && !getLuogo().trim().isEmpty();
    }

    /**
     * Valida che la durata sia almeno quella minima richiesta.
     */
    public boolean isDurataValida() {
        return getDurataMinuti() >= DURATA_MINIMA_MINUTI;
    }

    /**
     * Restituisce la durata predefinita per consulenze sul campo.
     */
    public static int getDurataPredefinitaMinuti() {
        return DURATA_PREDEFINITA_MINUTI;
    }

    /**
     * Restituisce la durata minima per consulenze sul campo.
     */
    public static int getDurataMinimaMinuti() {
        return DURATA_MINIMA_MINUTI;
    }

    @Override
    public String toString() {
        return "AppuntamentoSulCampo{" +
                "id='" + getIdAppuntamento() + '\'' +
                ", stato=" + getStato() +
                ", data=" + getDataOraInizio() +
                ", luogo='" + getLuogo() + '\'' +
                '}';
    }
}
