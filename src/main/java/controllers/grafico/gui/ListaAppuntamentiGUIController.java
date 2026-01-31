package controllers.grafico.gui;

import controllers.applicativo.AppuntamentoController;
import engclasses.beans.AppuntamentoBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import misc.Session;
import misc.StatoAppuntamento;

import java.io.IOException;
import java.util.List;

/**
 * Controller grafico per la visualizzazione della lista appuntamenti.
 */
public class ListaAppuntamentiGUIController {

    @FXML private TableView<AppuntamentoBean> appuntamentiTable;
    @FXML private TableColumn<AppuntamentoBean, String> dataColumn;
    @FXML private TableColumn<AppuntamentoBean, String> oraColumn;
    @FXML private TableColumn<AppuntamentoBean, String> tipoColumn;
    @FXML private TableColumn<AppuntamentoBean, String> consulenteColumn;
    @FXML private TableColumn<AppuntamentoBean, String> luogoColumn;
    @FXML private TableColumn<AppuntamentoBean, String> statoColumn;
    
    @FXML private ComboBox<String> filtroStatoCombo;
    @FXML private Button nuovoAppuntamentoBtn;
    @FXML private Button dettagliBtn;
    @FXML private Button modificaBtn;
    @FXML private Button cancellaBtn;
    @FXML private Button aggiornaBtn;
    @FXML private Button tornaIndietroBtn;
    @FXML private Label messageLabel;

    private Session session;
    private AppuntamentoController appuntamentoController;
    private ObservableList<AppuntamentoBean> appuntamentiList;

    @FXML
    private void initialize() {
        session = Session.getInstance();
        appuntamentoController = new AppuntamentoController(session);
        appuntamentiList = FXCollections.observableArrayList();

        // Configura le colonne della tabella
        configuraColonne();

        // Configura il filtro per stato
        configuraFiltroStato();

        // Carica i dati
        caricaAppuntamenti();

        // Abilita/disabilita bottoni in base alla selezione
        appuntamentiTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> aggiornaStatoBottoni(newSelection));

        // Disabilita i bottoni inizialmente
        aggiornaStatoBottoni(null);
    }

    private void configuraColonne() {
        dataColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getDataFormattata()));
        
        oraColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getIntervalloOrario()));
        
        tipoColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getTipoConsulenzaDescrizione()));
        
        consulenteColumn.setCellValueFactory(cellData -> {
            String nome = cellData.getValue().getNomeCompletoConsulente();
            return new SimpleStringProperty(nome.isEmpty() ? "Consulente" : nome);
        });
        
        luogoColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getLuogo()));
        
        statoColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getStatoDescrizione()));

        // Colora le righe in base allo stato
        appuntamentiTable.setRowFactory(tv -> new TableRow<AppuntamentoBean>() {
            @Override
            protected void updateItem(AppuntamentoBean item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    switch (item.getStato()) {
                        case CONFERMATO:
                            setStyle("-fx-background-color: #d4edda;");
                            break;
                        case CANCELLATO_CLIENTE:
                        case CANCELLATO_CONSULENTE:
                            setStyle("-fx-background-color: #f8d7da;");
                            break;
                        case COMPLETATO:
                            setStyle("-fx-background-color: #e2e3e5;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        appuntamentiTable.setItems(appuntamentiList);
    }

    private void configuraFiltroStato() {
        filtroStatoCombo.getItems().add("Tutti");
        for (StatoAppuntamento stato : StatoAppuntamento.values()) {
            filtroStatoCombo.getItems().add(stato.getDisplayName());
        }
        filtroStatoCombo.setValue("Tutti");

        filtroStatoCombo.setOnAction(e -> filtraAppuntamenti());
    }

    private void caricaAppuntamenti() {
        try {
            List<AppuntamentoBean> appuntamenti = appuntamentoController.getAppuntamentiUtenteCorrente();
            appuntamentiList.clear();
            appuntamentiList.addAll(appuntamenti);
            
            if (appuntamenti.isEmpty()) {
                mostraMessaggio("Nessun appuntamento trovato.", false);
            } else {
                messageLabel.setText("");
            }
        } catch (Exception e) {
            mostraMessaggio("Errore nel caricamento: " + e.getMessage(), true);
        }
    }

    private void filtraAppuntamenti() {
        String filtro = filtroStatoCombo.getValue();
        
        // Delega il filtraggio al controller applicativo
        StatoAppuntamento statoFiltro = null;
        if (!"Tutti".equals(filtro)) {
            // Trova lo stato corrispondente al nome visualizzato
            for (StatoAppuntamento stato : StatoAppuntamento.values()) {
                if (stato.getDisplayName().equals(filtro)) {
                    statoFiltro = stato;
                    break;
                }
            }
        }
        
        List<AppuntamentoBean> appuntamentiFiltrati = appuntamentoController.getAppuntamentiUtenteFiltrati(statoFiltro);
        appuntamentiList.clear();
        appuntamentiList.addAll(appuntamentiFiltrati);
    }

    private void aggiornaStatoBottoni(AppuntamentoBean selected) {
        boolean hasSelection = selected != null;
        boolean isModificabile = hasSelection && selected.isModificabile();

        dettagliBtn.setDisable(!hasSelection);
        modificaBtn.setDisable(!isModificabile);
        cancellaBtn.setDisable(!isModificabile);
    }

    @FXML
    private void onNuovoAppuntamento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrenotazioneView.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nuovo Appuntamento");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Aggiorna la lista dopo la chiusura
            caricaAppuntamenti();
        } catch (IOException e) {
            mostraMessaggio("Errore nell'apertura della finestra: " + e.getMessage(), true);
        }
    }

    @FXML
    private void onDettagli() {
        AppuntamentoBean selected = appuntamentiTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            mostraMessaggio("Seleziona un appuntamento.", true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DettaglioAppuntamentoView.fxml"));
            Parent root = loader.load();
            
            DettaglioAppuntamentoGUIController controller = loader.getController();
            controller.setAppuntamento(selected);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Dettaglio Appuntamento");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Aggiorna la lista dopo la chiusura
            caricaAppuntamenti();
        } catch (IOException e) {
            mostraMessaggio("Errore nell'apertura della finestra: " + e.getMessage(), true);
        }
    }

    @FXML
    private void onModifica() {
        AppuntamentoBean selected = appuntamentiTable.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.isModificabile()) {
            mostraMessaggio("Seleziona un appuntamento modificabile.", true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrenotazioneView.fxml"));
            Parent root = loader.load();
            
            PrenotazioneGUIController controller = loader.getController();
            controller.setAppuntamentoDaModificare(selected);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifica Appuntamento");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Aggiorna la lista dopo la chiusura
            caricaAppuntamenti();
        } catch (IOException e) {
            mostraMessaggio("Errore nell'apertura della finestra: " + e.getMessage(), true);
        }
    }

    @FXML
    private void onCancella() {
        AppuntamentoBean selected = appuntamentiTable.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.isCancellabile()) {
            mostraMessaggio("Seleziona un appuntamento cancellabile.", true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CancellazioneAppuntamentoView.fxml"));
            Parent root = loader.load();
            
            CancellazioneAppuntamentoGUIController controller = loader.getController();
            controller.setAppuntamento(selected);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Cancella Appuntamento");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Aggiorna la lista dopo la chiusura
            caricaAppuntamenti();
        } catch (IOException e) {
            mostraMessaggio("Errore nell'apertura della finestra: " + e.getMessage(), true);
        }
    }

    @FXML
    private void onAggiorna() {
        caricaAppuntamenti();
        mostraMessaggio("Lista aggiornata.", false);
    }

    @FXML
    private void onTornaIndietro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tornaIndietroBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Agroday - Menu Principale");
        } catch (IOException e) {
            mostraMessaggio("Errore nella navigazione: " + e.getMessage(), true);
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
