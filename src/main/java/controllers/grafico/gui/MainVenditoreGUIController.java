package controllers.grafico.gui;

import engclasses.pattern.viewfactory.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import misc.MessageConstants;
import misc.Session;
import misc.ViewType;
import model.Utente;

import java.util.Optional;

/**
 * Controller per la view principale del Venditore
 */
public class MainVenditoreGUIController {

    @FXML private Label lblBenvenuto;
    @FXML private Button btnCercaAnnunci;
    @FXML private Button mieOfferteButton;
    @FXML private Button btnOrdini;
    @FXML private Button btnProfilo;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        // Imposta messaggio di benvenuto
        Utente utente = Session.getInstance().getUtenteLoggato();
        if (utente != null) {
            lblBenvenuto.setText("Benvenuto, " + utente.getNome() + " " + utente.getCognome() + "!");
        }
    }

    @FXML
    private void cercaAnnunci() {
        // Naviga alla lista annunci (che ha già la ricerca integrata)
        ViewManager.goTo(ViewType.VISUALIZZA_ANNUNCI);
    }

    @FXML
    private void visualizzaTuttiAnnunci() {
        // Naviga alla lista completa annunci
        ViewManager.goTo(ViewType.VISUALIZZA_ANNUNCI);
    }

    @FXML
    private void visualizzaOrdini() {
        // Mostra messaggio temporaneo (funzionalità da implementare)
        mostraMessaggio("I Miei Ordini", 
            """
            Funzionalità in fase di sviluppo.
            
            Qui potrai gestire gli ordini ricevuti dagli agricoltori.""", 
            Alert.AlertType.INFORMATION);
    }

    @FXML
    private void showMieOfferte() {
        // Mostra le offerte del venditore
        controllers.applicativo.OffertaController offertaController = 
            new controllers.applicativo.OffertaController();
        
        String username = Session.getInstance().getUtenteLoggato().getUsername();
        java.util.List<model.Offerta> offerte = offertaController.getOfferteVenditore(username);
        
        if (offerte.isEmpty()) {
            mostraMessaggio("Le Mie Offerte", 
                """
                Non hai ancora fatto nessuna offerta.
                
                Vai su 'Cerca Annunci' per trovare annunci scaduti
                e proporre i tuoi prezzi!""", 
                Alert.AlertType.INFORMATION);
        } else {
            StringBuilder msg = new StringBuilder();
            msg.append(String.format("Hai %d offerte totali:%n%n", offerte.size()));
            
            long pending = offerte.stream().filter(model.Offerta::isPending).count();
            long accettate = offerte.stream().filter(model.Offerta::isAccettata).count();
            long rifiutate = offerte.stream().filter(model.Offerta::isRifiutata).count();
            
            msg.append(String.format("⏳ In attesa: %d%n", pending));
            msg.append(String.format("✅ Accettate: %d%n", accettate));
            msg.append(String.format("❌ Rifiutate: %d%n%n", rifiutate));
            
            msg.append("Ultime offerte:%n");
            offerte.stream().limit(5).forEach(o -> {
                msg.append(String.format("%n💰 Prezzo: %.2f €/kg | Totale: %.2f €%n", 
                    o.getPrezzoAlKg(), o.getPrezzoTotale()));
                msg.append(String.format("  " + MessageConstants.LABEL_STATO + "%s |" + MessageConstants.LABEL_DATA + "%s%n", 
                    o.getStato(), o.getDataOfferta().toLocalDate()));
            });
            
            mostraMessaggio("Le Mie Offerte", msg.toString(), Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void visualizzaProfilo() {
        // Naviga al profilo
        ViewManager.goTo(ViewType.PROFILO);
    }

    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText("Sei sicuro di voler uscire?");
        alert.setContentText("Dovrai effettuare nuovamente il login per accedere.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Session.getInstance().clearSession();
            ViewManager.goTo(ViewType.LOGIN);
        }
    }

    private void mostraMessaggio(String titolo, String messaggio, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
