package engclasses.dao.fileSystem;

import engclasses.dao.api.VenditoreDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Utente;

public class VenditoreDAOFile extends UtenteDAOFile implements VenditoreDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo)  {

    }


}
