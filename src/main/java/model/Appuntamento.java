package model;

import misc.StatoAppuntamento;
import misc.TipoConsulenza;
import model.observer.AppuntamentoObserver;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entità astratta che rappresenta un appuntamento per una consulenza.
 * Implementa il pattern Observer per notificare le parti interessate in caso di modifiche.
 * Le sottoclassi concrete (AppuntamentoOnline, AppuntamentoInUfficio, AppuntamentoSulCampo)
 * vengono create tramite il pattern Factory Method.
 */
public abstract class Appuntamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String idAppuntamento;
    private final String idCliente;
    private final String idConsulente;
    private TipoConsulenza tipoConsulenza;
    private StatoAppuntamento stato;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private String luogo; // URL per online, indirizzo per ufficio/campo
    private String note;
    private String motivoCancellazione;
    private LocalDateTime dataCreazione;
    private LocalDateTime dataUltimaModifica;
    private String googleCalendarEventId; // ID evento Google Calendar (se sincronizzato)

    // Lista degli observer (transient per non serializzarla)
    private transient List<AppuntamentoObserver> observers;

    /**
     * Costruttore principale per creare un nuovo appuntamento.
     * Visibilità protected per permettere l'uso solo dalle sottoclassi.
     */
    protected Appuntamento(String idCliente, String idConsulente, TipoConsulenza tipoConsulenza,
                        LocalDateTime dataOraInizio, LocalDateTime dataOraFine, String luogo) {
        this.idAppuntamento = UUID.randomUUID().toString();
        this.idCliente = idCliente;
        this.idConsulente = idConsulente;
        this.tipoConsulenza = tipoConsulenza;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
        this.luogo = luogo;
        this.stato = StatoAppuntamento.PRENOTATO;
        this.dataCreazione = LocalDateTime.now();
        this.dataUltimaModifica = LocalDateTime.now();
        this.observers = new ArrayList<>();
    }

    /**
     * Costruttore completo per ricostruire un appuntamento dal database/file.
     * Visibilità protected per permettere l'uso solo dalle sottoclassi.
     */
    protected Appuntamento(String idAppuntamento, String idCliente, String idConsulente,
                        TipoConsulenza tipoConsulenza, StatoAppuntamento stato,
                        LocalDateTime dataOraInizio, LocalDateTime dataOraFine,
                        String luogo, String note, String motivoCancellazione,
                        LocalDateTime dataCreazione, LocalDateTime dataUltimaModifica,
                        String googleCalendarEventId) {
        this.idAppuntamento = idAppuntamento;
        this.idCliente = idCliente;
        this.idConsulente = idConsulente;
        this.tipoConsulenza = tipoConsulenza;
        this.stato = stato;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
        this.luogo = luogo;
        this.note = note;
        this.motivoCancellazione = motivoCancellazione;
        this.dataCreazione = dataCreazione;
        this.dataUltimaModifica = dataUltimaModifica;
        this.googleCalendarEventId = googleCalendarEventId;
        this.observers = new ArrayList<>();
    }

    // ==================== Observer Pattern ====================

    public void addObserver(AppuntamentoObserver observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(AppuntamentoObserver observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    private void notifyObservers(String evento) {
        if (observers != null) {
            for (AppuntamentoObserver observer : observers) {
                observer.onAppuntamentoModificato(this, evento);
            }
        }
    }

    // ==================== Operazioni di Business ====================

    /**
     * Cancella l'appuntamento da parte del cliente.
     */
    public void cancellaPerCliente(String motivo) {
        this.stato = StatoAppuntamento.CANCELLATO_CLIENTE;
        this.motivoCancellazione = motivo;
        this.dataUltimaModifica = LocalDateTime.now();
        notifyObservers("CANCELLATO_CLIENTE");
    }

    /**
     * Cancella l'appuntamento da parte del consulente.
     */
    public void cancellaPerConsulente(String motivo) {
        this.stato = StatoAppuntamento.CANCELLATO_CONSULENTE;
        this.motivoCancellazione = motivo;
        this.dataUltimaModifica = LocalDateTime.now();
        notifyObservers("CANCELLATO_CONSULENTE");
    }

    /**
     * Conferma l'appuntamento.
     */
    public void conferma() {
        this.stato = StatoAppuntamento.CONFERMATO;
        this.dataUltimaModifica = LocalDateTime.now();
        notifyObservers("CONFERMATO");
    }

    /**
     * Segna l'appuntamento come completato.
     */
    public void completa() {
        this.stato = StatoAppuntamento.COMPLETATO;
        this.dataUltimaModifica = LocalDateTime.now();
        notifyObservers("COMPLETATO");
    }

    /**
     * Verifica se l'appuntamento è ancora modificabile.
     */
    public boolean isModificabile() {
        return stato.isAttivo() && dataOraInizio.isAfter(LocalDateTime.now());
    }

    /**
     * Verifica se l'appuntamento è passato.
     */
    public boolean isPassato() {
        return dataOraFine.isBefore(LocalDateTime.now());
    }

    /**
     * Calcola la durata dell'appuntamento in minuti.
     */
    public long getDurataMinuti() {
        return java.time.Duration.between(dataOraInizio, dataOraFine).toMinutes();
    }

    // ==================== Getters e Setters ====================

    public String getIdAppuntamento() {
        return idAppuntamento;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getIdConsulente() {
        return idConsulente;
    }

    public TipoConsulenza getTipoConsulenza() {
        return tipoConsulenza;
    }

    public void setTipoConsulenza(TipoConsulenza tipoConsulenza) {
        this.tipoConsulenza = tipoConsulenza;
        this.dataUltimaModifica = LocalDateTime.now();
    }

    public StatoAppuntamento getStato() {
        return stato;
    }

    public void setStato(StatoAppuntamento stato) {
        this.stato = stato;
        this.dataUltimaModifica = LocalDateTime.now();
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
        this.dataUltimaModifica = LocalDateTime.now();
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public void setDataOraFine(LocalDateTime dataOraFine) {
        this.dataOraFine = dataOraFine;
        this.dataUltimaModifica = LocalDateTime.now();
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
        this.dataUltimaModifica = LocalDateTime.now();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        this.dataUltimaModifica = LocalDateTime.now();
    }

    public String getMotivoCancellazione() {
        return motivoCancellazione;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public LocalDateTime getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public String getGoogleCalendarEventId() {
        return googleCalendarEventId;
    }

    public void setGoogleCalendarEventId(String googleCalendarEventId) {
        this.googleCalendarEventId = googleCalendarEventId;
    }

    @Override
    public String toString() {
        return "Appuntamento{" +
                "id='" + idAppuntamento + '\'' +
                ", tipo=" + tipoConsulenza +
                ", stato=" + stato +
                ", data=" + dataOraInizio +
                ", luogo='" + luogo + '\'' +
                '}';
    }
}
