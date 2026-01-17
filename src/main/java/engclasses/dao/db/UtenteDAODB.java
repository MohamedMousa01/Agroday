package main.java.engclasses.dao.db;

import main.java.engclasses.dao.api.UtenteDAO;
import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.misc.PersistenceType;
import main.java.model.Utente;

import static main.java.engclasses.dao.memory.UtenteDAOMemory.bufferUtenti;

public class UtenteDAODB implements UtenteDAO {

    @Override
    public boolean esisteUsername(String username)
            throws DatabaseConnessioneFallitaException {

        String query = "SELECT 1 FROM utenti WHERE username = ?";
        // execute query
        return resultSet.next();
    }

    @Override
    public boolean esisteEmail(String email)
            throws DatabaseConnessioneFallitaException {

        String query = "SELECT 1 FROM utenti WHERE email = ?";
        return resultSet.next();
    }

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipoPersistenza)
            throws DatabaseOperazioneFallitaException {
        // INSERT INTO utenti ...
    }

    @Override
    public Utente selezionaUtente(String campo, String valore, boolean persistence) {

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
