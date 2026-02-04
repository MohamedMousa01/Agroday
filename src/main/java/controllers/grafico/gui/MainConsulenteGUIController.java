package controllers.grafico.gui;

import engclasses.pattern.viewfactory.ViewManager;
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
 * Controller per la view principale del Consulente
 */
public class MainConsulenteGUIController {

    @FXML private Label lblBenvenuto;
    @FXML private Button btnRichieste;
    @FXML private Button btnConsulenzeAttive;
    @FXML private Button btnClienti;
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
    private void visualizzaRichieste() {
        // Naviga alla lista appuntamenti (mostra tutti gli appuntamenti del consulente)
        ViewManager.goTo(ViewType.LISTA_APPUNTAMENTI);
    }



    @FXML
    private void visualizzaClienti() {
        // Funzionalità in sviluppo: view dedicata per clienti
        mostraMessaggio("I Miei Clienti", 
                       """
                       La gestione clienti sarà disponibile a breve!
                       
                       Potrai:
                       • Visualizzare elenco clienti
                       • Storico consulenze per cliente
                       • Note e documenti condivisi""",
                       Alert.AlertType.INFORMATION);
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
