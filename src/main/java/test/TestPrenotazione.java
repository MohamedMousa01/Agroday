package test;

import controllers.applicativo.AppuntamentoController;
import engclasses.beans.AppuntamentoBean;
import misc.MessageConstants;
import misc.PersistenceType;
import misc.Session;
import misc.StatoAppuntamento;
import misc.TipoConsulenza;
import model.Consulente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Classe di test per provare il sistema di prenotazione appuntamenti.
 * Esegui questo main per testare le funzionalità senza GUI.
 */
public class TestPrenotazione {

    private static final Logger logger = LoggerFactory.getLogger(TestPrenotazione.class);

    // Costanti per ID test
    private static final String TEST_CLIENT_ID = "client001";
    private static final String TEST_CONSULTANT_ID = "cons002";
    
    // Costanti per log
    private static final String LOG_ID = "  ID: {}";
    private static final String LOG_DATA = "  Data: {}";
    private static final String LOG_ORARIO = "  Orario: {}";
    private static final String LOG_LUOGO = "  Luogo: {}";
    private static final String LOG_STATO = "  Stato: {}";
    private static final String LOG_EMPTY = "";

    public static void main(String[] args) {
        logger.info(MessageConstants.SEPARATOR_LINE);
        logger.info("   TEST SISTEMA PRENOTAZIONE CONSULENZE    ");
        logger.info("{}\n", MessageConstants.SEPARATOR_LINE);

        // 1. Inizializza la sessione e il controller
        AppuntamentoController controller = inizializzaSessioneEController();

        // Esegui i test di prenotazione
        eseguiTestPrenotazioni(controller);

        // Esegui test di gestione appuntamenti
        eseguiTestGestioneAppuntamenti(controller);
        
        // Esegui test conflitto orario
        eseguiTestConflittoOrario(controller);

        // Mostra riepilogo finale
        mostraRiepilogoFinale(controller);
    }
    
    private static AppuntamentoController inizializzaSessioneEController() {
        Session session = Session.getInstance();
        session.setPersistenceType(PersistenceType.MEMORY);
        logger.info("✓ Modalità: {}", session.isModalitaDemo() ? "DEMO (Memory)" : "FULL");

        Consulente utenteDemo = new Consulente(
                TEST_CLIENT_ID,
                "mario_rossi",
                "mario@example.com",
                "password123",
                "Mario",
                "Rossi",
                "Roma"
        );
        session.setUtenteLoggato(utenteDemo);
        logger.info("✓ Utente loggato: {}\n", utenteDemo.getUsername());

        return new AppuntamentoController(session);
    }
    
    private static void eseguiTestPrenotazioni(AppuntamentoController controller) {
        eseguiTestPrenotazioneOnline(controller);
        eseguiTestPrenotazioneInUfficio(controller);
        eseguiTestPrenotazioneSulCampo(controller);
    }
    
    private static void eseguiTestPrenotazioneOnline(AppuntamentoController controller) {
        logger.info("--- TEST 1: Prenotazione Consulenza ONLINE ---");
        try {
            AppuntamentoBean bean = new AppuntamentoBean();
            bean.setIdCliente(TEST_CLIENT_ID);
            bean.setIdConsulente(TEST_CONSULTANT_ID);
            bean.setTipoConsulenza(TipoConsulenza.ONLINE);
            bean.setData(LocalDate.now().plusDays(3));
            bean.setOraInizio(LocalTime.of(10, 0));
            bean.setOraFine(LocalTime.of(11, 0));
            bean.setNote("Prima consulenza di prova");

            AppuntamentoBean risultato = controller.prenotaAppuntamento(bean);
            logger.info("✓ Appuntamento ONLINE creato!");
            logDettagliAppuntamento(risultato);
            logger.info(LOG_EMPTY);
        } catch (Exception e) {
            logger.error("{}{}", MessageConstants.ERRORE_PREFIX, e.getMessage());
        }
    }
    
    private static void eseguiTestPrenotazioneInUfficio(AppuntamentoController controller) {
        logger.info("--- TEST 2: Prenotazione Consulenza IN UFFICIO ---");
        try {
            AppuntamentoBean bean = new AppuntamentoBean();
            bean.setIdCliente(TEST_CLIENT_ID);
            bean.setIdConsulente(TEST_CONSULTANT_ID);
            bean.setTipoConsulenza(TipoConsulenza.IN_UFFICIO);
            bean.setData(LocalDate.now().plusDays(5));
            bean.setOraInizio(LocalTime.of(14, 30));
            bean.setOraFine(LocalTime.of(16, 0));
            bean.setLuogo("Via Roma 123, Milano");
            bean.setNote("Portare documentazione catastale");

            AppuntamentoBean risultato = controller.prenotaAppuntamento(bean);
            logger.info("✓ Appuntamento IN UFFICIO creato!");
            logDettagliAppuntamento(risultato);
            logger.info(LOG_EMPTY);
        } catch (Exception e) {
            logger.error("{}{}", MessageConstants.ERRORE_PREFIX, e.getMessage());
        }
    }
    
    private static void eseguiTestPrenotazioneSulCampo(AppuntamentoController controller) {
        logger.info("--- TEST 3: Prenotazione Consulenza SUL CAMPO ---");
        try {
            AppuntamentoBean bean = new AppuntamentoBean();
            bean.setIdCliente(TEST_CLIENT_ID);
            bean.setIdConsulente(TEST_CONSULTANT_ID);
            bean.setTipoConsulenza(TipoConsulenza.SUL_CAMPO);
            bean.setData(LocalDate.now().plusDays(7));
            bean.setOraInizio(LocalTime.of(9, 0));
            bean.setOraFine(LocalTime.of(12, 0));
            bean.setLuogo("Azienda Agricola Verdi, Strada Provinciale 45, Latina");
            bean.setNote("Ispezione terreno per nuova coltivazione");

            AppuntamentoBean risultato = controller.prenotaAppuntamento(bean);
            logger.info("✓ Appuntamento SUL CAMPO creato!");
            logDettagliAppuntamento(risultato);
            logger.info("  Durata: {} minuti", risultato.getDurataMinuti());
            logger.info(LOG_EMPTY);
        } catch (Exception e) {
            logger.error("{}{}", MessageConstants.ERRORE_PREFIX, e.getMessage());
        }
    }
    
    private static void eseguiTestGestioneAppuntamenti(AppuntamentoController controller) {
        logger.info("--- TEST 4: Lista Appuntamenti Utente ---");
        List<AppuntamentoBean> appuntamenti = controller.getAppuntamentiUtenteCorrente();
        logger.info("Totale appuntamenti: {}", appuntamenti.size());
        for (AppuntamentoBean app : appuntamenti) {
            logger.info("  • {} {} - {} [{}]", 
                    app.getDataFormattata(), app.getIntervalloOrario(),
                    app.getTipoConsulenzaDescrizione(), app.getStatoDescrizione());
        }
        logger.info(LOG_EMPTY);

        eseguiTestConfermaAppuntamento(controller, appuntamenti);
        eseguiTestCancellazioneAppuntamento(controller, appuntamenti);
        eseguiTestPreparazioneConsulenza(controller, appuntamenti);
    }
    
    private static void eseguiTestConfermaAppuntamento(AppuntamentoController controller, List<AppuntamentoBean> appuntamenti) {
        logger.info("--- TEST 5: Conferma Appuntamento ---");
        if (!appuntamenti.isEmpty()) {
            String idDaConfermare = appuntamenti.get(0).getIdAppuntamento();
            boolean confermato = controller.confermaAppuntamento(idDaConfermare);
            logger.info(confermato ? "✓ Appuntamento confermato!" : "✗ Conferma fallita");
            
            AppuntamentoBean aggiornato = controller.getAppuntamento(idDaConfermare);
            logger.info("  Nuovo stato: {}", aggiornato.getStatoDescrizione());
        }
        logger.info(LOG_EMPTY);
    }
    
    private static void eseguiTestCancellazioneAppuntamento(AppuntamentoController controller, List<AppuntamentoBean> appuntamenti) {
        logger.info("--- TEST 6: Cancellazione Appuntamento ---");
        if (appuntamenti.size() > 1) {
            String idDaCancellare = appuntamenti.get(1).getIdAppuntamento();
            boolean cancellato = controller.cancellaAppuntamentoCliente(idDaCancellare, "Test cancellazione");
            logger.info(cancellato ? "✓ Appuntamento cancellato!" : "✗ Cancellazione fallita");
            
            AppuntamentoBean aggiornato = controller.getAppuntamento(idDaCancellare);
            logger.info("  Nuovo stato: {}", aggiornato.getStatoDescrizione());
            logger.info("  Motivo: {}", aggiornato.getMotivoCancellazione());
        }
        logger.info(LOG_EMPTY);
    }
    
    private static void eseguiTestPreparazioneConsulenza(AppuntamentoController controller, List<AppuntamentoBean> appuntamenti) {
        logger.info("--- TEST 7: Istruzioni Preparazione Consulenza ---");
        if (!appuntamenti.isEmpty()) {
            String idAppuntamento = appuntamenti.get(0).getIdAppuntamento();
            String istruzioni = controller.preparaConsulenza(idAppuntamento);
            logger.info(istruzioni);
        }
        logger.info(LOG_EMPTY);
    }
    
    private static void eseguiTestConflittoOrario(AppuntamentoController controller) {
        logger.info("--- TEST 8: Test Conflitto Orario ---");
        try {
            AppuntamentoBean bean = new AppuntamentoBean();
            bean.setIdCliente(TEST_CLIENT_ID);
            bean.setIdConsulente(TEST_CONSULTANT_ID);
            bean.setTipoConsulenza(TipoConsulenza.ONLINE);
            bean.setData(LocalDate.now().plusDays(3));
            bean.setOraInizio(LocalTime.of(10, 30));
            bean.setOraFine(LocalTime.of(11, 30));

            controller.prenotaAppuntamento(bean);
            logger.info("✗ Avrebbe dovuto rilevare il conflitto!");
        } catch (IllegalStateException e) {
            logger.info("✓ Conflitto rilevato correttamente: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Errore: {}", e.getMessage());
        }
        logger.info(LOG_EMPTY);
    }
    
    private static void mostraRiepilogoFinale(AppuntamentoController controller) {
        logger.info(MessageConstants.SEPARATOR_LINE);
        logger.info("   RIEPILOGO FINALE                        ");
        logger.info(MessageConstants.SEPARATOR_LINE);
        
        List<AppuntamentoBean> tuttiAppuntamenti = controller.getAppuntamentiUtenteCorrente();
        int prenotati = 0;
        int confermati = 0;
        int cancellati = 0;
        
        for (AppuntamentoBean app : tuttiAppuntamenti) {
            if (app.getStato() == StatoAppuntamento.PRENOTATO) prenotati++;
            else if (app.getStato() == StatoAppuntamento.CONFERMATO) confermati++;
            else if (app.getStato().isCancellato()) cancellati++;
        }
        
        logger.info("Totale appuntamenti: {}", tuttiAppuntamenti.size());
        logger.info("  - Prenotati: {}", prenotati);
        logger.info("  - Confermati: {}", confermati);
        logger.info("  - Cancellati: {}", cancellati);
        logger.info("\\n✓ Test completato con successo!");
    }
    
    private static void logDettagliAppuntamento(AppuntamentoBean appuntamento) {
        logger.info(LOG_ID, appuntamento.getIdAppuntamento());
        logger.info(LOG_DATA, appuntamento.getDataFormattata());
        logger.info(LOG_ORARIO, appuntamento.getIntervalloOrario());
        logger.info(LOG_LUOGO, appuntamento.getLuogo());
        logger.info(LOG_STATO, appuntamento.getStatoDescrizione());
    }
}
