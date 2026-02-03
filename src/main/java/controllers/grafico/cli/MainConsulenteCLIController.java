package controllers.grafico.cli;

import engclasses.pattern.ViewFactory.ViewManager;
import misc.MessageConstants;
import misc.Session;
import misc.ViewType;
import model.Utente;
import view.cli.MainConsulenteCLIView;
import view.cli.ProfiloCLIView;

/**
 * Controller CLI per il pannello principale del Consulente
 */
public class MainConsulenteCLIController {

    private final MainConsulenteCLIView view;
    private boolean running;

    public MainConsulenteCLIController() {
        this.view = new MainConsulenteCLIView();
        this.running = true;
    }

    public void start() {
        Utente utente = Session.getInstance().getUtenteLoggato();
        
        if (utente == null) {
            System.out.println("❌ Errore: nessun utente loggato!");
            ViewManager.goTo(ViewType.LOGIN);
            return;
        }

        while (running) {
            view.mostraTitolo(utente);
            view.mostraMenu();
            
            int scelta = view.chiediScelta();
            
            switch (scelta) {
                case 1 -> visualizzaRichiesteConsulenza();
                case 2 -> visualizzaRichiesteAccettate();
                case 3 -> mostraStatistiche();
                case 4 -> visualizzaMieiClienti();
                case 5 -> visualizzaProfilo();
                case 6 -> mostraImpostazioni();
                case 0 -> logout();
                default -> {
                    view.mostraErrore("Opzione non valida!");
                    view.attendiInvio();
                }
            }
        }
    }

    private void visualizzaRichiesteConsulenza() {
        view.mostraMessaggio("📬 Richieste di Consulenza");
        view.mostraSeparatore();
        view.mostraMessaggio("Nessuna richiesta al momento.");
        view.mostraMessaggio(MessageConstants.FUNZIONALITA_IN_SVILUPPO);
        view.attendiInvio();
    }

    private void visualizzaRichiesteAccettate() {
        view.mostraMessaggio("✅ Richieste Accettate");
        view.mostraSeparatore();
        view.mostraMessaggio("Nessuna richiesta accettata.");
        view.mostraMessaggio(MessageConstants.FUNZIONALITA_IN_SVILUPPO);
        view.attendiInvio();
    }

    private void mostraStatistiche() {
        view.mostraMessaggio("📊 Statistiche e Report");
        view.mostraSeparatore();
        System.out.println("\n  📬 Richieste ricevute: 0");
        System.out.println("  ✅ Richieste accettate: 0");
        System.out.println("  ⏳ Richieste in corso: 0");
        System.out.println("  ✔️  Richieste completate: 0");
        view.mostraSeparatore();
        view.attendiInvio();
    }

    private void visualizzaMieiClienti() {
        view.mostraMessaggio("👥 I Miei Clienti");
        view.mostraSeparatore();
        view.mostraMessaggio("Nessun cliente al momento.");
        view.mostraMessaggio(MessageConstants.FUNZIONALITA_IN_SVILUPPO);
        view.attendiInvio();
    }

    private void visualizzaProfilo() {
        ProfiloCLIView profiloView = new ProfiloCLIView();
        profiloView.mostraTitolo();
        profiloView.mostraProfilo(
            Session.getInstance().getUtenteLoggato(),
            Session.getInstance().getTipoUtente()
        );
        profiloView.mostraMenu();
        
        int scelta = profiloView.chiediScelta();
        
        switch (scelta) {
            case 1, 2, 3, 4, 5 -> {
                profiloView.mostraMessaggio("Modifica profilo - Funzionalità in sviluppo");
                profiloView.attendiInvio();
            }
            default -> {
                // Nessuna azione necessaria
            }
        }
    }

    private void mostraImpostazioni() {
        view.mostraMessaggio("⚙️ Impostazioni - Funzionalità in sviluppo");
        view.attendiInvio();
    }

    private void logout() {
        if (view.confermaAzione("Sei sicuro di voler uscire?")) {
            view.mostraSuccesso("Logout effettuato con successo!");
            Session.getInstance().clearSession();
            running = false;
            ViewManager.goTo(ViewType.LOGIN);
        }
    }
}
