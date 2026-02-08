package controllers.grafico.gui;

import javafx.scene.control.Alert;

/**
 * Classe base per tutti i controller GUI.
 * Contiene metodi comuni per evitare duplicazioni.
 */
public abstract class BaseGUIController {

    /**
     * Mostra un messaggio all'utente tramite Alert.
     *
     * @param titolo il titolo dell'alert
     * @param messaggio il contenuto del messaggio
     * @param tipo il tipo di alert
     */
    protected void mostraMessaggio(String titolo, String messaggio, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /**
     * Mostra un messaggio di errore.
     *
     * @param titolo il titolo dell'alert
     * @param messaggio il messaggio di errore
     */
    protected void mostraErrore(String titolo, String messaggio) {
        mostraMessaggio(titolo, messaggio, Alert.AlertType.ERROR);
    }

    /**
     * Mostra un messaggio informativo.
     *
     * @param titolo il titolo dell'alert
     * @param messaggio il messaggio informativo
     */
    protected void mostraInfo(String titolo, String messaggio) {
        mostraMessaggio(titolo, messaggio, Alert.AlertType.INFORMATION);
    }

    /**
     * Mostra un messaggio di successo.
     *
     * @param titolo il titolo dell'alert
     * @param messaggio il messaggio di successo
     */
    protected void mostraSuccesso(String titolo, String messaggio) {
        mostraMessaggio(titolo, messaggio, Alert.AlertType.INFORMATION);
    }
}
