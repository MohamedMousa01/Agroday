package engclasses.dao.memory;

import engclasses.dao.api.ConsulenteDAO;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import model.Consulente;

import java.util.List;

public class ConsulenteDAOMemory extends UtenteDAOMemory implements ConsulenteDAO {
    
    // I metodi aggiungiUtente e selezionaUtente sono ereditati da UtenteDAOMemory
    // Non serve override, funzionano già
    
    @Override
    public List<Consulente> trovaTutti() throws DatabaseOperazioneFallitaException {
        return bufferUtenti.values().stream()
                .filter(Consulente.class::isInstance)
                .map(Consulente.class::cast)
                .toList();
    }
}
