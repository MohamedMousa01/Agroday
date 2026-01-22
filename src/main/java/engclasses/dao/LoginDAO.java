package engclasses.dao;

import engclasses.exceptions.DatabaseOperazioneFallitaException;
import model.Utente;

public interface LoginDAO {
    Utente selezionaUtente(String username, String password)
            throws DatabaseOperazioneFallitaException;

}
