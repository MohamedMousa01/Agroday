package engclasses.dao.memory;

import engclasses.dao.api.ConsulenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Consulente;
import model.Utente;

import java.util.ArrayList;
import java.util.List;

public class ConsulenteDAOMemory extends UtenteDAOMemory implements ConsulenteDAO {
    
    // I metodi aggiungiUtente e selezionaUtente sono ereditati da UtenteDAOMemory
    // Non serve override, funzionano già
    
    @Override
    public List<Consulente> trovaTutti() throws DatabaseOperazioneFallitaException {
        return bufferUtenti.values().stream()
                .filter(u -> u instanceof Consulente)
                .map(u -> (Consulente) u)
                .toList();
    }
}
