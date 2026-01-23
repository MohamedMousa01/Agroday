package engclasses.dao.fileSystem;

import engclasses.dao.api.VenditoreDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Utente;

public class VenditoreDAOFile extends UtenteDAOFile implements VenditoreDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseOperazioneFallitaException {
        super.aggiungiUtente(utente, tipo);
    }

    @Override
    public Utente selezionaUtente(String username, String password) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        return super.selezionaUtente(username, password);
    }
}
