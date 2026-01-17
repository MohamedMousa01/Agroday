package main.java.engclasses.dao.db;

import main.java.engclasses.dao.api.ConsulenteDAO;
import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.misc.PersistenceType;
import main.java.model.Utente;

public class ConsulenteDAODB  implements ConsulenteDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {

    }
}
