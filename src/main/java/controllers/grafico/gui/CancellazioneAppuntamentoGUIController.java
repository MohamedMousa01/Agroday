package controllers.grafico.gui;

import controllers.applicativo.AppuntamentoController;
import engclasses.beans.AppuntamentoBean;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import misc.Session;

/**
 * Controller grafico per la cancellazione di un appuntamento.
 */
public class CancellazioneAppuntamentoGUIController {

    @FXML private Label infoAppuntamentoLabel;
    @FXML private TextArea motivoArea;
    @FXML private Label messageLabel;
    @FXML private Button annullaBtn;
    @FXML private Button confermaBtn;

    private Session session;
    private AppuntamentoController appuntamentoController;
    private AppuntamentoBean appuntamento;

    @FXML
    private void initialize() {
        session = Session.getInstance();
        appuntamentoController = new AppuntamentoController(session);
    }

    /**
     * Imposta l'appuntamento da cancellare.
     */
    public void setAppuntamento(AppuntamentoBean appuntamento) {
        this.appuntamento = appuntamento;
        popolaInfo();
    }

    private void popolaInfo() {
        if (appuntamento == null) {
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("📅 Data: ").append(appuntamento.getDataFormattata()).append("\n");
        info.append("🕐 Orario: ").append(appuntamento.getIntervalloOrario()).append("\n");
        info.append("📋 Tipo: ").append(appuntamento.getTipoConsulenzaDescrizione()).append("\n");
        info.append("📍 Luogo: ").append(appuntamento.getLuogo() != null ? appuntamento.getLuogo() : "N/A");

        infoAppuntamentoLabel.setText(info.toString());
    }

    @FXML
    private void onConfermaCancellazione() {
        if (appuntamento == null) {
            mostraMessaggio("Nessun appuntamento selezionato.", true);
            return;
        }

        if (!appuntamento.isCancellabile()) {
            mostraMessaggio("L'appuntamento non può più essere cancellato.", true);
            return;
        }

        String motivo = motivoArea.getText() != null ? motivoArea.getText().trim() : "";
        if (motivo.isEmpty()) {
            motivo = "Nessun motivo specificato";
        }

        try {
            // Delega al controller applicativo la logica di cancellazione
            // Il controller determina automaticamente se è cliente o consulente
            boolean successo = appuntamentoController.cancellaAppuntamento(
                    appuntamento.getIdAppuntamento(), motivo);

            if (successo) {
                mostraMessaggio("Appuntamento cancellato con successo!", false);
                
                // Chiudi la finestra dopo un breve messaggio
                chiudiFinestra();
            } else {
                mostraMessaggio("Errore nella cancellazione dell'appuntamento.", true);
            }
        } catch (Exception e) {
            mostraMessaggio("Errore: " + e.getMessage(), true);
        }
    }

    @FXML
    private void onAnnulla() {
        chiudiFinestra();
    }

    private void chiudiFinestra() {
        Stage stage = (Stage) annullaBtn.getScene().getWindow();
        stage.close();
    }

    private void mostraMessaggio(String messaggio, boolean isErrore) {
        messageLabel.setText(messaggio);
        if (isErrore) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }
    }
}
