package engclasses.dao.api;

import engclasses.exceptions.DatabaseOperazioneFallitaException;
import model.Consulente;
import java.util.List;

public interface ConsulenteDAO extends UtenteDAO{
    
    /**
     * Recupera tutti i consulenti registrati nel sistema.
     *
     * @return Lista di tutti i consulenti
     */
    List<Consulente> trovaTutti() throws DatabaseOperazioneFallitaException;
}
