package engclasses.pattern.viewfactory;

import controllers.grafico.cli.LoginCLIController;
import controllers.grafico.cli.RegistrazioneCLIController;
import misc.Session;
import misc.TipoUtente;
import misc.UiType;
import misc.ViewType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ViewManager {

        private static final Logger logger = LoggerFactory.getLogger(ViewManager.class);

        private ViewManager() {}

        public static void goTo(ViewType schermata) {

            UiType mode = Session.getInstance().getTipoInterfaccia();
            TipoUtente tipoUtente = Session.getInstance().getTipoUtente();  //devo andare a rivedere dove viene settato tipoUtente

            if (mode == UiType.GUI) {
                navigaGUI(schermata, tipoUtente);
            } else {
                navigaCLI(schermata, tipoUtente);
            }
        }



        private static void navigaGUI(ViewType schermata, TipoUtente tipo) {

        switch (schermata) {

            case LOGIN -> SceneManagerGUI.load("/login-view.fxml", "Login - Agroday");

            case REGISTRAZIONE -> SceneManagerGUI.load("/RegistrazioneView.fxml", "Registrazione - Agroday");

            case MAIN -> caricaMainViewGUI(tipo);

            case PROFILO -> SceneManagerGUI.load("/ProfiloView.fxml", "Il Mio Profilo");

            case CREA_ANNUNCIO -> SceneManagerGUI.load("/CreaAnnuncioView.fxml", "Crea Nuovo Annuncio");

            case VISUALIZZA_ANNUNCI -> SceneManagerGUI.load("/VisualizzaAnnunciView.fxml", "Visualizza Annunci");

            case VISUALIZZA_OFFERTE -> SceneManagerGUI.load("/VisualizzaOfferteView.fxml", "Le Mie Offerte");

            case GESTIONE_PRODOTTI -> SceneManagerGUI.load("/VisualizzaAnnunciView.fxml", "Gestione Prodotti");

            case ORDINI -> SceneManagerGUI.load("/OrdiniView.fxml", "I Miei Ordini");

            case CONSULENZA -> SceneManagerGUI.load("/ConsulenzaView.fxml", "Consulenze");

            // Appuntamenti (use case futuro)
            case LISTA_APPUNTAMENTI -> SceneManagerGUI.load("/ListaAppuntamentiView.fxml", "I Miei Appuntamenti");
            case DETTAGLIO_APPUNTAMENTO -> SceneManagerGUI.load("/DettaglioAppuntamentoView.fxml", "Dettaglio Appuntamento");
            case PRENOTAZIONE -> SceneManagerGUI.load("/PrenotazioneView.fxml", "Prenota Appuntamento");
            case CANCELLAZIONE_APPUNTAMENTO -> SceneManagerGUI.load("/CancellazioneAppuntamentoView.fxml", "Cancella Appuntamento");
            
            default -> logger.warn("Schermata non gestita per GUI: {}", schermata);
        }
    }

    private static void caricaMainViewGUI(TipoUtente tipo) {
        switch (tipo) {
            case AGRICOLTORE -> SceneManagerGUI.load("/MainView-Agricoltore.fxml", "Agroday - Pannello Agricoltore");
            case VENDITORE -> SceneManagerGUI.load("/MainView-Venditore.fxml", "Agroday - Pannello Venditore");
            case CONSULENTE -> SceneManagerGUI.load("/MainView-Consulente.fxml", "Agroday - Pannello Consulente");
            default -> logger.warn("Tipo utente non gestito: {}", tipo);
        }
    }



    private static void navigaCLI(ViewType schermata, TipoUtente tipo) {

        switch (schermata) {

            case LOGIN -> new LoginCLIController().start();

            case REGISTRAZIONE -> new RegistrazioneCLIController().start();

            case MAIN -> caricaMainViewCLI(tipo);

            case PROFILO -> CliViewManager.showProfilo();

            case GESTIONE_PRODOTTI -> CliViewManager.showGestioneProdotti();

            case ORDINI -> CliViewManager.showOrdini();

            case CONSULENZA -> CliViewManager.showConsulenza();
            
            default -> logger.warn("Schermata non gestita per CLI: {}", schermata);
        }
    }

    private static void caricaMainViewCLI(TipoUtente tipo) {
        switch (tipo) {
            case AGRICOLTORE -> CliViewManager.showMainAgricoltore();
            case VENDITORE -> CliViewManager.showMainVenditore();
            case CONSULENTE -> CliViewManager.showMainConsulente();
            default -> logger.warn("Tipo utente non gestito per CLI: {}", tipo);
        }
    }
}