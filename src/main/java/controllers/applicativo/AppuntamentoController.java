package controllers.applicativo;

import engclasses.beans.AppuntamentoBean;
import engclasses.dao.api.AppuntamentoDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.factory.DAOFactory;
import engclasses.pattern.Factory.AppuntamentoFactory;
import engclasses.pattern.Factory.AppuntamentoFactoryProvider;
import engclasses.services.CalendarService;
import engclasses.services.DemoCalendarService;
import engclasses.services.GoogleCalendarAdapter;
import misc.PersistenceType;
import misc.Session;
import misc.StatoAppuntamento;
import misc.TipoConsulenza;
import misc.TipoUtente;
import model.Appuntamento;
import model.Utente;
import model.observer.NotificationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller applicativo per la gestione degli appuntamenti.
 * Implementa i casi d'uso: prenotazione, modifica, cancellazione e visualizzazione appuntamenti.
 */
public class AppuntamentoController {

    private final Session session;
    private final DAOFactory daoFactory;
    private final CalendarService calendarService;
    private final NotificationService notificationService;

    public AppuntamentoController(Session session) {
        this.session = session;
        PersistenceType persistenceType = session.getPersistenceType();
        this.daoFactory = DAOFactory.getFactory(persistenceType);
        
        // In modalità MEMORY (Demo), usa il DemoCalendarService
        // In modalità DB o FILE (Full), usa GoogleCalendarAdapter
        if (persistenceType == PersistenceType.MEMORY) {
            this.calendarService = DemoCalendarService.getInstance();
        } else {
            this.calendarService = GoogleCalendarAdapter.getInstance();
        }
        
        this.notificationService = NotificationService.getInstance();
    }

    // ==================== Prenotazione Appuntamento ====================

    /**
     * Prenota un nuovo appuntamento.
     *
     * @param bean I dati dell'appuntamento da creare
     * @return Il bean dell'appuntamento creato con l'ID assegnato
     * @throws IllegalArgumentException se i dati non sono validi
     * @throws IllegalStateException se esiste un conflitto di orario
     */
    public AppuntamentoBean prenotaAppuntamento(AppuntamentoBean bean) 
            throws IllegalArgumentException, IllegalStateException {
        
        // Validazione dati
        String erroriValidazione = validaDatiAppuntamento(bean);
        if (!erroriValidazione.isEmpty()) {
            throw new IllegalArgumentException(erroriValidazione);
        }

        AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();

        // Verifica conflitti di orario
        LocalDateTime inizio = bean.getDataOraInizio();
        LocalDateTime fine = bean.getDataOraFine();
        
        if (appuntamentoDAO.esisteConflitto(bean.getIdConsulente(), inizio, fine, null)) {
            throw new IllegalStateException("Il consulente ha già un appuntamento in questo orario.");
        }

        // Crea l'entità Appuntamento usando la Factory Method
        AppuntamentoFactory factory = AppuntamentoFactoryProvider.getFactory(bean.getTipoConsulenza());
        
        // Valida i dati specifici per il tipo di appuntamento
        long durataMinuti = java.time.Duration.between(inizio, fine).toMinutes();
        String erroreFactory = factory.valida(bean.getLuogo(), durataMinuti);
        if (erroreFactory != null) {
            throw new IllegalArgumentException(erroreFactory);
        }
        
        Appuntamento appuntamento = factory.creaAppuntamento(
                bean.getIdCliente(),
                bean.getIdConsulente(),
                inizio,
                fine,
                bean.getLuogo()
        );
        appuntamento.setNote(bean.getNote());

        // Registra gli observer
        appuntamento.addObserver(notificationService);
        if (calendarService instanceof GoogleCalendarAdapter) {
            appuntamento.addObserver((GoogleCalendarAdapter) calendarService);
        }

        // Salva l'appuntamento
        if (!appuntamentoDAO.salva(appuntamento)) {
            throw new IllegalStateException("Errore nel salvataggio dell'appuntamento.");
        }

        // Sincronizza con Google Calendar (se disponibile)
        if (calendarService.isDisponibile()) {
            String eventId = calendarService.creaEvento(appuntamento);
            if (eventId != null) {
                appuntamento.setGoogleCalendarEventId(eventId);
                appuntamentoDAO.aggiorna(appuntamento);
            }
        }

        // Prepara il bean di risposta
        return convertiInBean(appuntamento);
    }

    // ==================== Cancellazione Appuntamento ====================

    /**
     * Cancella un appuntamento da parte del cliente.
     *
     * @param idAppuntamento L'ID dell'appuntamento da cancellare
     * @param motivo Il motivo della cancellazione
     * @return true se la cancellazione ha successo
     */
    public boolean cancellaAppuntamentoCliente(String idAppuntamento, String motivo) {
        return cancellaAppuntamento(idAppuntamento, motivo, true);
    }

    /**
     * Cancella un appuntamento da parte del consulente.
     *
     * @param idAppuntamento L'ID dell'appuntamento da cancellare
     * @param motivo Il motivo della cancellazione
     * @return true se la cancellazione ha successo
     */
    public boolean cancellaAppuntamentoConsulente(String idAppuntamento, String motivo) {
        return cancellaAppuntamento(idAppuntamento, motivo, false);
    }

    private boolean cancellaAppuntamento(String idAppuntamento, String motivo, boolean daCliente) {
        AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
        Appuntamento appuntamento = appuntamentoDAO.trovaPerId(idAppuntamento);

        if (appuntamento == null) {
            return false;
        }

        if (!appuntamento.isModificabile()) {
            return false;
        }

        // Registra gli observer prima della cancellazione
        appuntamento.addObserver(notificationService);
        if (calendarService instanceof GoogleCalendarAdapter) {
            appuntamento.addObserver((GoogleCalendarAdapter) calendarService);
        }

        // Esegui la cancellazione (notifica automaticamente gli observer)
        if (daCliente) {
            appuntamento.cancellaPerCliente(motivo);
        } else {
            appuntamento.cancellaPerConsulente(motivo);
        }

        // Aggiorna nel database
        return appuntamentoDAO.aggiorna(appuntamento);
    }

    // ==================== Modifica Appuntamento ====================

    /**
     * Modifica un appuntamento esistente.
     *
     * @param bean I nuovi dati dell'appuntamento
     * @return Il bean aggiornato
     */
    public AppuntamentoBean modificaAppuntamento(AppuntamentoBean bean) 
            throws IllegalArgumentException, IllegalStateException {
        
        if (bean.getIdAppuntamento() == null || bean.getIdAppuntamento().isEmpty()) {
            throw new IllegalArgumentException("ID appuntamento non valido.");
        }

        AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
        Appuntamento appuntamento = appuntamentoDAO.trovaPerId(bean.getIdAppuntamento());

        if (appuntamento == null) {
            throw new IllegalArgumentException("Appuntamento non trovato.");
        }

        if (!appuntamento.isModificabile()) {
            throw new IllegalStateException("L'appuntamento non è più modificabile.");
        }

        // Verifica conflitti (escludendo l'appuntamento corrente)
        LocalDateTime nuovoInizio = bean.getDataOraInizio();
        LocalDateTime nuovaFine = bean.getDataOraFine();
        
        if (appuntamentoDAO.esisteConflitto(bean.getIdConsulente(), nuovoInizio, nuovaFine, bean.getIdAppuntamento())) {
            throw new IllegalStateException("Il consulente ha già un appuntamento in questo orario.");
        }

        // Aggiorna i dati
        appuntamento.setTipoConsulenza(bean.getTipoConsulenza());
        appuntamento.setDataOraInizio(nuovoInizio);
        appuntamento.setDataOraFine(nuovaFine);
        appuntamento.setLuogo(bean.getLuogo());
        appuntamento.setNote(bean.getNote());

        // Valida usando la Factory Method
        AppuntamentoFactory factory = AppuntamentoFactoryProvider.getFactory(bean.getTipoConsulenza());
        long durataMinuti = java.time.Duration.between(nuovoInizio, nuovaFine).toMinutes();
        String erroreValidazione = factory.valida(bean.getLuogo(), durataMinuti);
        if (erroreValidazione != null) {
            throw new IllegalArgumentException(erroreValidazione);
        }

        // Salva le modifiche
        if (!appuntamentoDAO.aggiorna(appuntamento)) {
            throw new IllegalStateException("Errore nell'aggiornamento dell'appuntamento.");
        }

        // Aggiorna su Google Calendar
        if (calendarService.isDisponibile() && appuntamento.getGoogleCalendarEventId() != null) {
            calendarService.aggiornaEvento(appuntamento);
        }

        return convertiInBean(appuntamento);
    }

    // ==================== Conferma Appuntamento ====================

    /**
     * Conferma un appuntamento.
     *
     * @param idAppuntamento L'ID dell'appuntamento da confermare
     * @return true se la conferma ha successo
     */
    public boolean confermaAppuntamento(String idAppuntamento) {
        AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
        Appuntamento appuntamento = appuntamentoDAO.trovaPerId(idAppuntamento);

        if (appuntamento == null || appuntamento.getStato() != StatoAppuntamento.PRENOTATO) {
            return false;
        }

        // Registra observer
        appuntamento.addObserver(notificationService);

        // Conferma (notifica automaticamente)
        appuntamento.conferma();

        return appuntamentoDAO.aggiorna(appuntamento);
    }

    // ==================== Ricerca Appuntamenti ====================

    /**
     * Ottiene tutti gli appuntamenti dell'utente corrente.
     */
    public List<AppuntamentoBean> getAppuntamentiUtenteCorrente() {
        Utente utente = session.getUtenteLoggato();
        if (utente == null) {
            return new ArrayList<>();
        }

        AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
        List<Appuntamento> appuntamenti;

        // Se l'utente è un consulente, cerca per consulente, altrimenti per cliente
        if (utente.getTipo() == TipoUtente.CONSULENTE) {
            appuntamenti = appuntamentoDAO.trovaPerConsulente(utente.getIdUtente());
        } else {
            appuntamenti = appuntamentoDAO.trovaPerCliente(utente.getIdUtente());
        }

        return convertiListaInBean(appuntamenti);
    }

    /**
     * Ottiene gli appuntamenti per un consulente specifico.
     */
    public List<AppuntamentoBean> getAppuntamentiConsulente(String idConsulente) {
        AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
        List<Appuntamento> appuntamenti = appuntamentoDAO.trovaPerConsulente(idConsulente);
        return convertiListaInBean(appuntamenti);
    }

    /**
     * Ottiene un appuntamento per ID.
     */
    public AppuntamentoBean getAppuntamento(String idAppuntamento) {
        AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
        Appuntamento appuntamento = appuntamentoDAO.trovaPerId(idAppuntamento);
        return appuntamento != null ? convertiInBean(appuntamento) : null;
    }

    /**
     * Ottiene gli appuntamenti in un intervallo di date per un consulente.
     */
    public List<AppuntamentoBean> getAppuntamentiConsulentePerPeriodo(
            String idConsulente, LocalDateTime da, LocalDateTime a) {
        AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
        List<Appuntamento> appuntamenti = appuntamentoDAO.trovaPerConsulenteEIntervallo(idConsulente, da, a);
        return convertiListaInBean(appuntamenti);
    }

    /**
     * Ottiene la lista dei consulenti disponibili.
     */
    public List<Utente> getConsulentiDisponibili() {
        UtenteDAO utenteDAO = daoFactory.getUtenteDAO(TipoUtente.CONSULENTE);
        // Qui si potrebbe filtrare per consulenti attivi
        // Per ora restituiamo tutti i consulenti
        return new ArrayList<>(); // Da implementare con un metodo findAll nel DAO
    }

    // ==================== Preparazione Consulenza ====================

    /**
     * Prepara la consulenza e restituisce le istruzioni.
     * Le istruzioni sono specifiche per ogni tipo di appuntamento (Factory Method).
     */
    public String preparaConsulenza(String idAppuntamento) {
        AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
        Appuntamento appuntamento = appuntamentoDAO.trovaPerId(idAppuntamento);

        if (appuntamento == null) {
            return "Appuntamento non trovato.";
        }

        // Genera istruzioni in base al tipo di consulenza
        return switch (appuntamento.getTipoConsulenza()) {
            case ONLINE -> "📹 Consulenza Online\n" +
                    "Link videochiamata: " + (appuntamento.getLuogo() != null ? appuntamento.getLuogo() : "Da generare") + "\n" +
                    "Assicurati di avere una connessione internet stabile.";
            case IN_UFFICIO -> "🏢 Consulenza in Ufficio\n" +
                    "Indirizzo: " + appuntamento.getLuogo() + "\n" +
                    "Porta con te eventuali documenti necessari.";
            case SUL_CAMPO -> "🌾 Consulenza sul Campo\n" +
                    "Luogo: " + appuntamento.getLuogo() + "\n" +
                    "Indossa abbigliamento adatto per attività all'aperto.";
        };
    }

    // ==================== Metodi di Validazione ====================

    /**
     * Valida i campi del form di prenotazione.
     * Questo metodo viene chiamato dal controller grafico prima di creare il bean.
     *
     * @param consulenteSelezionato true se è stato selezionato un consulente
     * @param tipoConsulenza il tipo di consulenza selezionato (può essere null)
     * @param dataSelezionata true se è stata selezionata una data
     * @param oraInizioSelezionata true se è stata selezionata l'ora di inizio
     * @param oraFineSelezionata true se è stata selezionata l'ora di fine
     * @param luogo il luogo inserito (può essere null o vuoto)
     * @return stringa con gli errori di validazione, vuota se non ci sono errori
     */
    public String validaCampiPrenotazione(boolean consulenteSelezionato, TipoConsulenza tipoConsulenza,
                                           boolean dataSelezionata, boolean oraInizioSelezionata,
                                           boolean oraFineSelezionata, String luogo) {
        StringBuilder errori = new StringBuilder();

        if (!consulenteSelezionato) {
            errori.append("Seleziona un consulente.\n");
        }
        if (tipoConsulenza == null) {
            errori.append("Seleziona il tipo di consulenza.\n");
        }
        if (!dataSelezionata) {
            errori.append("Seleziona una data.\n");
        }
        if (!oraInizioSelezionata) {
            errori.append("Seleziona l'ora di inizio.\n");
        }
        if (!oraFineSelezionata) {
            errori.append("Seleziona l'ora di fine.\n");
        }

        // Verifica luogo per consulenze non online
        if (tipoConsulenza != null && tipoConsulenza != TipoConsulenza.ONLINE) {
            if (luogo == null || luogo.trim().isEmpty()) {
                errori.append("Il luogo è obbligatorio per le consulenze " + tipoConsulenza.getDisplayName() + ".\n");
            }
        }

        return errori.toString();
    }

    /**
     * Restituisce la durata predefinita in minuti per un tipo di consulenza.
     * Utilizza la Factory Method per ottenere la durata specifica del tipo.
     *
     * @param tipoConsulenza il tipo di consulenza
     * @return durata in minuti
     */
    public int getDurataPredefinitaMinuti(TipoConsulenza tipoConsulenza) {
        if (tipoConsulenza == null) {
            return 60; // default
        }
        AppuntamentoFactory factory = AppuntamentoFactoryProvider.getFactory(tipoConsulenza);
        return factory.getDurataPredefinitaMinuti();
    }

    /**
     * Filtra gli appuntamenti dell'utente corrente per stato.
     *
     * @param stato lo stato per cui filtrare (null per tutti)
     * @return lista di appuntamenti filtrati
     */
    public List<AppuntamentoBean> getAppuntamentiUtenteFiltrati(StatoAppuntamento stato) {
        List<AppuntamentoBean> tutti = getAppuntamentiUtenteCorrente();
        
        if (stato == null) {
            return tutti;
        }
        
        List<AppuntamentoBean> filtrati = new ArrayList<>();
        for (AppuntamentoBean app : tutti) {
            if (app.getStato() == stato) {
                filtrati.add(app);
            }
        }
        return filtrati;
    }

    /**
     * Cancella un appuntamento determinando automaticamente se la cancellazione
     * è da parte del cliente o del consulente in base all'utente loggato.
     *
     * @param idAppuntamento l'ID dell'appuntamento da cancellare
     * @param motivo il motivo della cancellazione
     * @return true se la cancellazione ha successo
     */
    public boolean cancellaAppuntamento(String idAppuntamento, String motivo) {
        Utente utente = session.getUtenteLoggato();
        
        if (utente != null && utente.getTipo() == TipoUtente.CONSULENTE) {
            return cancellaAppuntamentoConsulente(idAppuntamento, motivo);
        } else {
            return cancellaAppuntamentoCliente(idAppuntamento, motivo);
        }
    }

    private String validaDatiAppuntamento(AppuntamentoBean bean) {
        StringBuilder errori = new StringBuilder();

        if (bean.getIdCliente() == null || bean.getIdCliente().isEmpty()) {
            errori.append("Il cliente è obbligatorio.\n");
        }
        if (bean.getIdConsulente() == null || bean.getIdConsulente().isEmpty()) {
            errori.append("Il consulente è obbligatorio.\n");
        }
        if (bean.getTipoConsulenza() == null) {
            errori.append("Il tipo di consulenza è obbligatorio.\n");
        }
        if (bean.getData() == null) {
            errori.append("La data è obbligatoria.\n");
        }
        if (bean.getOraInizio() == null) {
            errori.append("L'ora di inizio è obbligatoria.\n");
        }
        if (bean.getOraFine() == null) {
            errori.append("L'ora di fine è obbligatoria.\n");
        }

        // Verifica che la data/ora sia nel futuro
        LocalDateTime dataOraInizio = bean.getDataOraInizio();
        if (dataOraInizio != null && dataOraInizio.isBefore(LocalDateTime.now())) {
            errori.append("La data e ora devono essere nel futuro.\n");
        }

        // Verifica che l'ora di fine sia dopo l'ora di inizio
        if (bean.getOraInizio() != null && bean.getOraFine() != null 
                && !bean.getOraFine().isAfter(bean.getOraInizio())) {
            errori.append("L'ora di fine deve essere successiva all'ora di inizio.\n");
        }

        // Verifica luogo per consulenze non online
        if (bean.getTipoConsulenza() != null && bean.getTipoConsulenza() != TipoConsulenza.ONLINE) {
            if (bean.getLuogo() == null || bean.getLuogo().trim().isEmpty()) {
                errori.append("Il luogo è obbligatorio per le consulenze " + 
                        bean.getTipoConsulenza().getDisplayName() + ".\n");
            }
        }

        return errori.toString();
    }

    // ==================== Metodi di Conversione ====================

    private AppuntamentoBean convertiInBean(Appuntamento appuntamento) {
        AppuntamentoBean bean = new AppuntamentoBean();
        bean.setIdAppuntamento(appuntamento.getIdAppuntamento());
        bean.setIdCliente(appuntamento.getIdCliente());
        bean.setIdConsulente(appuntamento.getIdConsulente());
        bean.setTipoConsulenza(appuntamento.getTipoConsulenza());
        bean.setStato(appuntamento.getStato());
        bean.setDataOraInizio(appuntamento.getDataOraInizio());
        bean.setDataOraFine(appuntamento.getDataOraFine());
        bean.setLuogo(appuntamento.getLuogo());
        bean.setNote(appuntamento.getNote());
        bean.setMotivoCancellazione(appuntamento.getMotivoCancellazione());
        bean.setDataCreazione(appuntamento.getDataCreazione());
        bean.setDataUltimaModifica(appuntamento.getDataUltimaModifica());
        bean.setGoogleCalendarEventId(appuntamento.getGoogleCalendarEventId());

        // Aggiungi link calendario se disponibile
        if (appuntamento.getGoogleCalendarEventId() != null && calendarService.isDisponibile()) {
            bean.setLinkCalendario(calendarService.getLinkEvento(appuntamento.getGoogleCalendarEventId()));
        }

        // Carica nomi utenti (se necessario)
        caricaNomiUtenti(bean);

        return bean;
    }

    private List<AppuntamentoBean> convertiListaInBean(List<Appuntamento> appuntamenti) {
        List<AppuntamentoBean> beans = new ArrayList<>();
        for (Appuntamento app : appuntamenti) {
            beans.add(convertiInBean(app));
        }
        return beans;
    }

    private void caricaNomiUtenti(AppuntamentoBean bean) {
        // Carica i nomi del cliente e del consulente dai rispettivi DAO
        // Questa è un'operazione opzionale per migliorare la visualizzazione
        try {
            // Per ora lasciamo vuoto - da implementare se necessario
            // UtenteDAO utenteDAO = daoFactory.getLoginUtenteDAO();
            // Utente cliente = utenteDAO.findById(bean.getIdCliente());
            // if (cliente != null) {
            //     bean.setNomeCliente(cliente.getNome());
            //     bean.setCognomeCliente(cliente.getCognome());
            // }
        } catch (Exception e) {
            // Ignora errori nel caricamento dei nomi
        }
    }
}
