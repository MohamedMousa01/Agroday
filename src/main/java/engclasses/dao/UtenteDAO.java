package main.java.engclasses.dao;

import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.model.Utente;

public interface UtenteDAO {

    void aggiungiUtente(Utente utente, boolean persistence) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException;
}
