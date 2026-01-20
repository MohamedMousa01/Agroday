package main.java.engclasses.dao.memory;

import main.java.engclasses.dao.api.UtenteDAO;
import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.misc.PersistenceType;
import main.java.model.Utente;

import java.util.*;

public class UtenteDAOMemory implements UtenteDAO {

    // Buffer per memorizzare temporaneamente i partecipanti
    protected static final Map<String, Utente> bufferUtenti = new HashMap<>();
    protected static final Set<String> CAMPI_VALIDI = Set.of("username", "idUtente", "email");
    protected static final String ERRORE_AGGIORNAMENTO_DB = "Errore durante l'aggiornamento del database";


    @Override
    public boolean esisteUsername(String username) {
        return bufferUtenti.values().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    @Override
    public boolean esisteEmail(String email) {
        return bufferUtenti.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        bufferUtenti.put(utente.getIdUtente(), utente);
    }


    @Override
    public Utente selezionaUtente(String campo, String valore, boolean persistence) {

        return bufferUtenti.get(valore);
    }

    @Override
    public boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence){
        String username = utenteAggiornato.getIdUtente();

        if (!bufferUtenti.containsKey(username)) {
            return false;
        }

        bufferUtenti.put(username, utenteAggiornato);
        return true;
    }
}
