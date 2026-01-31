import controllers.applicativo.AppuntamentoController;
import engclasses.beans.AppuntamentoBean;
import misc.PersistenceType;
import misc.Session;
import misc.StatoAppuntamento;
import misc.TipoConsulenza;
import model.Consulente;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Classe di test per provare il sistema di prenotazione appuntamenti.
 * Esegui questo main per testare le funzionalità senza GUI.
 */
public class TestPrenotazione {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   TEST SISTEMA PRENOTAZIONE CONSULENZE    ");
        System.out.println("===========================================\n");

        // 1. Inizializza la sessione in modalità DEMO (Memory)
        Session session = Session.getInstance();
        session.setPersistenceType(PersistenceType.MEMORY);
        System.out.println("✓ Modalità: " + (session.isModalitaDemo() ? "DEMO (Memory)" : "FULL"));

        // 2. Simula un utente loggato (Consulente come cliente)
        Consulente utenteDemo = new Consulente(
                "client001",
                "mario_rossi",
                "mario@example.com",
                "password123",
                "Mario",
                "Rossi",
                "Roma"
        );
        session.setUtenteLoggato(utenteDemo);
        System.out.println("✓ Utente loggato: " + utenteDemo.getUsername() + "\n");

        // 3. Crea il controller applicativo
        AppuntamentoController controller = new AppuntamentoController(session);

        // ============ TEST 1: Prenotazione Consulenza ONLINE ============
        System.out.println("--- TEST 1: Prenotazione Consulenza ONLINE ---");
        try {
            AppuntamentoBean beanOnline = new AppuntamentoBean();
            beanOnline.setIdCliente("client001");
            beanOnline.setIdConsulente("cons001");
            beanOnline.setTipoConsulenza(TipoConsulenza.ONLINE);
            beanOnline.setData(LocalDate.now().plusDays(3)); // Fra 3 giorni
            beanOnline.setOraInizio(LocalTime.of(10, 0));
            beanOnline.setOraFine(LocalTime.of(11, 0));
            beanOnline.setNote("Prima consulenza di prova");
            // Per ONLINE il luogo è opzionale (verrà generato automaticamente)

            AppuntamentoBean risultato = controller.prenotaAppuntamento(beanOnline);
            System.out.println("✓ Appuntamento ONLINE creato!");
            System.out.println("  ID: " + risultato.getIdAppuntamento());
            System.out.println("  Data: " + risultato.getDataFormattata());
            System.out.println("  Orario: " + risultato.getIntervalloOrario());
            System.out.println("  Luogo (link): " + risultato.getLuogo());
            System.out.println("  Stato: " + risultato.getStatoDescrizione());
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Errore: " + e.getMessage());
        }

        // ============ TEST 2: Prenotazione Consulenza IN UFFICIO ============
        System.out.println("--- TEST 2: Prenotazione Consulenza IN UFFICIO ---");
        try {
            AppuntamentoBean beanUfficio = new AppuntamentoBean();
            beanUfficio.setIdCliente("client001");
            beanUfficio.setIdConsulente("cons002");
            beanUfficio.setTipoConsulenza(TipoConsulenza.IN_UFFICIO);
            beanUfficio.setData(LocalDate.now().plusDays(5)); // Fra 5 giorni
            beanUfficio.setOraInizio(LocalTime.of(14, 30));
            beanUfficio.setOraFine(LocalTime.of(16, 0));
            beanUfficio.setLuogo("Via Roma 123, Milano");
            beanUfficio.setNote("Portare documentazione catastale");

            AppuntamentoBean risultato = controller.prenotaAppuntamento(beanUfficio);
            System.out.println("✓ Appuntamento IN UFFICIO creato!");
            System.out.println("  ID: " + risultato.getIdAppuntamento());
            System.out.println("  Data: " + risultato.getDataFormattata());
            System.out.println("  Orario: " + risultato.getIntervalloOrario());
            System.out.println("  Luogo: " + risultato.getLuogo());
            System.out.println("  Stato: " + risultato.getStatoDescrizione());
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Errore: " + e.getMessage());
        }

        // ============ TEST 3: Prenotazione Consulenza SUL CAMPO ============
        System.out.println("--- TEST 3: Prenotazione Consulenza SUL CAMPO ---");
        try {
            AppuntamentoBean beanCampo = new AppuntamentoBean();
            beanCampo.setIdCliente("client001");
            beanCampo.setIdConsulente("cons001");
            beanCampo.setTipoConsulenza(TipoConsulenza.SUL_CAMPO);
            beanCampo.setData(LocalDate.now().plusDays(7)); // Fra 7 giorni
            beanCampo.setOraInizio(LocalTime.of(9, 0));
            beanCampo.setOraFine(LocalTime.of(12, 0)); // 3 ore (minimo 60 min richiesto)
            beanCampo.setLuogo("Azienda Agricola Verdi, Strada Provinciale 45, Latina");
            beanCampo.setNote("Ispezione terreno per nuova coltivazione");

            AppuntamentoBean risultato = controller.prenotaAppuntamento(beanCampo);
            System.out.println("✓ Appuntamento SUL CAMPO creato!");
            System.out.println("  ID: " + risultato.getIdAppuntamento());
            System.out.println("  Data: " + risultato.getDataFormattata());
            System.out.println("  Orario: " + risultato.getIntervalloOrario());
            System.out.println("  Durata: " + risultato.getDurataMinuti() + " minuti");
            System.out.println("  Luogo: " + risultato.getLuogo());
            System.out.println("  Stato: " + risultato.getStatoDescrizione());
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Errore: " + e.getMessage());
        }

        // ============ TEST 4: Visualizza tutti gli appuntamenti ============
        System.out.println("--- TEST 4: Lista Appuntamenti Utente ---");
        List<AppuntamentoBean> appuntamenti = controller.getAppuntamentiUtenteCorrente();
        System.out.println("Totale appuntamenti: " + appuntamenti.size());
        for (AppuntamentoBean app : appuntamenti) {
            System.out.println("  • " + app.getDataFormattata() + " " + app.getIntervalloOrario() + 
                    " - " + app.getTipoConsulenzaDescrizione() + " [" + app.getStatoDescrizione() + "]");
        }
        System.out.println();

        // ============ TEST 5: Conferma un appuntamento ============
        System.out.println("--- TEST 5: Conferma Appuntamento ---");
        if (!appuntamenti.isEmpty()) {
            String idDaConfermare = appuntamenti.get(0).getIdAppuntamento();
            boolean confermato = controller.confermaAppuntamento(idDaConfermare);
            System.out.println(confermato ? "✓ Appuntamento confermato!" : "✗ Conferma fallita");
            
            // Verifica lo stato aggiornato
            AppuntamentoBean aggiornato = controller.getAppuntamento(idDaConfermare);
            System.out.println("  Nuovo stato: " + aggiornato.getStatoDescrizione());
        }
        System.out.println();

        // ============ TEST 6: Cancella un appuntamento ============
        System.out.println("--- TEST 6: Cancellazione Appuntamento ---");
        if (appuntamenti.size() > 1) {
            String idDaCancellare = appuntamenti.get(1).getIdAppuntamento();
            boolean cancellato = controller.cancellaAppuntamentoCliente(idDaCancellare, "Test cancellazione");
            System.out.println(cancellato ? "✓ Appuntamento cancellato!" : "✗ Cancellazione fallita");
            
            // Verifica lo stato aggiornato
            AppuntamentoBean aggiornato = controller.getAppuntamento(idDaCancellare);
            System.out.println("  Nuovo stato: " + aggiornato.getStatoDescrizione());
            System.out.println("  Motivo: " + aggiornato.getMotivoCancellazione());
        }
        System.out.println();

        // ============ TEST 7: Preparazione Consulenza (Strategy) ============
        System.out.println("--- TEST 7: Istruzioni Preparazione Consulenza ---");
        if (!appuntamenti.isEmpty()) {
            String idAppuntamento = appuntamenti.get(0).getIdAppuntamento();
            String istruzioni = controller.preparaConsulenza(idAppuntamento);
            System.out.println(istruzioni);
        }
        System.out.println();

        // ============ TEST 8: Verifica conflitto orario ============
        System.out.println("--- TEST 8: Test Conflitto Orario ---");
        try {
            // Prova a prenotare nello stesso orario dello stesso consulente
            AppuntamentoBean beanConflitto = new AppuntamentoBean();
            beanConflitto.setIdCliente("client001");
            beanConflitto.setIdConsulente("cons001"); // Stesso consulente del TEST 1
            beanConflitto.setTipoConsulenza(TipoConsulenza.ONLINE);
            beanConflitto.setData(LocalDate.now().plusDays(3)); // Stessa data del TEST 1
            beanConflitto.setOraInizio(LocalTime.of(10, 30)); // Orario sovrapposto!
            beanConflitto.setOraFine(LocalTime.of(11, 30));

            controller.prenotaAppuntamento(beanConflitto);
            System.out.println("✗ Avrebbe dovuto rilevare il conflitto!");
        } catch (IllegalStateException e) {
            System.out.println("✓ Conflitto rilevato correttamente: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
        System.out.println();

        // ============ Riepilogo finale ============
        System.out.println("===========================================");
        System.out.println("   RIEPILOGO FINALE                        ");
        System.out.println("===========================================");
        List<AppuntamentoBean> tuttiAppuntamenti = controller.getAppuntamentiUtenteCorrente();
        int prenotati = 0, confermati = 0, cancellati = 0;
        for (AppuntamentoBean app : tuttiAppuntamenti) {
            if (app.getStato() == StatoAppuntamento.PRENOTATO) prenotati++;
            else if (app.getStato() == StatoAppuntamento.CONFERMATO) confermati++;
            else if (app.getStato().isCancellato()) cancellati++;
        }
        System.out.println("Totale appuntamenti: " + tuttiAppuntamenti.size());
        System.out.println("  - Prenotati: " + prenotati);
        System.out.println("  - Confermati: " + confermati);
        System.out.println("  - Cancellati: " + cancellati);
        System.out.println("\n✓ Test completato con successo!");
    }
}
