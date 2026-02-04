package model;

import misc.EventoAppuntamento;
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
     * Builder Pattern per costruire Appuntamento con parametri flessibili.
     * Public per permettere l'uso da DAO e altre classi.
     */
    public static class Builder {
        // Campi obbligatori
        protected final String idCliente;
        protected final String idConsulente;
        protected final TipoConsulenza tipoConsulenza;
        protected final LocalDateTime dataOraInizio;
        protected final LocalDateTime dataOraFine;
        
        // Campi opzionali
        protected String idAppuntamento = null;
        protected StatoAppuntamento stato = StatoAppuntamento.PRENOTATO;
        protected String luogo = "";
        protected String note = "";
        protected String motivoCancellazione = null;
        protected LocalDateTime dataCreazione = LocalDateTime.now();
        protected LocalDateTime dataUltimaModifica = LocalDateTime.now();
        protected String googleCalendarEventId = null;
        
        public Builder(String idCliente, String idConsulente, TipoConsulenza tipoConsulenza,
                      LocalDateTime dataOraInizio, LocalDateTime dataOraFine) {
            this.idCliente = idCliente;
            this.idConsulente = idConsulente;
            this.tipoConsulenza = tipoConsulenza;
            this.dataOraInizio = dataOraInizio;
            this.dataOraFine = dataOraFine;
        }
        
        public Builder idAppuntamento(String idAppuntamento) {
            this.idAppuntamento = idAppuntamento;
            return this;
        }
        
        public Builder stato(StatoAppuntamento stato) {
            this.stato = stato;
            return this;
        }
        
        public Builder luogo(String luogo) {
            this.luogo = luogo;
            return this;
        }
        
        public Builder note(String note) {
            this.note = note;
            return this;
        }
        
        public Builder motivoCancellazione(String motivoCancellazione) {
            this.motivoCancellazione = motivoCancellazione;
            return this;
        }
        
        public Builder dataCreazione(LocalDateTime dataCreazione) {
            this.dataCreazione = dataCreazione;
            return this;
        }
        
        public Builder dataUltimaModifica(LocalDateTime dataUltimaModifica) {
            this.dataUltimaModifica = dataUltimaModifica;
            return this;
        }
        
        public Builder googleCalendarEventId(String googleCalendarEventId) {
            this.googleCalendarEventId = googleCalendarEventId;
            return this;
        }
    }

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
     * Costruttore protected usato dal Builder.
     */
    protected Appuntamento(Builder builder) {
        this.idAppuntamento = builder.idAppuntamento != null ? builder.idAppuntamento : UUID.randomUUID().toString();
        this.idCliente = builder.idCliente;
        this.idConsulente = builder.idConsulente;
        this.tipoConsulenza = builder.tipoConsulenza;
        this.stato = builder.stato;
        this.dataOraInizio = builder.dataOraInizio;
        this.dataOraFine = builder.dataOraFine;
        this.luogo = builder.luogo;
        this.note = builder.note;
        this.motivoCancellazione = builder.motivoCancellazione;
        this.dataCreazione = builder.dataCreazione;
        this.dataUltimaModifica = builder.dataUltimaModifica;
        this.googleCalendarEventId = builder.googleCalendarEventId;
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
        notifyObservers(EventoAppuntamento.CANCELLATO_CLIENTE);
    }

    /**
     * Cancella l'appuntamento da parte del consulente.
     */
    public void cancellaPerConsulente(String motivo) {
        this.stato = StatoAppuntamento.CANCELLATO_CONSULENTE;
        this.motivoCancellazione = motivo;
        this.dataUltimaModifica = LocalDateTime.now();
        notifyObservers(EventoAppuntamento.CANCELLATO_CONSULENTE);
    }

    /**
     * Conferma l'appuntamento.
     */
    public void conferma() {
        this.stato = StatoAppuntamento.CONFERMATO;
        this.dataUltimaModifica = LocalDateTime.now();
        notifyObservers(EventoAppuntamento.CONFERMATO);
    }

    /**
     * Segna l'appuntamento come completato.
     */
    public void completa() {
        this.stato = StatoAppuntamento.COMPLETATO;
        this.dataUltimaModifica = LocalDateTime.now();
        notifyObservers(EventoAppuntamento.COMPLETATO);
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
