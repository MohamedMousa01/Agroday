package engclasses.dao.memory;

import engclasses.dao.api.ConsulenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Utente;

public class ConsulenteDAOMemory extends UtenteDAOMemory  implements ConsulenteDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {

    }


}
