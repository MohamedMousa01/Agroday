package main.java.engclasses.dao;

import  main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import  main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;

import main.java.model.Venditore;
import main.java.model.Utente;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class VenditoreDAO {

    // Buffer per memorizzare temporaneamente gli Venditori
    private static final Map<String, Utente> bufferVenditori = new HashMap<>();
    private static final Set<String> CAMPI_VALIDI = Set.of("username", "idUtente", "email");

    private VenditoreDAO() {}

    // Aggiunge un organizzatore, scegliendo tra buffer o database in base al flag 'persistence'
    public static void aggiungiVenditore(Utente venditore, boolean persistence) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
//        if (persistence) {
//            salvaInDb(organizzatore); // Salvataggio nel database
//        } else {
            salvaInBuffer(venditore); // Salvataggio temporaneo nel buffer
//        }
    }


    // Salva un venditore nel buffer temporaneo
    private static void salvaInBuffer(Utente venditore) {
        bufferVenditori.put(venditore.getIdUtente(), venditore);
    }

    // Seleziona un venditore in base al campo specificato (es. username, idUtente)
    public static Venditore selezionaVenditore(String campo, String valore, boolean persistence) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
//        if (persistence) {
//            return recuperaDaDb(campo, valore); // Recupera dal database
//        } else {
            return (Venditore) bufferVenditori.get(valore); // Recupera dal buffer
//        }
    }

    // Aggiorna un venditore
    public static boolean aggiornaVenditore(Venditore venditoreggiornato, boolean persistence) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
//        if (persistence) {
//            return aggiornaInDb(venditoreggiornato);
//        } else {
            return aggiornaInBuffer(venditoreggiornato);
//        }
    }

    private static boolean aggiornaInBuffer(Venditore venditoreAggiornato) {
        String idUtente = venditoreAggiornato.getIdUtente();

        if (!bufferVenditori.containsKey(idUtente)) {
            return false;
        }

        bufferVenditori.put(idUtente, venditoreAggiornato);
        return true;
    }



}
