package controllers.grafico.gui;

import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.exceptions.RegistrazioneFallitaException;
import engclasses.pattern.viewfactory.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import misc.Session;
import misc.PersistenceType;
import misc.TipoUtente;
import misc.ViewType;
import engclasses.beans.RegistrazioneBean;
import controllers.applicativo.RegistrazioneController;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrazioneGUIController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrazioneGUIController.class);

    // ==================== CAMPI FXML ====================

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confermaPasswordField;
    @FXML private TextField cittaField;

    @FXML private TextField titoloDiStudioField;
    @FXML private Label titoloDiStudioLabel;

    // Tipo utente
    @FXML private RadioButton agricoltoreRadio;
    @FXML private RadioButton venditoreRadio;
    @FXML private RadioButton consulenteRadio;

    // Persistenza
    @FXML private RadioButton memoryRadio;
    @FXML private RadioButton fileRadio;
    @FXML private RadioButton dbRadio;

    @FXML private Button registratiButton;
    @FXML private Hyperlink loginLink;

    // ==================== ALTRI ATTRIBUTI ====================

    private Session session;
    private RegistrazioneController controllerApplicativo;

    // ==================== INITIALIZE ====================

    @FXML
    public void initialize() {
        session = Session.getInstance();
        controllerApplicativo = new RegistrazioneController(Session.getInstance());

        // Mostra/nasconde titolo di studio
        consulenteRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            titoloDiStudioLabel.setVisible(newVal);
            titoloDiStudioLabel.setManaged(newVal);
            titoloDiStudioField.setVisible(newVal);
            titoloDiStudioField.setManaged(newVal);
        });



    }

    // ==================== REGISTRAZIONE ====================

    @FXML
    private void handleRegistrazione() {
        logger.debug("handleRegistrazione() triggered.");

        try {
            logger.debug("Calling determinaPersistenza().");


            // 2️⃣ Persistenza
            PersistenceType persistenceType = determinaPersistenza();
            logger.debug("PersistenceType determined: {}. Setting session.", persistenceType);
            session.setPersistenceType(persistenceType);
            logger.debug("Calling creaBean().");

            // 3️⃣ Bean
            RegistrazioneBean bean = creaBean();
            logger.debug("RegistrazioneBean created. Calling registraUtente().");

            // 4️⃣ Chiamata controller applicativo
            controllerApplicativo.registraUtente(bean);
            logger.info("User registered successfully: {}", bean.getUsername());

            // 5️⃣ Feedback e navigazione
            mostraAlert(Alert.AlertType.INFORMATION,
                    "Registrazione completata",
                    "Utente registrato con successo!");
            
            // Naviga al login usando ViewManager
            ViewManager.goTo(ViewType.LOGIN);

        } catch (RegistrazioneFallitaException e) {
            mostraAlert(Alert.AlertType.ERROR, "Errore di validazione", e.getMessage());

        } catch (DatabaseConnessioneFallitaException | DatabaseOperazioneFallitaException e) {
            mostraAlert(Alert.AlertType.ERROR, "Errore di sistema", e.getMessage());
        } catch (RuntimeException e) { // Catch all other unexpected runtime exceptions
            logger.error("Errore inatteso durante la registrazione", e);
            mostraAlert(Alert.AlertType.ERROR, "Errore inatteso", "Si è verificato un errore inatteso: " + e.getMessage());
        }
    }

    // ==================== METODI DI SUPPORTO ====================

    private TipoUtente determinaTipoUtente() {
        if (agricoltoreRadio.isSelected()) return TipoUtente.AGRICOLTORE;
        if (venditoreRadio.isSelected()) return TipoUtente.VENDITORE;
        return TipoUtente.CONSULENTE;
    }

    private PersistenceType determinaPersistenza() {
        if (dbRadio.isSelected()) return PersistenceType.DB;
        if (fileRadio.isSelected()) return PersistenceType.FILE;
        return PersistenceType.MEMORY;
    }

    private RegistrazioneBean creaBean() {
        RegistrazioneBean bean = new RegistrazioneBean();

        bean.setNome(nomeField.getText());
        bean.setCognome(cognomeField.getText());
        bean.setUsername(usernameField.getText());
        bean.setEmail(emailField.getText());
        bean.setPassword(passwordField.getText());
        bean.setConfirmPassword(confermaPasswordField.getText());
        bean.setPersistenceType(determinaPersistenza());
        bean.setTipoUtente(determinaTipoUtente());
        bean.setCitta(cittaField.getText());

        return bean;
    }

    private void mostraAlert(Alert.AlertType tipo, String titolo, String messaggio) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    // ==================== NAVIGAZIONE ====================

    @FXML
    private void handleLoginLink() {
        // Naviga al login usando ViewManager
        ViewManager.goTo(ViewType.LOGIN);
    }


}

