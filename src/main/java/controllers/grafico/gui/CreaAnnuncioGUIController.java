package controllers.grafico.gui;

import controllers.applicativo.AnnuncioController;
import engclasses.exceptions.AnnuncioNonValidoException;
import engclasses.pattern.viewfactory.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import misc.Session;
import misc.ViewType;


public class CreaAnnuncioGUIController {

    @FXML private TextField titoloField;
    @FXML private TextField autoreField;
    @FXML private DatePicker dataScadenzaPicker;
    @FXML private TextField nomeProdottoField;
    @FXML private TextField quantitaField;
    @FXML private TextField cittaField;
    @FXML private TextArea descrizioneField;

    private final AnnuncioController annuncioController = new AnnuncioController();

    @FXML
    public void initialize() {
        // Imposta automaticamente l'username dell'utente loggato come autore
        String username = Session.getInstance().getUtenteLoggato().getUsername();
        autoreField.setText(username);
        autoreField.setDisable(true); // Rende il campo non editabile
        autoreField.setStyle(autoreField.getStyle() + "; -fx-opacity: 0.7;"); // Stile per campo disabilitato
    }

    @FXML
    private void creaAnnuncio() {

        try {
            // Usa SEMPRE lo username dell'utente loggato come autore (non il campo editabile)
            String autore = Session.getInstance().getUtenteLoggato().getUsername();
            
            annuncioController.creaAnnuncio(
                    autore,                         // ✅ AUTORE (username loggato)
                    titoloField.getText(),          // ✅ TITOLO
                    descrizioneField.getText(),     // DESCRIZIONE
                    dataScadenzaPicker.getValue(),  // DATA SCADENZA
                    nomeProdottoField.getText(),    // NOME PRODOTTO
                    Integer.parseInt(quantitaField.getText()), // QUANTITÀ
                    cittaField.getText()            // CITTÀ
            );

            mostraInfo("Annuncio creato con successo");
            ViewManager.goTo(ViewType.MAIN);

        } catch (NumberFormatException e) {
            mostraErrore("La quantità deve essere numerica");
        } catch (AnnuncioNonValidoException e) {
            mostraErrore(e.getMessage());
        }
    }

    @FXML
    private void annulla() {
        // Torna alla view principale
        ViewManager.goTo(ViewType.MAIN);
    }
    
    @FXML
    private void tornaIndietro() {
        // Alias per annulla
        ViewManager.goTo(ViewType.MAIN);
    }

    private void mostraErrore(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void mostraInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
