package controllers.grafico.cli;

import controllers.applicativo.AnnuncioController;
import engclasses.beans.AnnuncioBean;
import engclasses.exceptions.AnnuncioNonValidoException;
import engclasses.pattern.ViewFactory.ViewManager;
import misc.Session;
import misc.ViewType;
import model.Utente;
import view.cli.MainAgricoltoreCLIView;
import view.cli.CreaAnnuncioCLIView;
import view.cli.VisualizzaAnnunciCLIView;
import view.cli.ProfiloCLIView;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller CLI per il pannello principale dell'Agricoltore
 */
public class MainAgricoltoreCLIController {

    private final MainAgricoltoreCLIView view;
    private final AnnuncioController annuncioController;
    private boolean running;

    public MainAgricoltoreCLIController() {
        this.view = new MainAgricoltoreCLIView();
        this.annuncioController = new AnnuncioController();
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
                case 1 -> creaNuovoAnnuncio();
                case 2 -> visualizzaMieiAnnunci();
                case 3 -> visualizzaTuttiAnnunci();
                case 4 -> mostraStatistiche();
                case 5 -> richiediConsulenza();
                case 6 -> visualizzaProfilo();
                case 7 -> mostraImpostazioni();
                case 0 -> logout();
                default -> {
                    view.mostraErrore("Opzione non valida!");
                    view.attendiInvio();
                }
            }
        }
    }

    private void creaNuovoAnnuncio() {
        CreaAnnuncioCLIView creaView = new CreaAnnuncioCLIView();
        creaView.mostraTitolo();
        
        try {
            // Input dati annuncio
            String titolo = creaView.chiediTitolo();
            if (titolo.isEmpty()) {
                creaView.mostraErrore("Il titolo non può essere vuoto!");
                creaView.attendiInvio();
                return;
            }
            
            String descrizione = creaView.chiediDescrizione();
            String nomeProdotto = creaView.chiediNomeProdotto();
            
            int quantita = creaView.chiediQuantita();
            if (quantita <= 0) {
                creaView.mostraErrore("La quantità deve essere maggiore di zero!");
                creaView.attendiInvio();
                return;
            }
            
            String citta = creaView.chiediCitta();
            LocalDate dataScadenza = creaView.chiediDataScadenza();
            
            if (dataScadenza == null) {
                creaView.attendiInvio();
                return;
            }
            
            // Mostra riepilogo
            creaView.mostraRiepilogo(titolo, descrizione, nomeProdotto, quantita, citta, dataScadenza);
            
            if (creaView.confermaCreazione()) {
                // Crea l'annuncio
                String autore = Session.getInstance().getUtenteLoggato().getUsername();
                annuncioController.creaAnnuncio(autore, titolo, descrizione, dataScadenza, 
                                                nomeProdotto, quantita, citta);
                
                creaView.mostraSuccesso("Annuncio creato con successo!");
                
                if (creaView.vuoiCreareAltroAnnuncio()) {
                    creaNuovoAnnuncio();
                }
            } else {
                creaView.mostraMessaggio("Creazione annullata.");
            }
            
        } catch (AnnuncioNonValidoException e) {
            creaView.mostraErrore(e.getMessage());
        }
        
        creaView.attendiInvio();
    }

    private void visualizzaMieiAnnunci() {
        VisualizzaAnnunciCLIView annunciView = new VisualizzaAnnunciCLIView();
        annunciView.mostraTitolo(true);
        
        // Ottieni annunci dell'utente loggato
        String username = Session.getInstance().getUtenteLoggato().getUsername();
        List<AnnuncioBean> tuttiAnnunci = annuncioController.getAnnunci();
        List<AnnuncioBean> mieiAnnunci = tuttiAnnunci.stream()
                .filter(a -> a.getAutore().equals(username))
                .collect(Collectors.toList());
        
        if (mieiAnnunci.isEmpty()) {
            annunciView.nessunoAnnuncio();
        } else {
            annunciView.mostraListaAnnunci(mieiAnnunci);
            annunciView.mostraMenuAzioni(true);
            
            int scelta = annunciView.chiediScelta();
            
            switch (scelta) {
                case 1 -> annunciView.mostraMessaggio("Modifica annuncio - Funzionalità in sviluppo");
                case 2 -> {
                    int num = annunciView.chiediNumeroAnnuncio(mieiAnnunci.size());
                    if (num > 0 && num <= mieiAnnunci.size() && annunciView.confermaEliminazione()) {
                        annunciView.mostraSuccesso("Annuncio eliminato! (Funzionalità da implementare)");
                    }
                }
                case 3 -> {
                    int num = annunciView.chiediNumeroAnnuncio(mieiAnnunci.size());
                    if (num > 0 && num <= mieiAnnunci.size()) {
                        annunciView.mostraDettaglioAnnuncio(mieiAnnunci.get(num - 1));
                    }
                }
            }
        }
        
        annunciView.attendiInvio();
    }

    private void visualizzaTuttiAnnunci() {
        VisualizzaAnnunciCLIView annunciView = new VisualizzaAnnunciCLIView();
        annunciView.mostraTitolo(false); // false = non "Miei Annunci" ma "Tutti gli Annunci"
        
        // Ottieni TUTTI gli annunci (senza filtro)
        List<AnnuncioBean> tuttiAnnunci = annuncioController.getAnnunci();
        
        if (tuttiAnnunci.isEmpty()) {
            annunciView.mostraMessaggio("Non ci sono annunci disponibili al momento.");
        } else {
            annunciView.mostraListaAnnunci(tuttiAnnunci);
            annunciView.mostraMenuAzioni(false); // false = non mostrare opzioni di modifica/elimina
            
            int scelta = annunciView.chiediScelta();
            
            if (scelta > 0 && scelta <= tuttiAnnunci.size()) {
                annunciView.mostraDettaglioAnnuncio(tuttiAnnunci.get(scelta - 1));
            }
        }
        
        annunciView.attendiInvio();
    }

    private void mostraStatistiche() {
        view.mostraMessaggio("📊 Statistiche Annunci");
        view.mostraSeparatore();
        
        String username = Session.getInstance().getUtenteLoggato().getUsername();
        List<AnnuncioBean> mieiAnnunci = annuncioController.getAnnunci().stream()
                .filter(a -> a.getAutore().equals(username))
                .collect(Collectors.toList());
        
        System.out.println("\n  📋 Totale annunci pubblicati: " + mieiAnnunci.size());
        System.out.println("  ✅ Annunci attivi: " + mieiAnnunci.size());
        System.out.println("  📅 Annunci scaduti: 0");
        
        view.mostraSeparatore();
        view.attendiInvio();
    }

    private void richiediConsulenza() {
        view.mostraMessaggio("💬 Richiedi Consulenza - Funzionalità in sviluppo");
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
