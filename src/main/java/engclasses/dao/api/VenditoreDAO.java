package engclasses.dao.api;

import  engclasses.exceptions.DatabaseConnessioneFallitaException;
import  engclasses.exceptions.DatabaseOperazioneFallitaException;

import model.Venditore;
import model.Utente;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public interface VenditoreDAO extends engclasses.dao.api.UtenteDAO {

//    // Buffer per memorizzare temporaneamente gli Venditori
//    private static final Map<String, Utente> bufferVenditori = new HashMap<>();
//    private static final Set<String> CAMPI_VALIDI = Set.of("username", "idUtente", "email");
//
//    protected VenditoreDAO() {}
//
//    // Aggiunge un organizzatore, scegliendo tra buffer o database in base al flag 'persistence'
//    public void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
////        if (persistence) {
////            salvaInDb(organizzatore); // Salvataggio nel database
////        } else {
//            salvaInBuffer(utente); // Salvataggio temporaneo nel buffer
////        }
//    }
//
//
//    // Salva un venditore nel buffer temporaneo
//    private static void salvaInBuffer(Utente venditore) {
//        bufferVenditori.put(venditore.getIdUtente(), venditore);        //associo alla chiave idUtente, il valore che tiene il
//                                                                        // riferimento al model/entity utente.
//    }
//
//    // Seleziona un venditore in base al campo specificato (es. username, idUtente)
//    public static Venditore selezionaVenditore(String campo, String valore, boolean persistence) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
////        if (persistence) {
////            return recuperaDaDb(campo, valore); // Recupera dal database
////        } else {
//            return (Venditore) bufferVenditori.get(valore); // Recupera dal buffer
////        }
//    }
//
//    // Aggiorna un venditore
//    public static boolean aggiornaVenditore(Venditore venditoreggiornato, boolean persistence) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
////        if (persistence) {
////            return aggiornaInDb(venditoreggiornato);
////        } else {
//            return aggiornaInBuffer(venditoreggiornato);
////        }
//    }
//
//    private static boolean aggiornaInBuffer(Venditore venditoreAggiornato) {
//        String idUtente = venditoreAggiornato.getIdUtente();
//
//        if (!bufferVenditori.containsKey(idUtente)) {
//            return false;
//        }
//
//        bufferVenditori.put(idUtente, venditoreAggiornato);
//        return true;
//    }
//
//
//
}
