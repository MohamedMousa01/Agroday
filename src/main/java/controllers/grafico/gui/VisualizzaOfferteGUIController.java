package controllers.grafico.gui;

import controllers.applicativo.OffertaController;
import engclasses.pattern.viewfactory.ViewManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import misc.CSSConstants;
import misc.MessageConstants;
import misc.Session;
import misc.ViewType;
import model.Offerta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller per visualizzare le offerte ricevute dall'agricoltore sui suoi annunci.
 */
public class VisualizzaOfferteGUIController {

    private static final Logger logger = LoggerFactory.getLogger(VisualizzaOfferteGUIController.class);

    @FXML private VBox contenitoreOfferte;
    @FXML private Label titoloLabel;
    @FXML private Label subtitleLabel;
    @FXML private Button btnIndietro;

    private OffertaController offertaController;
    private String usernameAgricoltore;

    @FXML
    public void initialize() {
        offertaController = new OffertaController();
        usernameAgricoltore = Session.getInstance().getUtenteLoggato().getUsername();

        // DEBUG: Stampa username per verifica
        logger.debug("Username agricoltore loggato: '{}'", usernameAgricoltore);

        caricaOfferte();
    }

    private void caricaOfferte() {
        contenitoreOfferte.getChildren().clear();

        // Ottieni tutte le offerte ricevute
        List<Offerta> offerte = offertaController.getOfferteRicevute(usernameAgricoltore);

        // DEBUG: Stampa numero offerte trovate
        logger.debug("Numero offerte trovate per '{}': {}", usernameAgricoltore, offerte.size());
        for (Offerta o : offerte) {
            logger.debug("Offerta: {} - Venditore: {} - Prezzo: {}", o.getIdOfferta(), o.getUsernameVenditore(), o.getPrezzoAlKg());
        }

        if (offerte.isEmpty()) {
            mostraMessaggioVuoto();
            return;
        }

        // Raggruppa le offerte per annuncio
        Map<String, List<Offerta>> offertePerAnnuncio = offerte.stream()
                .collect(Collectors.groupingBy(Offerta::getIdAnnuncio));

        // Mostra le offerte raggruppate
        for (Map.Entry<String, List<Offerta>> entry : offertePerAnnuncio.entrySet()) {
            String idAnnuncio = entry.getKey();
            List<Offerta> offerteAnnuncio = entry.getValue();

            // Crea card per l'annuncio
            VBox cardAnnuncio = creaCardAnnuncio(idAnnuncio, offerteAnnuncio);
            contenitoreOfferte.getChildren().add(cardAnnuncio);
        }

        // Aggiorna subtitle con il numero di offerte
        long offertePending = offerte.stream().filter(Offerta::isPending).count();
        subtitleLabel.setText(String.format("Hai %d offerte in attesa su %d annunci", 
            offertePending, offertePerAnnuncio.size()));
    }

    private VBox creaCardAnnuncio(String idAnnuncio, List<Offerta> offerte) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                     "-fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);");
        card.setPadding(new Insets(15));

        // Header con info annuncio
        HBox header = creaHeaderAnnuncio(idAnnuncio);
        card.getChildren().add(header);

        // Separatore
        Separator sep = new Separator();
        sep.setPadding(new Insets(10, 0, 10, 0));
        card.getChildren().add(sep);

        // Lista offerte per questo annuncio
        for (Offerta offerta : offerte) {
            HBox rigaOfferta = creaRigaOfferta(offerta);
            card.getChildren().add(rigaOfferta);
        }

        return card;
    }

    private HBox creaHeaderAnnuncio(String idAnnuncio) {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        // Ottieni info annuncio (semplificato - potremmo caricare l'annuncio completo)
        Label lblTitolo = new Label("📦 Annuncio" + MessageConstants.LABEL_ID + idAnnuncio.substring(0, Math.min(8, idAnnuncio.length())));
        lblTitolo.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblTitolo.setStyle("-fx-text-fill: #333;");

        header.getChildren().add(lblTitolo);

        return header;
    }

    private HBox creaRigaOfferta(Offerta offerta) {
        HBox riga = new HBox(15);
        riga.setAlignment(Pos.CENTER_LEFT);
        riga.setPadding(new Insets(10));
        riga.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");

        // Info offerta
        VBox infoBox = new VBox(5);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label lblVenditore = new Label("👤 Venditore: " + offerta.getUsernameVenditore());
        lblVenditore.setStyle(CSSConstants.BOLD_TEXT_STYLE);

        Label lblPrezzo = new Label(String.format("💰 Prezzo: %.2f €/kg | Totale: %.2f €", 
            offerta.getPrezzoAlKg(), offerta.getPrezzoTotale()));
        lblPrezzo.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Label lblData = new Label("📅 " + offerta.getDataOfferta().format(formatter));
        lblData.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");

        infoBox.getChildren().addAll(lblVenditore, lblPrezzo, lblData);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Stato e pulsanti
        VBox azioniBox = new VBox(10);
        azioniBox.setAlignment(Pos.CENTER_RIGHT);

        Label lblStato = creaLabelStato(offerta);
        azioniBox.getChildren().add(lblStato);

        // Pulsanti solo se l'offerta è pending
        if (offerta.isPending()) {
            HBox pulsantiBox = new HBox(10);
            pulsantiBox.setAlignment(Pos.CENTER_RIGHT);

            Button btnAccetta = new Button("✅ Accetta");
            btnAccetta.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; " +
                               "-fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 8 20;");
            btnAccetta.setOnAction(e -> accettaOfferta(offerta));

            Button btnRifiuta = new Button("❌ Rifiuta");
            btnRifiuta.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                               "-fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 8 20;");
            btnRifiuta.setOnAction(e -> rifiutaOfferta(offerta));

            pulsantiBox.getChildren().addAll(btnAccetta, btnRifiuta);
            azioniBox.getChildren().add(pulsantiBox);
        }

        riga.getChildren().addAll(infoBox, spacer, azioniBox);

        return riga;
    }

    private Label creaLabelStato(Offerta offerta) {
        Label lblStato = new Label();
        lblStato.setStyle("-fx-padding: 5 15; -fx-background-radius: 15; -fx-font-weight: bold;");

        if (offerta.isPending()) {
            lblStato.setText("⏳ In Attesa");
            lblStato.setStyle(lblStato.getStyle() + "-fx-background-color: #f39c12; -fx-text-fill: white;");
        } else if (offerta.isAccettata()) {
            lblStato.setText("✅ Accettata");
            lblStato.setStyle(lblStato.getStyle() + "-fx-background-color: #2ecc71; -fx-text-fill: white;");
        } else if (offerta.isRifiutata()) {
            lblStato.setText("❌ Rifiutata");
            lblStato.setStyle(lblStato.getStyle() + "-fx-background-color: #e74c3c; -fx-text-fill: white;");
        }

        return lblStato;
    }

    private void accettaOfferta(Offerta offerta) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Accettazione");
        alert.setHeaderText("Accettare questa offerta?");
        alert.setContentText(String.format(
            "Venditore: %s%nPrezzo: %.2f €/kg%nTotale: %.2f €%n%nVuoi accettare questa offerta?",
            offerta.getUsernameVenditore(), offerta.getPrezzoAlKg(), offerta.getPrezzoTotale()));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean successo = offertaController.accettaOfferta(offerta.getIdOfferta());
                if (successo) {
                    mostraMessaggio("Successo", "Offerta accettata con successo!", Alert.AlertType.INFORMATION);
                    caricaOfferte(); // Ricarica la lista
                } else {
                    mostraMessaggio("Errore", "Errore nell'accettazione dell'offerta", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void rifiutaOfferta(Offerta offerta) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Rifiuto");
        alert.setHeaderText("Rifiutare questa offerta?");
        alert.setContentText(String.format(
            "Venditore: %s%nPrezzo: %.2f €/kg%n%nSei sicuro di voler rifiutare questa offerta?",
            offerta.getUsernameVenditore(), offerta.getPrezzoAlKg()));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean successo = offertaController.rifiutaOfferta(offerta.getIdOfferta());
                if (successo) {
                    mostraMessaggio("Successo", "Offerta rifiutata", Alert.AlertType.INFORMATION);
                    caricaOfferte(); // Ricarica la lista
                } else {
                    mostraMessaggio("Errore", "Errore nel rifiuto dell'offerta", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void mostraMessaggioVuoto() {
        VBox messaggioVuoto = new VBox(20);
        messaggioVuoto.setAlignment(Pos.CENTER);
        messaggioVuoto.setPadding(new Insets(50));
        messaggioVuoto.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);");

        Label icona = new Label("📭");
        icona.setStyle("-fx-font-size: 64px;");

        Label messaggio = new Label("Non hai ancora ricevuto offerte");
        messaggio.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");

        Label info = new Label("Quando i venditori proporranno prezzi per i tuoi annunci scaduti,\nle offerte appariranno qui.");
        info.setStyle("-fx-text-fill: #999; -fx-text-alignment: center;");
        info.setWrapText(true);

        messaggioVuoto.getChildren().addAll(icona, messaggio, info);
        contenitoreOfferte.getChildren().add(messaggioVuoto);
    }

    @FXML
    private void tornaIndietro() {
        ViewManager.goTo(ViewType.MAIN);
    }

    private void mostraMessaggio(String titolo, String messaggio, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
