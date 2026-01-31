package controllers.grafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import misc.Session;
import java.io.IOException;

public class MainGUIController {

    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private Button creaAnnuncioButton;
    @FXML private Button cercaAnnunciButton;
    @FXML private Button mieiAnnunciButton;
    @FXML private Button mieiAppuntamentiButton;
    @FXML private Button prenotaConsulenzaButton;
    @FXML private StackPane contentPane;
    @FXML private VBox annunciListContainer;

    private Session session;

    @FXML
    public void initialize() {
        session = Session.getInstance();
        
        // Imposta il messaggio di benvenuto
        if (session.getUtenteLoggato() != null) {
            String tipoUtente = session.getUtenteLoggato().getTipo().name();
            welcomeLabel.setText("Benvenuto, " + session.getUtenteLoggato().getUsername() + " (" + tipoUtente + ")");
        }

        // Mostra indicatore modalità
        if (session.isModalitaDemo()) {
            System.out.println("[INFO] Modalità DEMO attiva - I dati non saranno persistiti");
        } else {
            System.out.println("[INFO] Modalità FULL attiva - Persistenza e Google Calendar abilitati");
        }
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
            session.clearSession();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Errore nel caricamento della pagina di login.");
        }
    }

    // ==================== Navigazione Annunci ====================

    @FXML
    private void showCreaAnnuncio() {
        System.out.println("Mostra Crea Annuncio");
        // loadViewIntoCenter("/CreaAnnuncioView.fxml");
    }

    @FXML
    private void showCercaAnnunci() {
        System.out.println("Mostra Cerca Annunci");
        // loadViewIntoCenter("/CercaAnnunciView.fxml");
    }

    @FXML
    private void showMieiAnnunci() {
        System.out.println("Mostra I Miei Annunci");
        // loadViewIntoCenter("/MieiAnnunciView.fxml");
    }

    // ==================== Navigazione Appuntamenti/Consulenze ====================

    @FXML
    private void showMieiAppuntamenti() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListaAppuntamentiView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mieiAppuntamentiButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("I Miei Appuntamenti - Agroday");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Errore nel caricamento della lista appuntamenti.");
        }
    }

    @FXML
    private void showPrenotaConsulenza() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrenotazioneView.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Prenota Consulenza - Agroday");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Errore nel caricamento della finestra di prenotazione.");
        }
    }

    // ==================== Metodi di Utilità ====================

    private void loadViewIntoCenter(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Errore nel caricamento della vista: " + fxmlPath);
        }
    }

    private void showErrorAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
