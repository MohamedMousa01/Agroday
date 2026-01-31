package controllers.grafico.gui;

import controllers.applicativo.AnnuncioController;
import engclasses.exceptions.AnnuncioNonValidoException;
import engclasses.pattern.ViewFactory.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import misc.ViewType;

import java.time.LocalDate;

public class CreaAnnuncioGUIController {

    @FXML private TextField titoloField;
    @FXML private TextField autoreField;
    @FXML private DatePicker dataScadenzaPicker;
    @FXML private TextField nomeProdottoField;
    @FXML private TextField quantitaField;
    @FXML private TextField cittaField;
    @FXML private TextArea descrizioneField;  // Cambiato da TextField a TextArea

    private final AnnuncioController annuncioController =
            new AnnuncioController();

    @FXML
    private void creaAnnuncio() {

        try {
            annuncioController.creaAnnuncio(
                    titoloField.getText(),
                    autoreField.getText(),
                    descrizioneField.getText(),
                    dataScadenzaPicker.getValue(),
                    nomeProdottoField.getText(),
                    Integer.parseInt(quantitaField.getText()),
                    cittaField.getText()
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
