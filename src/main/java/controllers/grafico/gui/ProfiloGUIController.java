package controllers.grafico.gui;

import controllers.applicativo.AnnuncioController;
import engclasses.beans.AnnuncioBean;
import engclasses.pattern.viewfactory.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import misc.Session;
import misc.TipoUtente;
import misc.ViewType;
import model.Utente;

import java.util.List;

/**
 * Controller per la view del profilo utente
 */
public class ProfiloGUIController {

    @FXML private Button btnIndietro;
    @FXML private TextField txtNome;
    @FXML private TextField txtCognome;
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCitta;
    @FXML private TextField txtTipoUtente;
    @FXML private Button btnModifica;
    @FXML private Button btnCambiaPassword;
    @FXML private VBox vboxStatistiche;
    @FXML private Label lblNumAnnunci;
    @FXML private Label lblNumOrdini;

    @FXML
    public void initialize() {
        caricaDatiProfilo();
    }

    private void caricaDatiProfilo() {
        Utente utente = Session.getInstance().getUtenteLoggato();
        TipoUtente tipo = Session.getInstance().getTipoUtente();

        if (utente != null) {
            txtNome.setText(utente.getNome());
            txtCognome.setText(utente.getCognome());
            txtUsername.setText(utente.getUsername());
            txtEmail.setText(utente.getEmail());
            txtCitta.setText(utente.getCitta());
            
            if (tipo != null) {
                txtTipoUtente.setText(getTipoUtenteLabel(tipo));
            }

            // Mostra statistiche se è agricoltore
            if (tipo == TipoUtente.AGRICOLTORE) {
                mostraStatistiche(utente.getUsername());
            }
        }
    }

    private String getTipoUtenteLabel(TipoUtente tipo) {
        return switch (tipo) {
            case AGRICOLTORE -> "🌾 Agricoltore";
            case VENDITORE -> "🛒 Venditore";
            case CONSULENTE -> "🎓 Consulente";
        };
    }

    private void mostraStatistiche(String username) {
        vboxStatistiche.setVisible(true);
        vboxStatistiche.setManaged(true);

        try {
            AnnuncioController annuncioController = new AnnuncioController();
            List<AnnuncioBean> tuttiAnnunci = annuncioController.getAnnunci();
            
            // Filtra annunci dell'utente
            List<AnnuncioBean> mieiAnnunci = tuttiAnnunci.stream()
                    .filter(a -> a.getAutore().equals(username))
                    .toList();
            
            lblNumAnnunci.setText(String.valueOf(mieiAnnunci.size()));
            lblNumOrdini.setText("0"); // Nota: Il conteggio ordini sarà implementato quando il sistema ordini sarà disponibile
            
        } catch (Exception e) {
            lblNumAnnunci.setText("N/A");
            lblNumOrdini.setText("N/A");
        }
    }

    @FXML
    private void tornaIndietro() {
        // Torna alla schermata principale
        ViewManager.goTo(ViewType.MAIN);
    }

    @FXML
    private void modificaProfilo() {
        mostraMessaggio("Modifica Profilo", 
                       """
                       La funzionalità di modifica del profilo sarà disponibile a breve!
                       
                       Potrai modificare:
                       • Nome e Cognome
                       • Email
                       • Città""",
                       Alert.AlertType.INFORMATION);
    }

    @FXML
    private void cambiaPassword() {
        mostraMessaggio("Cambia Password", 
                       """
                       La funzionalità di cambio password sarà disponibile a breve!
                       
                       Potrai modificare la tua password in modo sicuro.""",
                       Alert.AlertType.INFORMATION);
    }

    private void mostraMessaggio(String titolo, String messaggio, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
