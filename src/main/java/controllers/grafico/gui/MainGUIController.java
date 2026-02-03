package controllers.grafico.gui;

import engclasses.pattern.ViewFactory.SceneManagerGUI;
import engclasses.pattern.ViewFactory.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import misc.Session;
import misc.ViewType;
import model.Utente;

import java.util.Optional;

/**
 * Controller per la view principale dell'Agricoltore
 */
public class MainGUIController {

    @FXML private Label welcomeLabel;
    @FXML private Button creaAnnuncioButton;
    @FXML private Button cercaAnnunciButton;
    @FXML private Button mieiAnnunciButton;
    @FXML private Button miePartecipazioniButton;
    @FXML private Button consulenzaButton;
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {
        // Imposta messaggio di benvenuto
        Utente utente = Session.getInstance().getUtenteLoggato();
        if (utente != null) {
            welcomeLabel.setText("Benvenuto, " + utente.getNome() + " " + utente.getCognome() + "!");
        }
    }

    @FXML
    private void showCreaAnnuncio() {
        // Naviga alla view di creazione annuncio
        ViewManager.goTo(ViewType.CREA_ANNUNCIO);
    }

    @FXML
    private void showCercaAnnunci() {
        // Naviga alla lista di tutti gli annunci
        SceneManagerGUI.setMostraSoloMieiAnnunci(false);
        ViewManager.goTo(ViewType.VISUALIZZA_ANNUNCI);
    }

    @FXML
    private void showMieiAnnunci() {
        // Naviga alla lista filtrata per annunci personali
        SceneManagerGUI.setMostraSoloMieiAnnunci(true);
        SceneManagerGUI.setMostraPartecipazioniUtente(false);
        ViewManager.goTo(ViewType.VISUALIZZA_ANNUNCI);
    }

    @FXML
    private void showMiePartecipazioni() {
        // Naviga alla lista degli annunci a cui l'utente ha partecipato
        SceneManagerGUI.setMostraSoloMieiAnnunci(false);
        SceneManagerGUI.setMostraPartecipazioniUtente(true);
        ViewManager.goTo(ViewType.VISUALIZZA_ANNUNCI);
    }

    @FXML
    private void showChiediConsulenza() {
        // Naviga alla view per richiedere consulenza
        ViewManager.goTo(ViewType.PRENOTAZIONE);
    }

    @FXML
    private void showOfferteRicevute() {
        // Naviga alla view delle offerte ricevute
        ViewManager.goTo(ViewType.VISUALIZZA_OFFERTE);
    }

    @FXML
    private void showListaAppuntamenti() {
        // Naviga alla lista appuntamenti
        ViewManager.goTo(ViewType.LISTA_APPUNTAMENTI);
    }

    @FXML
    private void visualizzaProfilo() {
        // Naviga al profilo
        ViewManager.goTo(ViewType.PROFILO);
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText("Sei sicuro di voler uscire?");
        alert.setContentText("Dovrai effettuare nuovamente il login per accedere.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Session.getInstance().clearSession();
            ViewManager.goTo(ViewType.LOGIN);
        }
    }

}
