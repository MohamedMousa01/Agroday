package controllers.grafico.cli;

import controllers.applicativo.AnnuncioController;
import engclasses.beans.AnnuncioBean;
import engclasses.pattern.viewfactory.ViewManager;
import misc.Session;
import misc.ViewType;
import model.Utente;
import view.cli.MainVenditoreCLIView;
import view.cli.VisualizzaAnnunciCLIView;
import view.cli.ProfiloCLIView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller CLI per il pannello principale del Venditore
 */
public class MainVenditoreCLIController {

    private final MainVenditoreCLIView view;
    private final AnnuncioController annuncioController;
    private boolean running;

    public MainVenditoreCLIController() {
        this.view = new MainVenditoreCLIView();
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
                case 1 -> cercaAnnunci();
                case 2 -> visualizzaTuttiAnnunci();
                case 3 -> visualizzaMieiOrdini();
                case 4 -> mostraStoricoAcquisti();
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

    private void cercaAnnunci() {
        String termineRicerca = view.chiediRicerca();
        
        if (termineRicerca.isEmpty()) {
            view.mostraErrore("Termine di ricerca vuoto!");
            view.attendiInvio();
            return;
        }
        
        VisualizzaAnnunciCLIView annunciView = new VisualizzaAnnunciCLIView();
        
        List<AnnuncioBean> tuttiAnnunci = annuncioController.getAnnunci();
        List<AnnuncioBean> risultati = tuttiAnnunci.stream()
                .filter(a -> a.getTitolo().toLowerCase().contains(termineRicerca.toLowerCase()) ||
                            a.getDescrizione().toLowerCase().contains(termineRicerca.toLowerCase()) ||
                            a.getCitta().toLowerCase().contains(termineRicerca.toLowerCase()))
                .collect(Collectors.toList());
        
        annunciView.mostraTitolo(false);
        
        if (risultati.isEmpty()) {
            annunciView.mostraMessaggio("Nessun annuncio trovato per: " + termineRicerca);
        } else {
            annunciView.mostraMessaggio("Trovati " + risultati.size() + " annunci per: " + termineRicerca);
            annunciView.mostraListaAnnunci(risultati);
            
            annunciView.mostraMenuAzioni(false);
            int scelta = annunciView.chiediScelta();
            
            switch (scelta) {
                case 1 -> {
                    int num = annunciView.chiediNumeroAnnuncio(risultati.size());
                    if (num > 0 && num <= risultati.size()) {
                        annunciView.mostraDettaglioAnnuncio(risultati.get(num - 1));
                    }
                }
                case 2 -> annunciView.mostraMessaggio("Contatta venditore - Funzionalità in sviluppo");
                case 3 -> annunciView.mostraMessaggio("Effettua ordine - Funzionalità in sviluppo");
                case 0 -> {
                    // Torna indietro - nessuna azione necessaria
                }
                default -> annunciView.mostraMessaggio("Opzione non valida");
            }
        }
        
        annunciView.attendiInvio();
    }

    private void visualizzaTuttiAnnunci() {
        VisualizzaAnnunciCLIView annunciView = new VisualizzaAnnunciCLIView();
        annunciView.mostraTitolo(false);
        
        List<AnnuncioBean> annunci = annuncioController.getAnnunci();
        
        if (annunci.isEmpty()) {
            annunciView.mostraMessaggio("Nessun annuncio disponibile al momento.");
        } else {
            annunciView.mostraListaAnnunci(annunci);
            annunciView.mostraMenuAzioni(false);
            
            int scelta = annunciView.chiediScelta();
            
            switch (scelta) {
                case 1 -> {
                    int num = annunciView.chiediNumeroAnnuncio(annunci.size());
                    if (num > 0 && num <= annunci.size()) {
                        annunciView.mostraDettaglioAnnuncio(annunci.get(num - 1));
                    }
                }
                case 2 -> annunciView.mostraMessaggio("Contatta venditore - Funzionalità in sviluppo");
                case 3 -> annunciView.mostraMessaggio("Effettua ordine - Funzionalità in sviluppo");
                case 0 -> {
                    // Torna indietro - nessuna azione necessaria
                }
                default -> annunciView.mostraMessaggio("Opzione non valida");
            }
        }
        
        annunciView.attendiInvio();
    }

    private void visualizzaMieiOrdini() {
        view.mostraMessaggio("🛍️ I Miei Ordini - Funzionalità in sviluppo");
        view.attendiInvio();
    }

    private void mostraStoricoAcquisti() {
        view.mostraMessaggio("📊 Storico Acquisti - Funzionalità in sviluppo");
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
