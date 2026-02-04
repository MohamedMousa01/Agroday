package model;

import misc.TipoConsulenza;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sottoclasse concreta per appuntamenti di tipo ONLINE (videochiamata).
 * Creata tramite il pattern Factory Method.
 */
public class AppuntamentoOnline extends Appuntamento {

    private static final long serialVersionUID = 1L;
    private static final int DURATA_PREDEFINITA_MINUTI = 60;

    /**
     * Costruttore principale per creare un nuovo appuntamento online.
     */
    public AppuntamentoOnline(String idCliente, String idConsulente,
                               LocalDateTime dataOraInizio, LocalDateTime dataOraFine, String luogo) {
        super(idCliente, idConsulente, TipoConsulenza.ONLINE, dataOraInizio, dataOraFine, luogo);
    }

    /**
     * Costruttore con ID per ricaricare dal database.
     */
    public AppuntamentoOnline(String idAppuntamento, String idCliente, String idConsulente,
                               LocalDateTime dataOraInizio, LocalDateTime dataOraFine, String luogo) {
        super(new Builder(idCliente, idConsulente, TipoConsulenza.ONLINE, dataOraInizio, dataOraFine)
                .idAppuntamento(idAppuntamento)
                .luogo(luogo));
    }

    /**
     * Costruttore che accetta un Builder (per DAO e uso avanzato).
     */
    public AppuntamentoOnline(Builder builder) {
        super(builder);
    }

    /**
     * Genera automaticamente un URL per il meeting se non presente.
     */
    public String generaUrlMeeting() {
        if (getLuogo() == null || getLuogo().isEmpty()) {
            String meetingId = UUID.randomUUID().toString().substring(0, 8);
            String url = "https://meet.agroday.it/" + meetingId;
            setLuogo(url);
            return url;
        }
        return getLuogo();
    }

    /**
     * Restituisce la durata predefinita per consulenze online.
     */
    public static int getDurataPredefinitaMinuti() {
        return DURATA_PREDEFINITA_MINUTI;
    }

    @Override
    public String toString() {
        return "AppuntamentoOnline{" +
                "id='" + getIdAppuntamento() + '\'' +
                ", stato=" + getStato() +
                ", data=" + getDataOraInizio() +
                ", url='" + getLuogo() + '\'' +
                '}';
    }
}
