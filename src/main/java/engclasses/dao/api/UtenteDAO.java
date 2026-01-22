package engclasses.dao.api;

import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Utente;

import java.sql.SQLException;

public interface UtenteDAO {


    void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException;

    boolean esisteUsername(String username) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException;

    boolean esisteEmail(String email) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException;

    Utente selezionaUtente(String username, String password) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException;

    boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence);
}
