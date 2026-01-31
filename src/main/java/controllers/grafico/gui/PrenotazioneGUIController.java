package controllers.grafico.gui;

import controllers.applicativo.AppuntamentoController;
import engclasses.beans.AppuntamentoBean;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import misc.Session;
import misc.TipoConsulenza;
import model.Utente;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controller grafico per la prenotazione e modifica di appuntamenti.
 */
public class PrenotazioneGUIController {

    @FXML private Label titoloLabel;
    @FXML private ComboBox<String> consulenteCombo;
    @FXML private ComboBox<TipoConsulenza> tipoConsulenzaCombo;
    @FXML private DatePicker dataPicker;
    @FXML private ComboBox<String> oraInizioCombo;
    @FXML private ComboBox<String> minutoInizioCombo;
    @FXML private ComboBox<String> oraFineCombo;
    @FXML private ComboBox<String> minutoFineCombo;
    @FXML private TextField luogoField;
    @FXML private TextArea noteArea;
    @FXML private Label infoTipoLabel;
    @FXML private Label messageLabel;
    @FXML private Button annullaBtn;
    @FXML private Button confermaBtn;

    private Session session;
    private AppuntamentoController appuntamentoController;
    private AppuntamentoBean appuntamentoDaModificare;
    private boolean isModifica = false;

    // Simulazione lista consulenti (in produzione verrebbe dal DB)
    private static final String[] CONSULENTI_DEMO = {
            "cons001:Mario Rossi",
            "cons002:Giulia Bianchi",
            "cons003:Luca Verdi"
    };

    @FXML
    private void initialize() {
        session = Session.getInstance();
        appuntamentoController = new AppuntamentoController(session);

        // Popola i combo box
        popolaComboConsulenti();
        popolaComboTipoConsulenza();
        popolaComboOrari();

        // Imposta data minima (domani)
        dataPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now().plusDays(1)));
            }
        });

        // Listener per mostrare info sul tipo di consulenza
        tipoConsulenzaCombo.setOnAction(e -> aggiornaInfoTipo());

        // Listener per aggiornare automaticamente l'ora di fine
        oraInizioCombo.setOnAction(e -> suggerisciOraFine());
        minutoInizioCombo.setOnAction(e -> suggerisciOraFine());
        tipoConsulenzaCombo.setOnAction(e -> {
            aggiornaInfoTipo();
            suggerisciOraFine();
            aggiornaVisibilitaLuogo();
        });
    }

    private void popolaComboConsulenti() {
        consulenteCombo.getItems().clear();
        for (String cons : CONSULENTI_DEMO) {
            String[] parts = cons.split(":");
            consulenteCombo.getItems().add(parts[1]); // Mostra solo il nome
        }
    }

    private void popolaComboTipoConsulenza() {
        tipoConsulenzaCombo.getItems().clear();
        tipoConsulenzaCombo.getItems().addAll(TipoConsulenza.values());
    }

    private void popolaComboOrari() {
        // Ore (8-19)
        for (int h = 8; h <= 19; h++) {
            String ora = String.format("%02d", h);
            oraInizioCombo.getItems().add(ora);
            oraFineCombo.getItems().add(ora);
        }

        // Minuti (0, 15, 30, 45)
        for (int m = 0; m < 60; m += 15) {
            String min = String.format("%02d", m);
            minutoInizioCombo.getItems().add(min);
            minutoFineCombo.getItems().add(min);
        }
    }

    private void aggiornaInfoTipo() {
        TipoConsulenza tipo = tipoConsulenzaCombo.getValue();
        if (tipo != null) {
            infoTipoLabel.setText(tipo.getDescrizione());
        } else {
            infoTipoLabel.setText("");
        }
    }

    private void suggerisciOraFine() {
        if (oraInizioCombo.getValue() == null || minutoInizioCombo.getValue() == null) {
            return;
        }

        try {
            int oraInizio = Integer.parseInt(oraInizioCombo.getValue());
            int minInizio = Integer.parseInt(minutoInizioCombo.getValue());
            
            // Ottiene la durata predefinita dal controller applicativo
            int durataMinuti = appuntamentoController.getDurataPredefinitaMinuti(tipoConsulenzaCombo.getValue());

            LocalTime inizio = LocalTime.of(oraInizio, minInizio);
            LocalTime fine = inizio.plusMinutes(durataMinuti);

            oraFineCombo.setValue(String.format("%02d", fine.getHour()));
            minutoFineCombo.setValue(String.format("%02d", fine.getMinute()));
        } catch (Exception e) {
            // Ignora errori nel suggerimento
        }
    }

    private void aggiornaVisibilitaLuogo() {
        TipoConsulenza tipo = tipoConsulenzaCombo.getValue();
        if (tipo == TipoConsulenza.ONLINE) {
            luogoField.setPromptText("Link videochiamata (opzionale, verrà generato)");
        } else if (tipo == TipoConsulenza.IN_UFFICIO) {
            luogoField.setPromptText("Indirizzo ufficio");
        } else if (tipo == TipoConsulenza.SUL_CAMPO) {
            luogoField.setPromptText("Indirizzo terreno/azienda");
        }
    }

    /**
     * Imposta un appuntamento da modificare.
     */
    public void setAppuntamentoDaModificare(AppuntamentoBean appuntamento) {
        this.appuntamentoDaModificare = appuntamento;
        this.isModifica = true;
        
        titoloLabel.setText("Modifica Appuntamento");
        confermaBtn.setText("Salva Modifiche");

        // Popola i campi con i dati esistenti
        // Trova l'indice del consulente
        for (int i = 0; i < CONSULENTI_DEMO.length; i++) {
            if (CONSULENTI_DEMO[i].startsWith(appuntamento.getIdConsulente())) {
                consulenteCombo.getSelectionModel().select(i);
                break;
            }
        }

        tipoConsulenzaCombo.setValue(appuntamento.getTipoConsulenza());
        dataPicker.setValue(appuntamento.getData());
        
        if (appuntamento.getOraInizio() != null) {
            oraInizioCombo.setValue(String.format("%02d", appuntamento.getOraInizio().getHour()));
            minutoInizioCombo.setValue(String.format("%02d", appuntamento.getOraInizio().getMinute()));
        }
        if (appuntamento.getOraFine() != null) {
            oraFineCombo.setValue(String.format("%02d", appuntamento.getOraFine().getHour()));
            minutoFineCombo.setValue(String.format("%02d", appuntamento.getOraFine().getMinute()));
        }
        
        luogoField.setText(appuntamento.getLuogo());
        noteArea.setText(appuntamento.getNote());

        aggiornaInfoTipo();
        aggiornaVisibilitaLuogo();
    }

    @FXML
    private void onConferma() {
        // Validazione base
        if (!validaCampi()) {
            return;
        }

        try {
            AppuntamentoBean bean = creaBean();

            if (isModifica) {
                bean.setIdAppuntamento(appuntamentoDaModificare.getIdAppuntamento());
                appuntamentoController.modificaAppuntamento(bean);
                mostraMessaggio("Appuntamento modificato con successo!", false);
            } else {
                appuntamentoController.prenotaAppuntamento(bean);
                mostraMessaggio("Appuntamento prenotato con successo!", false);
            }

            // Chiudi la finestra dopo un breve delay
            chiudiFinestra();

        } catch (IllegalArgumentException e) {
            mostraMessaggio(e.getMessage(), true);
        } catch (IllegalStateException e) {
            mostraMessaggio(e.getMessage(), true);
        } catch (Exception e) {
            mostraMessaggio("Errore: " + e.getMessage(), true);
        }
    }

    private boolean validaCampi() {
        // Delega la validazione al controller applicativo
        String errori = appuntamentoController.validaCampiPrenotazione(
                consulenteCombo.getValue() != null,
                tipoConsulenzaCombo.getValue(),
                dataPicker.getValue() != null,
                oraInizioCombo.getValue() != null && minutoInizioCombo.getValue() != null,
                oraFineCombo.getValue() != null && minutoFineCombo.getValue() != null,
                luogoField.getText()
        );

        if (!errori.isEmpty()) {
            mostraMessaggio(errori, true);
            return false;
        }
        return true;
    }

    private AppuntamentoBean creaBean() {
        AppuntamentoBean bean = new AppuntamentoBean();

        // ID Cliente (utente loggato)
        Utente utente = session.getUtenteLoggato();
        if (utente != null) {
            bean.setIdCliente(utente.getIdUtente());
        } else {
            // Per demo, usa un ID fittizio
            bean.setIdCliente("demo_client_001");
        }

        // ID Consulente
        int selectedIndex = consulenteCombo.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < CONSULENTI_DEMO.length) {
            String[] parts = CONSULENTI_DEMO[selectedIndex].split(":");
            bean.setIdConsulente(parts[0]);
        }

        bean.setTipoConsulenza(tipoConsulenzaCombo.getValue());
        bean.setData(dataPicker.getValue());

        // Ora inizio
        int oraInizio = Integer.parseInt(oraInizioCombo.getValue());
        int minInizio = Integer.parseInt(minutoInizioCombo.getValue());
        bean.setOraInizio(LocalTime.of(oraInizio, minInizio));

        // Ora fine
        int oraFine = Integer.parseInt(oraFineCombo.getValue());
        int minFine = Integer.parseInt(minutoFineCombo.getValue());
        bean.setOraFine(LocalTime.of(oraFine, minFine));

        bean.setLuogo(luogoField.getText() != null ? luogoField.getText().trim() : "");
        bean.setNote(noteArea.getText() != null ? noteArea.getText().trim() : "");

        return bean;
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
