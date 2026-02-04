package controllers.grafico.gui;

import controllers.applicativo.LoginController;
import engclasses.pattern.viewfactory.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.exceptions.LoginFallitoException;
import misc.PersistenceType;
import misc.CSSConstants;
import misc.Session;
import misc.ViewType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginGUIController {

    private static final Logger logger = LoggerFactory.getLogger(LoginGUIController.class);

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
            messageLabel.setStyle(CSSConstants.ERROR_TEXT_STYLE);
            messageLabel.setText("Inserisci username e password!");
            return;
        }

        try {
            LoginController loginController = new LoginController(session);
            boolean autenticato = loginController.autentica(username, password);

            if (autenticato) {
                messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                messageLabel.setText("Accesso effettuato!");
                // Naviga alla schermata principale usando ViewManager
                ViewManager.goTo(ViewType.MAIN);
            } else {
                messageLabel.setStyle(CSSConstants.ERROR_TEXT_STYLE);
                messageLabel.setText("Username o password errati!");
            }
        } catch (LoginFallitoException e) {
            messageLabel.setStyle(CSSConstants.ERROR_TEXT_STYLE);
            messageLabel.setText(e.getMessage());
        } catch (DatabaseConnessioneFallitaException | DatabaseOperazioneFallitaException e) {
            messageLabel.setStyle(CSSConstants.ERROR_TEXT_STYLE);
            messageLabel.setText("Errore di sistema: " + e.getMessage());
            logger.error("Errore durante il login per l'utente: {}", username, e);
        }
    }

    @FXML
    private void handleRegistrazioneLink() {
        // Naviga alla registrazione usando ViewManager
        ViewManager.goTo(ViewType.REGISTRAZIONE);
    }
}