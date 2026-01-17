package main.java.engclasses.dao.fileSystem;

import main.java.engclasses.dao.api.AgricoltoreDAO;
import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.misc.PersistenceType;
import main.java.model.Utente;

public class AgricoltoreDAOFile implements AgricoltoreDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {

    }
}
