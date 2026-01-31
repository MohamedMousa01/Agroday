package engclasses.beans;

import misc.StatoAppuntamento;
import misc.TipoConsulenza;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Bean (DTO) per il trasferimento dei dati degli appuntamenti tra i livelli dell'applicazione.
 * I controller grafici utilizzano questo bean per comunicare con i controller applicativi.
 */
public class AppuntamentoBean {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Identificatori
    private String idAppuntamento;
    private String idCliente;
    private String idConsulente;

    // Dati del cliente e consulente (per visualizzazione)
    private String nomeCliente;
    private String cognomeCliente;
    private String nomeConsulente;
    private String cognomeConsulente;

    // Dettagli appuntamento
    private TipoConsulenza tipoConsulenza;
    private StatoAppuntamento stato;
    private LocalDate data;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String luogo;
    private String note;
    private String motivoCancellazione;

    // Date di sistema
    private LocalDateTime dataCreazione;
    private LocalDateTime dataUltimaModifica;

    // Google Calendar
    private String googleCalendarEventId;
    private String linkCalendario;

    // Costruttore vuoto
    public AppuntamentoBean() {
    }

    // Costruttore per la creazione di un nuovo appuntamento
    public AppuntamentoBean(String idCliente, String idConsulente, TipoConsulenza tipoConsulenza,
                            LocalDate data, LocalTime oraInizio, LocalTime oraFine, String luogo) {
        this.idCliente = idCliente;
        this.idConsulente = idConsulente;
        this.tipoConsulenza = tipoConsulenza;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.luogo = luogo;
        this.stato = StatoAppuntamento.PRENOTATO;
    }

    // ==================== Metodi di utilità ====================

    /**
     * Restituisce la data/ora di inizio come LocalDateTime.
     */
    public LocalDateTime getDataOraInizio() {
        if (data != null && oraInizio != null) {
            return LocalDateTime.of(data, oraInizio);
        }
        return null;
    }

    /**
     * Restituisce la data/ora di fine come LocalDateTime.
     */
    public LocalDateTime getDataOraFine() {
        if (data != null && oraFine != null) {
            return LocalDateTime.of(data, oraFine);
        }
        return null;
    }

    /**
     * Imposta data e ora di inizio da un LocalDateTime.
     */
    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        if (dataOraInizio != null) {
            this.data = dataOraInizio.toLocalDate();
            this.oraInizio = dataOraInizio.toLocalTime();
        }
    }

    /**
     * Imposta l'ora di fine da un LocalDateTime.
     */
    public void setDataOraFine(LocalDateTime dataOraFine) {
        if (dataOraFine != null) {
            this.oraFine = dataOraFine.toLocalTime();
        }
    }

    /**
     * Restituisce la data formattata per la visualizzazione.
     */
    public String getDataFormattata() {
        return data != null ? data.format(DATE_FORMATTER) : "";
    }

    /**
     * Restituisce l'ora di inizio formattata.
     */
    public String getOraInizioFormattata() {
        return oraInizio != null ? oraInizio.format(TIME_FORMATTER) : "";
    }

    /**
     * Restituisce l'ora di fine formattata.
     */
    public String getOraFineFormattata() {
        return oraFine != null ? oraFine.format(TIME_FORMATTER) : "";
    }

    /**
     * Restituisce l'intervallo orario formattato.
     */
    public String getIntervalloOrario() {
        return getOraInizioFormattata() + " - " + getOraFineFormattata();
    }

    /**
     * Restituisce il nome completo del cliente.
     */
    public String getNomeCompletoCliente() {
        StringBuilder sb = new StringBuilder();
        if (nomeCliente != null) sb.append(nomeCliente);
        if (cognomeCliente != null) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(cognomeCliente);
        }
        return sb.toString();
    }

    /**
     * Restituisce il nome completo del consulente.
     */
    public String getNomeCompletoConsulente() {
        StringBuilder sb = new StringBuilder();
        if (nomeConsulente != null) sb.append(nomeConsulente);
        if (cognomeConsulente != null) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(cognomeConsulente);
        }
        return sb.toString();
    }

    /**
     * Calcola la durata in minuti.
     */
    public long getDurataMinuti() {
        if (oraInizio != null && oraFine != null) {
            return java.time.Duration.between(oraInizio, oraFine).toMinutes();
        }
        return 0;
    }

    /**
     * Restituisce una descrizione testuale dello stato.
     */
    public String getStatoDescrizione() {
        return stato != null ? stato.getDisplayName() : "";
    }

    /**
     * Restituisce una descrizione testuale del tipo di consulenza.
     */
    public String getTipoConsulenzaDescrizione() {
        return tipoConsulenza != null ? tipoConsulenza.getDisplayName() : "";
    }

    /**
     * Verifica se l'appuntamento è modificabile.
     */
    public boolean isModificabile() {
        if (stato == null || !stato.isAttivo()) {
            return false;
        }
        LocalDateTime dataOraInizio = getDataOraInizio();
        return dataOraInizio != null && dataOraInizio.isAfter(LocalDateTime.now());
    }

    /**
     * Verifica se l'appuntamento è cancellabile.
     */
    public boolean isCancellabile() {
        return isModificabile();
    }

    // ==================== Getters e Setters ====================

    public String getIdAppuntamento() {
        return idAppuntamento;
    }

    public void setIdAppuntamento(String idAppuntamento) {
        this.idAppuntamento = idAppuntamento;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdConsulente() {
        return idConsulente;
    }

    public void setIdConsulente(String idConsulente) {
        this.idConsulente = idConsulente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCognomeCliente() {
        return cognomeCliente;
    }

    public void setCognomeCliente(String cognomeCliente) {
        this.cognomeCliente = cognomeCliente;
    }

    public String getNomeConsulente() {
        return nomeConsulente;
    }

    public void setNomeConsulente(String nomeConsulente) {
        this.nomeConsulente = nomeConsulente;
    }

    public String getCognomeConsulente() {
        return cognomeConsulente;
    }

    public void setCognomeConsulente(String cognomeConsulente) {
        this.cognomeConsulente = cognomeConsulente;
    }

    public TipoConsulenza getTipoConsulenza() {
        return tipoConsulenza;
    }

    public void setTipoConsulenza(TipoConsulenza tipoConsulenza) {
        this.tipoConsulenza = tipoConsulenza;
    }

    public StatoAppuntamento getStato() {
        return stato;
    }

    public void setStato(StatoAppuntamento stato) {
        this.stato = stato;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMotivoCancellazione() {
        return motivoCancellazione;
    }

    public void setMotivoCancellazione(String motivoCancellazione) {
        this.motivoCancellazione = motivoCancellazione;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDateTime getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public void setDataUltimaModifica(LocalDateTime dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    public String getGoogleCalendarEventId() {
        return googleCalendarEventId;
    }

    public void setGoogleCalendarEventId(String googleCalendarEventId) {
        this.googleCalendarEventId = googleCalendarEventId;
    }

    public String getLinkCalendario() {
        return linkCalendario;
    }

    public void setLinkCalendario(String linkCalendario) {
        this.linkCalendario = linkCalendario;
    }

    @Override
    public String toString() {
        return "AppuntamentoBean{" +
                "id='" + idAppuntamento + '\'' +
                ", tipo=" + tipoConsulenza +
                ", stato=" + stato +
                ", data=" + getDataFormattata() +
                ", ora=" + getIntervalloOrario() +
                '}';
    }
}
