package engclasses.dao.fileSystem;

import engclasses.dao.api.ConsulenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Utente;

public class ConsulenteDAOFile extends UtenteDAOFile implements ConsulenteDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseOperazioneFallitaException {
        super.aggiungiUtente(utente, tipo);
    }

    @Override
    public Utente selezionaUtente(String username, String password) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        return super.selezionaUtente(username, password);
    }
}
