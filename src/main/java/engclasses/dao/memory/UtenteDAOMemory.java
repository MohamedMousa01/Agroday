package engclasses.dao.memory;

import engclasses.dao.api.UtenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Utente;

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
    public Utente selezionaUtente(String username, String password) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        return bufferUtenti.values().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
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
