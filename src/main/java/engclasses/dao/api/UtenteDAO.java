package main.java.engclasses.dao.api;

import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.misc.PersistenceType;
import main.java.model.Utente;

import java.sql.SQLException;

public interface UtenteDAO {


    void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException;

    boolean esisteUsername(String username) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException;

    boolean esisteEmail(String email) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException;

    Utente selezionaUtente(String campo, String valore, boolean persistence);

    boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence);
}
