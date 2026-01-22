package controllers.grafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox; // Importa VBox
import javafx.stage.Stage;
import misc.Session;
import java.io.IOException;

public class MainGUIController {

    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private Button creaAnnuncioButton;
    @FXML private Button cercaAnnunciButton;
    @FXML private Button mieiAnnunciButton;
    @FXML private StackPane contentPane; // Area per il contenuto dinamico
    @FXML private VBox annunciListContainer; // Nuovo: per l'elenco annunci in formato orizzontale

    private Session session;

    @FXML
    public void initialize() {
        session = Session.getInstance();
        // Placeholder per impostare il testo di benvenuto, ad esempio:
        // if (session.getUtenteCorrente() != null) {
        //     welcomeLabel.setText("Benvenuto, " + session.getUtenteCorrente().getUsername() + "!");
        // }

        // Qui potresti popolare il annunciListContainer con annunci di esempio o caricarli dal database
        // Esempio: populateAnnunciList();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Agroday");
            stage.show();
            session.clearSession(); // Pulisce i dati della sessione
        } catch (IOException e) {
            e.printStackTrace();
            // Gestire l'errore di caricamento
        }
    }

    @FXML
    private void showCreaAnnuncio() {
        System.out.println("Mostra Crea Annuncio");
        // Esempio: loadViewIntoCenter("/path/to/crea-annuncio-view.fxml");
    }

    @FXML
    private void showCercaAnnunci() {
        System.out.println("Mostra Cerca Annunci");
        // Esempio: loadViewIntoCenter("/path/to/cerca-annunci-view.fxml");
    }

    @FXML
    private void showMieiAnnunci() {
        System.out.println("Mostra I Miei Annunci");
        // Esempio: loadViewIntoCenter("/path/to/miei-annunci-view.fxml");
    }

    // Metodo di supporto per caricare un FXML nel contentPane (o sostituire il contenuto di annunciListContainer)
    private void loadViewIntoCenter(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view); // Sostituisce l'intero contenuto del StackPane
            // Se volessi sostituire solo il contenuto della lista:
            // annunciListContainer.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            // Gestire l'errore di caricamento della vista
        }
    }
}
