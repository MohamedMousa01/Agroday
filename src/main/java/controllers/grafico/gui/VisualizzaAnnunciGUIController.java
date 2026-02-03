package controllers.grafico.gui;

import controllers.applicativo.AnnuncioController;
import engclasses.beans.AnnuncioBean;
import engclasses.pattern.ViewFactory.ViewManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import misc.MessageConstants;
import misc.Session;
import misc.CSSConstants;
import misc.ViewType;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller per la view di visualizzazione annunci
 */
public class VisualizzaAnnunciGUIController {

    private static final String SUCCESS_MESSAGE_TEMPLATE = """
        ✅ Proposta inviata e salvata con successo!
        
        📦 Prodotto: %s
        📏 Quantità totale: %d kg
        💰 Prezzo proposto: %.2f €/kg
        💵 Totale: %.2f €
        
        L'agricoltore riceverà la tua proposta.
        Puoi vedere le tue offerte in "Le Mie Offerte".
        """;

    @FXML private TextField txtRicerca;
    @FXML private Button btnCerca;
    @FXML private Button btnIndietro;
    @FXML private Label lblNumAnnunci;
    @FXML private Button btnAggiorna;
    @FXML private VBox vboxAnnunci;

    private AnnuncioController annuncioController;
    private List<AnnuncioBean> tuttiAnnunci;
    private DateTimeFormatter dateFormatter;
    private boolean soloMieiAnnunci = false; // Flag per filtrare
    private boolean mostraPartecipazioni = false; // Flag per mostrare partecipazioni

    /**
     * Imposta se mostrare solo gli annunci dell'utente loggato o tutti
     * Deve essere chiamato PRIMA di initialize()
     */
    public void setMostraSoloMieiAnnunci(boolean soloMiei) {
        this.soloMieiAnnunci = soloMiei;
    }

    /**
     * Imposta se mostrare gli annunci a cui l'utente ha partecipato
     * Deve essere chiamato PRIMA di initialize()
     */
    public void setMostraPartecipazioni(boolean mostraPartecipazioni) {
        this.mostraPartecipazioni = mostraPartecipazioni;
    }

    @FXML
    public void initialize() {
        annuncioController = new AnnuncioController();
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        caricaAnnunci();
    }

    private void caricaAnnunci() {
        try {
            String username = Session.getInstance().getUtenteLoggato().getUsername();
            
            // Scegli quale lista caricare
            if (mostraPartecipazioni) {
                // Carica solo gli annunci a cui l'utente ha partecipato
                tuttiAnnunci = annuncioController.getAnnunciPartecipazioniUtente(username);
            } else {
                // Carica tutti gli annunci
                tuttiAnnunci = annuncioController.getAnnunci();
                
                // Se richiesto, filtra solo gli annunci dell'utente loggato
                if (soloMieiAnnunci) {
                    tuttiAnnunci = tuttiAnnunci.stream()
                        .filter(a -> a.getAutore().equals(username))
                        .toList();
                }
            }
            
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
        lblQuantitaIcon.setStyle(CSSConstants.FONT_SIZE_24);
        Label lblQuantitaText = new Label(annuncio.getQuantita() + " kg");
        lblQuantitaText.setStyle(CSSConstants.BOLD_TEXT_STYLE);
        Label lblQuantitaLabel = new Label("Richiesta autore");
        lblQuantitaLabel.setStyle(CSSConstants.SUBTITLE_STYLE);
        infoQuantita.getChildren().addAll(lblQuantitaIcon, lblQuantitaText, lblQuantitaLabel);
        
        // Quantità totale (autore + partecipanti)
        VBox infoTotale = new VBox(5);
        infoTotale.setAlignment(Pos.CENTER);
        Label lblTotaleIcon = new Label("🎯");
        lblTotaleIcon.setStyle(CSSConstants.FONT_SIZE_24);
        Label lblTotaleText = new Label(annuncio.getQuantitaTotale() + " kg");
        lblTotaleText.setStyle("-fx-font-weight: bold; -fx-text-fill: #43e97b;");
        Label lblTotaleLabel = new Label("Totale gruppo");
        lblTotaleLabel.setStyle(CSSConstants.SUBTITLE_STYLE);
        infoTotale.getChildren().addAll(lblTotaleIcon, lblTotaleText, lblTotaleLabel);
        
        // Numero partecipanti
        VBox infoPartecipanti = new VBox(5);
        infoPartecipanti.setAlignment(Pos.CENTER);
        Label lblPartIcon = new Label("👥");
        lblPartIcon.setStyle(CSSConstants.FONT_SIZE_24);
        Label lblPartText = new Label(String.valueOf(annuncio.getNumeroPartecipanti()));
        lblPartText.setStyle(CSSConstants.BOLD_TEXT_STYLE);
        Label lblPartLabel = new Label("Partecipanti");
        lblPartLabel.setStyle(CSSConstants.SUBTITLE_STYLE);
        infoPartecipanti.getChildren().addAll(lblPartIcon, lblPartText, lblPartLabel);
        
        VBox infoCitta = new VBox(5);
        infoCitta.setAlignment(Pos.CENTER);
        Label lblCittaIcon = new Label("📍");
        lblCittaIcon.setStyle(CSSConstants.FONT_SIZE_24);
        Label lblCittaText = new Label(annuncio.getCitta());
        lblCittaText.setStyle(CSSConstants.BOLD_TEXT_STYLE);
        Label lblCittaLabel = new Label("Città");
        lblCittaLabel.setStyle(CSSConstants.SUBTITLE_STYLE);
        infoCitta.getChildren().addAll(lblCittaIcon, lblCittaText, lblCittaLabel);
        
        VBox infoStato = new VBox(5);
        infoStato.setAlignment(Pos.CENTER);
        String statoIcon = annuncio.isAttivo() ? "✅" : "⏰";
        String statoColor = annuncio.isAttivo() ? "#43e97b" : "#e74c3c";
        Label lblStatoIcon = new Label(statoIcon);
        lblStatoIcon.setStyle(CSSConstants.FONT_SIZE_24);
        Label lblStatoText = new Label(annuncio.getStato());
        lblStatoText.setStyle("-fx-font-weight: bold; -fx-text-fill: " + statoColor + ";");
        Label lblStatoLabel = new Label("Stato");
        lblStatoLabel.setStyle(CSSConstants.SUBTITLE_STYLE);
        infoStato.getChildren().addAll(lblStatoIcon, lblStatoText, lblStatoLabel);
        
        if (annuncio.getDataScadenza() != null) {
            VBox infoScadenza = new VBox(5);
            infoScadenza.setAlignment(Pos.CENTER);
            Label lblScadenzaIcon = new Label("📅");
            lblScadenzaIcon.setStyle(CSSConstants.FONT_SIZE_24);
            Label lblScadenzaText = new Label(annuncio.getDataScadenza().format(dateFormatter));
            lblScadenzaText.setStyle(CSSConstants.BOLD_TEXT_STYLE);
            Label lblScadenzaLabel = new Label("Scadenza");
            lblScadenzaLabel.setStyle(CSSConstants.SUBTITLE_STYLE);
            infoScadenza.getChildren().addAll(lblScadenzaIcon, lblScadenzaText, lblScadenzaLabel);
            infoBox.getChildren().add(infoScadenza);
        }
        
        infoBox.getChildren().addAll(infoQuantita, infoTotale, infoPartecipanti, infoCitta, infoStato);

        // Action Buttons
        HBox actionsBox = new HBox(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);
        
        String usernameCorrente = Session.getInstance().getUtenteLoggato().getUsername();
        model.Utente utenteLoggato = Session.getInstance().getUtenteLoggato();
        boolean isVenditore = utenteLoggato instanceof model.Venditore;
        boolean isAutore = annuncio.getAutore().equals(usernameCorrente);
        
        // Bottoni diversi in base al tipo di utente
        if (isVenditore) {
            // VENDITORE: può proporre un prezzo se l'annuncio è scaduto
            if (annuncio.isScaduto()) {
                Button btnProponiPrezzo = new Button("💰 Proponi Prezzo");
                btnProponiPrezzo.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; " +
                                         CSSConstants.ROUNDED_BUTTON_STYLE);
                btnProponiPrezzo.setOnAction(e -> proponiPrezzo(annuncio));
                actionsBox.getChildren().add(btnProponiPrezzo);
            } else {
                Label lblAttivo = new Label("⏳ In corso");
                lblAttivo.setStyle("-fx-text-fill: #43e97b; -fx-font-weight: bold; -fx-font-size: 14px;");
                actionsBox.getChildren().add(lblAttivo);
            }
        } else {
            // AGRICOLTORE
            if (isAutore && soloMieiAnnunci) {
                // L'utente è l'autore e sta visualizzando "I Miei Annunci" -> può eliminare
                Button btnElimina = new Button("🗑️ Elimina");
                btnElimina.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                                   CSSConstants.ROUNDED_BUTTON_STYLE);
                btnElimina.setOnAction(e -> eliminaAnnuncio(annuncio));
                actionsBox.getChildren().add(btnElimina);
            } else if (!isAutore && annuncio.isAttivo()) {
                // Può partecipare se l'annuncio è attivo e non è l'autore
                Button btnPartecipa = new Button("🤝 Partecipa");
                btnPartecipa.setStyle("-fx-background-color: #6a11cb; -fx-text-fill: white; -fx-font-weight: bold; " +
                                    CSSConstants.ROUNDED_BUTTON_STYLE);
                btnPartecipa.setOnAction(e -> partecipaAnnuncio(annuncio));
                actionsBox.getChildren().add(btnPartecipa);
            } else if (!isAutore && annuncio.isScaduto()) {
                Label lblScaduto = new Label("⏰ Scaduto");
                lblScaduto.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 14px;");
                actionsBox.getChildren().add(lblScaduto);
            }
        }
        
        Button btnDettagli = new Button("📋 Dettagli");
        btnDettagli.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-font-weight: bold; " +
                            CSSConstants.ROUNDED_BUTTON_STYLE);
        btnDettagli.setOnAction(e -> mostraDettagli(annuncio));
        
        actionsBox.getChildren().add(btnDettagli);

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
                .toList();

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

    private void partecipaAnnuncio(AnnuncioBean annuncio) {
        // Dialog per chiedere la quantità
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Partecipa all'Annuncio");
        dialog.setHeaderText("Partecipa a: " + annuncio.getTitolo());
        dialog.setContentText("Inserisci la quantità che vuoi richiedere (kg):");

        dialog.showAndWait().ifPresent(quantitaStr -> {
            try {
                int quantita = Integer.parseInt(quantitaStr);
                
                if (quantita <= 0) {
                    mostraErrore("La quantità deve essere maggiore di zero");
                    return;
                }

                // Chiama il controller per partecipare
                controllers.applicativo.PartecipazioneController partController = 
                    new controllers.applicativo.PartecipazioneController();
                
                String username = Session.getInstance().getUtenteLoggato().getUsername();
                boolean successo = partController.partecipa(annuncio.getIdAnnuncio(), username, quantita);

                if (successo) {
                    mostraInfo("✅ Partecipazione registrata con successo!\n" +
                              "Hai richiesto " + quantita + " kg.\n\n" +
                              "Ora la quantità totale del gruppo sarà aggiornata.");
                    aggiornaLista(); // Ricarica gli annunci
                } else {
                    mostraErrore("Errore nella partecipazione. Riprova.");
                }

            } catch (NumberFormatException e) {
                mostraErrore("Inserisci un numero valido");
            } catch (IllegalStateException | IllegalArgumentException e) {
                mostraErrore(e.getMessage());
            }
        });
    }

    private void proponiPrezzo(AnnuncioBean annuncio) {
        // Dialog per proporre un prezzo all'ingrosso
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Proponi Prezzo all'Ingrosso");
        dialog.setHeaderText("Annuncio: " + annuncio.getTitolo() + "\nQuantità totale: " + annuncio.getQuantitaTotale() + " kg");
        dialog.setContentText("Inserisci il tuo prezzo al kg (€):");

        dialog.showAndWait().ifPresent(prezzoStr -> {
            try {
                double prezzo = Double.parseDouble(prezzoStr);
                
                if (prezzo <= 0) {
                    mostraErrore("Il prezzo deve essere maggiore di zero");
                    return;
                }

                // Salva l'offerta nel database
                controllers.applicativo.OffertaController offertaController = 
                    new controllers.applicativo.OffertaController();
                
                String usernameVenditore = Session.getInstance().getUtenteLoggato().getUsername();
                boolean salvata = offertaController.creaOfferta(
                    annuncio.getIdAnnuncio(), 
                    usernameVenditore, 
                    prezzo, 
                    annuncio.getQuantitaTotale()
                );

                if (salvata) {
                    double totale = prezzo * annuncio.getQuantitaTotale();
                    String successMsg = String.format(SUCCESS_MESSAGE_TEMPLATE,
                        annuncio.getTitolo(),
                        annuncio.getQuantitaTotale(),
                        prezzo,
                        totale
                    );
                    mostraInfo(successMsg);
                } else {
                    mostraErrore("Errore nel salvataggio dell'offerta. Riprova.");
                }

            } catch (NumberFormatException e) {
                mostraErrore("Inserisci un numero valido");
            } catch (Exception e) {
                mostraErrore(MessageConstants.ERRORE_GENERICO + e.getMessage());
            }
        });
    }

    private void mostraDettagli(AnnuncioBean annuncio) {
        // Ottieni i partecipanti
        controllers.applicativo.PartecipazioneController partController = 
            new controllers.applicativo.PartecipazioneController();
        
        int numPartecipanti = partController.getNumeroPartecipanti(annuncio.getIdAnnuncio());
        int quantitaTotale = partController.getQuantitaTotale(annuncio.getIdAnnuncio());
        
        model.Utente utenteLoggato = Session.getInstance().getUtenteLoggato();
        boolean isVenditore = utenteLoggato instanceof model.Venditore;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dettagli Annuncio");
        alert.setHeaderText(annuncio.getTitolo());
        
        String stato = annuncio.isAttivo() ? "✅ ATTIVO" : "⏰ SCADUTO";
        
        String infoSpecifica;
        if (isVenditore) {
            infoSpecifica = annuncio.isScaduto() ? 
                "💰 Come venditore, puoi proporre un prezzo all'ingrosso!" : 
                "⏳ Annuncio ancora in corso. Aspetta la scadenza per fare un'offerta.";
        } else {
            infoSpecifica = annuncio.isAttivo() ? 
                "💡 Puoi partecipare a questo annuncio di gruppo!" : 
                "⚠️ Questo annuncio è scaduto. Non è possibile partecipare.";
        }
        
        String dettagli = String.format("""
            📋 Descrizione: %s
            
            👤 Autore: %s
            📍 Città: %s
            📅 Scadenza: %s
            %s Stato: %s
            
            📦 QUANTITÀ:
            • Richiesta autore: %d kg
            • Totale gruppo: %d kg
            • Partecipanti: %d
            
            %s
            """,
            annuncio.getDescrizione(),
            annuncio.getAutore(),
            annuncio.getCitta(),
            annuncio.getDataScadenza().format(dateFormatter),
            annuncio.isAttivo() ? "✅" : "⏰",
            stato,
            annuncio.getQuantita(),
            quantitaTotale,
            numPartecipanti,
            infoSpecifica
        );
        
        alert.setContentText(dettagli);
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

    private void eliminaAnnuncio(AnnuncioBean annuncio) {
        // Conferma eliminazione
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Eliminazione");
        alert.setHeaderText("Eliminare questo annuncio?");
        String confirmMsg =  String.format(
            "Titolo: %s%nCittà: %s%nQuantità: %d kg%n%nSei sicuro di voler eliminare questo annuncio?%nQuesta azione è irreversibile!",
            annuncio.getTitolo(), annuncio.getCitta(), annuncio.getQuantita()
        );
        alert.setContentText(confirmMsg);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean successo = annuncioController.eliminaAnnuncio(annuncio.getIdAnnuncio());
                
                if (successo) {
                    mostraInfo("✅ Annuncio eliminato con successo!");
                    aggiornaLista(); // Ricarica la lista
                } else {
                    mostraErrore("❌ Errore nell'eliminazione dell'annuncio. Riprova.");
                }
            }
        });
    }
}
