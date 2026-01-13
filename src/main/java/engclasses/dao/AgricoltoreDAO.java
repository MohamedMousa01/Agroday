package main.java.engclasses.dao;

import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;

import main.java.model.Agricoltore;
import main.java.model.Utente;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AgricoltoreDAO {

    // Buffer per memorizzare temporaneamente gli Agricoltori
    private static final Map<String, Utente> bufferAgricoltori = new HashMap<>();
    private static final Set<String> CAMPI_VALIDI = Set.of("username", "idUtente", "email");
    private static final String ERRORE_AGGIORNAMENTO_DB = "Errore durante l'aggiornamento del database";

    private AgricoltoreDAO() {}


    public static void aggiungiAgricoltore(Utente agricoltore, boolean persistence) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
//        if (persistence) {
//            salvaInDb(agricoltore); // Salvataggio nel database
//        } else {
            salvaInBuffer(agricoltore); // Salvataggio temporaneo nel buffer
//        }
    }

    // Salva un agricoltore nel buffer temporaneo
    private static void salvaInBuffer(Utente agricoltore) {
        bufferAgricoltori.put(agricoltore.getIdUtente(), agricoltore);
    }


    // Seleziona un agricoltore in base al campo specificato (es. username, email, idUtente)
    public static Agricoltore selezionaAgricoltore(String campo, String valore, boolean persistence) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
//        if (persistence) {
//            // Recupera dal database
//            return recuperaDaDb(campo, valore);
//        } else {
            return (Agricoltore) bufferAgricoltori.get(valore);
//        }
    }


    public static boolean aggiornaAgricoltore(Agricoltore agricoltoreAggiornato, boolean persistence) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
//        if (persistence) {
//            return aggiornaInDb(partecipanteAggiornato);
//        } else {
            return aggiornaInBuffer(agricoltoreAggiornato);
//        }
    }

    private static boolean aggiornaInBuffer(Agricoltore agricoltoreAggiornato) {
        String username = agricoltoreAggiornato.getIdUtente();

        if (!bufferAgricoltori.containsKey(username)) {
            return false;
        }

        bufferAgricoltori.put(username, agricoltoreAggiornato);
        return true;
    }



}
