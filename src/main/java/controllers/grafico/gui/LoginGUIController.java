package controllers.grafico.gui;

import controllers.applicativo.LoginController;
import engclasses.beans.LoginBean;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.exceptions.LoginFallitoException;
import misc.PersistenceType;
import misc.Session;

import java.io.IOException;

public class LoginGUIController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML private RadioButton memoryRadio;
    @FXML private RadioButton fileRadio;
    @FXML private RadioButton dbRadio;
    @FXML private ToggleGroup persistenceGroup;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loginButton;

    @FXML private Hyperlink registrazioneLink;

    private Session session; // Non più final, inizializzata in initialize()

    public LoginGUIController() {
        // Costruttore senza argomenti richiesto da FXMLLoader
    }


    @FXML
    private void initialize() {
        session = Session.getInstance(); // Inizializza sessione qui
        // Imposta il tipo di persistenza iniziale (Memory di default)
        session.setPersistenceType(PersistenceType.MEMORY);

        LoginController controller = new LoginController(session);

        // Aggiunge un listener al ToggleGroup per aggiornare la persistenza nella sessione
        persistenceGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == memoryRadio) {
                session.setPersistenceType(PersistenceType.MEMORY);
            } else if (newVal == fileRadio) {
                session.setPersistenceType(PersistenceType.FILE);
            } else if (newVal == dbRadio) {
                session.setPersistenceType(PersistenceType.DB);
            }
        });
    }


    @FXML
    public void onLoginClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validazione UI (se necessario, altrimenti il controller applicativo lo farà)
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            messageLabel.setText("Inserisci username e password!");
            return;
        }

        try {
            LoginController loginController = new LoginController(session);
            boolean autenticato = loginController.autentica(username, password);

            if (autenticato) {
                messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                messageLabel.setText("Accesso effettuato!");
                // Naviga alla schermata principale
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Agroday");
                stage.show();
            } else {
                messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                messageLabel.setText("Username o password errati!");
            }
        } catch (LoginFallitoException e) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            messageLabel.setText(e.getMessage());
        } catch (DatabaseConnessioneFallitaException | DatabaseOperazioneFallitaException e) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            messageLabel.setText("Errore di sistema: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            messageLabel.setText("Errore di caricamento interfaccia: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistrazioneLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrazioneView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registrazioneLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrazione - Agroday");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            messageLabel.setText("Errore di caricamento interfaccia: " + e.getMessage());
        }
    }
}