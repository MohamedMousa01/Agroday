package controllers.grafico.gui;

import controllers.applicativo.AnnuncioController;
import engclasses.beans.AnnuncioBean;
import engclasses.pattern.ViewFactory.ViewManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import misc.Session;
import misc.ViewType;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller per la view di visualizzazione annunci
 */
public class VisualizzaAnnunciGUIController {

    @FXML private TextField txtRicerca;
    @FXML private Button btnCerca;
    @FXML private Button btnIndietro;
    @FXML private Label lblNumAnnunci;
    @FXML private Button btnAggiorna;
    @FXML private VBox vboxAnnunci;

    private AnnuncioController annuncioController;
    private List<AnnuncioBean> tuttiAnnunci;
    private DateTimeFormatter dateFormatter;

    @FXML
    public void initialize() {
        annuncioController = new AnnuncioController();
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        caricaAnnunci();
    }

    private void caricaAnnunci() {
        try {
            tuttiAnnunci = annuncioController.getAnnunci();
            mostraAnnunci(tuttiAnnunci);
            aggiornaContatore(tuttiAnnunci.size());
        } catch (Exception e) {
            mostraErrore("Errore nel caricamento degli annunci: " + e.getMessage());
            tuttiAnnunci = List.of();
            aggiornaContatore(0);
        }
    }

    private void mostraAnnunci(List<AnnuncioBean> annunci) {
        vboxAnnunci.getChildren().clear();

        if (annunci.isEmpty()) {
            Label lblVuoto = new Label("📭 Nessun annuncio disponibile");
            lblVuoto.setStyle("-fx-font-size: 18px; -fx-text-fill: #666; -fx-padding: 50;");
            vboxAnnunci.getChildren().add(lblVuoto);
            vboxAnnunci.setAlignment(Pos.CENTER);
            return;
        }

        vboxAnnunci.setAlignment(Pos.TOP_CENTER);

        for (AnnuncioBean annuncio : annunci) {
            VBox cardAnnuncio = creaCardAnnuncio(annuncio);
            vboxAnnunci.getChildren().add(cardAnnuncio);
        }
    }

    private VBox creaCardAnnuncio(AnnuncioBean annuncio) {
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 25; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        card.setMaxWidth(900);

        // Header con titolo e autore
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitolo = new Label(annuncio.getTitolo());
        lblTitolo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label lblAutore = new Label("👤 " + annuncio.getAutore());
        lblAutore.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-font-style: italic;");
        
        header.getChildren().addAll(lblTitolo, spacer, lblAutore);

        // Descrizione
        Label lblDescrizione = new Label(annuncio.getDescrizione());
        lblDescrizione.setWrapText(true);
        lblDescrizione.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        // Info Grid
        HBox infoBox = new HBox(30);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-padding: 15;");
        
        VBox infoQuantita = new VBox(5);
        infoQuantita.setAlignment(Pos.CENTER);
        Label lblQuantitaIcon = new Label("📦");
        lblQuantitaIcon.setStyle("-fx-font-size: 24px;");
        Label lblQuantitaText = new Label(annuncio.getQuantita() + " kg");
        lblQuantitaText.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        Label lblQuantitaLabel = new Label("Quantità");
        lblQuantitaLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
        infoQuantita.getChildren().addAll(lblQuantitaIcon, lblQuantitaText, lblQuantitaLabel);
        
        VBox infoCitta = new VBox(5);
        infoCitta.setAlignment(Pos.CENTER);
        Label lblCittaIcon = new Label("📍");
        lblCittaIcon.setStyle("-fx-font-size: 24px;");
        Label lblCittaText = new Label(annuncio.getCitta());
        lblCittaText.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        Label lblCittaLabel = new Label("Città");
        lblCittaLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
        infoCitta.getChildren().addAll(lblCittaIcon, lblCittaText, lblCittaLabel);
        
        if (annuncio.getDataScadenza() != null) {
            VBox infoScadenza = new VBox(5);
            infoScadenza.setAlignment(Pos.CENTER);
            Label lblScadenzaIcon = new Label("📅");
            lblScadenzaIcon.setStyle("-fx-font-size: 24px;");
            Label lblScadenzaText = new Label(annuncio.getDataScadenza().format(dateFormatter));
            lblScadenzaText.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
            Label lblScadenzaLabel = new Label("Scadenza");
            lblScadenzaLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
            infoScadenza.getChildren().addAll(lblScadenzaIcon, lblScadenzaText, lblScadenzaLabel);
            infoBox.getChildren().add(infoScadenza);
        }
        
        infoBox.getChildren().addAll(infoQuantita, infoCitta);

        // Action Buttons
        HBox actionsBox = new HBox(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnContatta = new Button("📧 Contatta");
        btnContatta.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-font-weight: bold; " +
                            "-fx-background-radius: 15; -fx-padding: 8 20;");
        btnContatta.setOnAction(e -> contattaVenditore(annuncio));
        
        actionsBox.getChildren().add(btnContatta);

        // Separator
        Separator separator = new Separator();

        card.getChildren().addAll(header, lblDescrizione, separator, infoBox, actionsBox);
        
        return card;
    }

    @FXML
    private void cercaAnnunci() {
        String termineRicerca = txtRicerca.getText().trim().toLowerCase();
        
        if (termineRicerca.isEmpty()) {
            mostraAnnunci(tuttiAnnunci);
            aggiornaContatore(tuttiAnnunci.size());
            return;
        }

        List<AnnuncioBean> risultati = tuttiAnnunci.stream()
                .filter(a -> a.getTitolo().toLowerCase().contains(termineRicerca) ||
                            a.getDescrizione().toLowerCase().contains(termineRicerca) ||
                            a.getCitta().toLowerCase().contains(termineRicerca) ||
                            a.getAutore().toLowerCase().contains(termineRicerca))
                .collect(Collectors.toList());

        mostraAnnunci(risultati);
        aggiornaContatore(risultati.size());
    }

    @FXML
    private void aggiornaLista() {
        txtRicerca.clear();
        caricaAnnunci();
        mostraInfo("Lista annunci aggiornata!");
    }

    @FXML
    private void tornaIndietro() {
        // Torna alla schermata principale
        ViewManager.goTo(ViewType.MAIN);
    }

    private void contattaVenditore(AnnuncioBean annuncio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contatta Venditore");
        alert.setHeaderText("Informazioni di Contatto");
        alert.setContentText("Per contattare il venditore " + annuncio.getAutore() + 
                           " per l'annuncio \"" + annuncio.getTitolo() + "\":\n\n" +
                           "Funzionalità di messaggistica in arrivo!");
        alert.showAndWait();
    }

    private void aggiornaContatore(int numero) {
        if (numero == 0) {
            lblNumAnnunci.setText("Nessun annuncio trovato");
        } else if (numero == 1) {
            lblNumAnnunci.setText("1 annuncio disponibile");
        } else {
            lblNumAnnunci.setText(numero + " annunci disponibili");
        }
    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraInfo(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.show();
    }
}
