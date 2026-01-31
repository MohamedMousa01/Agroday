package controllers.grafico.gui;

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
 * Controller per la view principale del Venditore
 */
public class MainVenditoreGUIController {

    @FXML private Label lblBenvenuto;
    @FXML private Button btnCercaAnnunci;
    @FXML private Button btnVisualizzaAnnunci;
    @FXML private Button btnOrdini;
    @FXML private Button btnProfilo;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        // Imposta messaggio di benvenuto
        Utente utente = Session.getInstance().getUtenteLoggato();
        if (utente != null) {
            lblBenvenuto.setText("Benvenuto, " + utente.getNome() + " " + utente.getCognome() + "!");
        }
    }

    @FXML
    private void cercaAnnunci() {
        // Naviga alla lista annunci (che ha già la ricerca integrata)
        ViewManager.goTo(ViewType.VISUALIZZA_ANNUNCI);
    }

    @FXML
    private void visualizzaTuttiAnnunci() {
        // Naviga alla lista completa annunci
        ViewManager.goTo(ViewType.VISUALIZZA_ANNUNCI);
    }

    @FXML
    private void visualizzaOrdini() {
        // Naviga agli ordini
        ViewManager.goTo(ViewType.ORDINI);
    }

    @FXML
    private void visualizzaProfilo() {
        // Naviga al profilo
        ViewManager.goTo(ViewType.PROFILO);
    }

    @FXML
    private void logout() {
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

    private void mostraMessaggio(String titolo, String messaggio, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
