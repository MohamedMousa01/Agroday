package controllers.grafico.gui;

import controllers.applicativo.AppuntamentoController;
import engclasses.beans.AppuntamentoBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import misc.Session;
import misc.StatoAppuntamento;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

/**
 * Controller grafico per la visualizzazione dei dettagli di un appuntamento.
 */
public class DettaglioAppuntamentoGUIController {

    @FXML private Label statoLabel;
    @FXML private Label idLabel;
    @FXML private Label tipoLabel;
    @FXML private Label dataLabel;
    @FXML private Label orarioLabel;
    @FXML private Label durataLabel;
    @FXML private Label luogoLabel;
    @FXML private Label consulenteLabel;
    @FXML private Label clienteLabel;
    @FXML private TextArea noteArea;
    @FXML private VBox motivoCancellazioneBox;
    @FXML private Label motivoCancellazioneLabel;
    @FXML private VBox istruzioniBox;
    @FXML private Label istruzioniLabel;
    @FXML private HBox calendarioBox;
    @FXML private Button apriCalendarioBtn;
    @FXML private Label messageLabel;
    @FXML private Button modificaBtn;
    @FXML private Button cancellaBtn;
    @FXML private Button confermaBtn;
    @FXML private Button chiudiBtn;

    private Session session;
    private AppuntamentoController appuntamentoController;
    private AppuntamentoBean appuntamento;

    @FXML
    private void initialize() {
        session = Session.getInstance();
        appuntamentoController = new AppuntamentoController(session);
    }

    /**
     * Imposta l'appuntamento da visualizzare.
     */
    public void setAppuntamento(AppuntamentoBean appuntamento) {
        this.appuntamento = appuntamento;
        popolaCampi();
        aggiornaStatoBottoni();
    }

    private void popolaCampi() {
        if (appuntamento == null) {
            return;
        }

        // ID
        idLabel.setText(appuntamento.getIdAppuntamento());

        // Stato con colore
        StatoAppuntamento stato = appuntamento.getStato();
        statoLabel.setText(stato.getDisplayName());
        impostaColoreStato(stato);

        // Tipo
        tipoLabel.setText(appuntamento.getTipoConsulenzaDescrizione());

        // Data e orario
        dataLabel.setText(appuntamento.getDataFormattata());
        orarioLabel.setText(appuntamento.getIntervalloOrario());
        durataLabel.setText(appuntamento.getDurataMinuti() + " minuti");

        // Luogo
        luogoLabel.setText(appuntamento.getLuogo() != null ? appuntamento.getLuogo() : "Non specificato");

        // Consulente e Cliente
        String nomeConsulente = appuntamento.getNomeCompletoConsulente();
        consulenteLabel.setText(nomeConsulente.isEmpty() ? "ID: " + appuntamento.getIdConsulente() : nomeConsulente);
        
        String nomeCliente = appuntamento.getNomeCompletoCliente();
        clienteLabel.setText(nomeCliente.isEmpty() ? "ID: " + appuntamento.getIdCliente() : nomeCliente);

        // Note
        noteArea.setText(appuntamento.getNote() != null ? appuntamento.getNote() : "Nessuna nota");

        // Motivo cancellazione
        if (stato.isCancellato() && appuntamento.getMotivoCancellazione() != null) {
            motivoCancellazioneBox.setVisible(true);
            motivoCancellazioneBox.setManaged(true);
            motivoCancellazioneLabel.setText(appuntamento.getMotivoCancellazione());
        } else {
            motivoCancellazioneBox.setVisible(false);
            motivoCancellazioneBox.setManaged(false);
        }

        // Istruzioni (solo per appuntamenti attivi)
        if (stato.isAttivo()) {
            String istruzioni = appuntamentoController.preparaConsulenza(appuntamento.getIdAppuntamento());
            istruzioniLabel.setText(istruzioni);
            istruzioniBox.setVisible(true);
            istruzioniBox.setManaged(true);
        } else {
            istruzioniBox.setVisible(false);
            istruzioniBox.setManaged(false);
        }

        // Link Google Calendar
        if (appuntamento.getLinkCalendario() != null && !appuntamento.getLinkCalendario().isEmpty()) {
            calendarioBox.setVisible(true);
            calendarioBox.setManaged(true);
        } else {
            calendarioBox.setVisible(false);
            calendarioBox.setManaged(false);
        }
    }

    private void impostaColoreStato(StatoAppuntamento stato) {
        String style = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 20; ";
        
        switch (stato) {
            case PRENOTATO:
                style += "-fx-background-color: #fff3cd; -fx-text-fill: #856404;";
                break;
            case CONFERMATO:
                style += "-fx-background-color: #d4edda; -fx-text-fill: #155724;";
                break;
            case IN_CORSO:
                style += "-fx-background-color: #cce5ff; -fx-text-fill: #004085;";
                break;
            case COMPLETATO:
                style += "-fx-background-color: #e2e3e5; -fx-text-fill: #383d41;";
                break;
            case CANCELLATO_CLIENTE:
            case CANCELLATO_CONSULENTE:
                style += "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;";
                break;
            default:
                style += "-fx-background-color: #e9ecef; -fx-text-fill: #333;";
        }
        
        statoLabel.setStyle(style);
    }

    private void aggiornaStatoBottoni() {
        if (appuntamento == null) {
            modificaBtn.setDisable(true);
            cancellaBtn.setDisable(true);
            confermaBtn.setDisable(true);
            return;
        }

        boolean isModificabile = appuntamento.isModificabile();
        boolean isPrenotato = appuntamento.getStato() == StatoAppuntamento.PRENOTATO;

        modificaBtn.setDisable(!isModificabile);
        cancellaBtn.setDisable(!isModificabile);
        confermaBtn.setDisable(!isPrenotato);
        confermaBtn.setVisible(isPrenotato);
        confermaBtn.setManaged(isPrenotato);
    }

    @FXML
    private void onModifica() {
        if (appuntamento == null || !appuntamento.isModificabile()) {
            mostraMessaggio("L'appuntamento non è più modificabile.", true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrenotazioneView.fxml"));
            Parent root = loader.load();
            
            PrenotazioneGUIController controller = loader.getController();
            controller.setAppuntamentoDaModificare(appuntamento);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifica Appuntamento");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Ricarica i dati
            ricaricaAppuntamento();
        } catch (IOException e) {
            mostraMessaggio("Errore nell'apertura della finestra: " + e.getMessage(), true);
        }
    }

    @FXML
    private void onCancella() {
        if (appuntamento == null || !appuntamento.isCancellabile()) {
            mostraMessaggio("L'appuntamento non può essere cancellato.", true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CancellazioneAppuntamentoView.fxml"));
            Parent root = loader.load();
            
            CancellazioneAppuntamentoGUIController controller = loader.getController();
            controller.setAppuntamento(appuntamento);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Cancella Appuntamento");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Ricarica i dati
            ricaricaAppuntamento();
        } catch (IOException e) {
            mostraMessaggio("Errore nell'apertura della finestra: " + e.getMessage(), true);
        }
    }

    @FXML
    private void onConfermaAppuntamento() {
        if (appuntamento == null || appuntamento.getStato() != StatoAppuntamento.PRENOTATO) {
            mostraMessaggio("L'appuntamento non può essere confermato.", true);
            return;
        }

        try {
            boolean successo = appuntamentoController.confermaAppuntamento(appuntamento.getIdAppuntamento());
            
            if (successo) {
                mostraMessaggio("Appuntamento confermato con successo!", false);
                ricaricaAppuntamento();
            } else {
                mostraMessaggio("Errore nella conferma dell'appuntamento.", true);
            }
        } catch (Exception e) {
            mostraMessaggio("Errore: " + e.getMessage(), true);
        }
    }

    @FXML
    private void onApriCalendario() {
        String link = appuntamento.getLinkCalendario();
        if (link != null && !link.isEmpty()) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(link));
                } else {
                    mostraMessaggio("Link: " + link, false);
                }
            } catch (Exception e) {
                mostraMessaggio("Impossibile aprire il link: " + link, true);
            }
        }
    }

    @FXML
    private void onChiudi() {
        Stage stage = (Stage) chiudiBtn.getScene().getWindow();
        stage.close();
    }

    private void ricaricaAppuntamento() {
        if (appuntamento != null) {
            AppuntamentoBean aggiornato = appuntamentoController.getAppuntamento(appuntamento.getIdAppuntamento());
            if (aggiornato != null) {
                this.appuntamento = aggiornato;
                popolaCampi();
                aggiornaStatoBottoni();
            }
        }
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
