package engclasses.dao.filesystem;

import engclasses.dao.api.ConsulenteDAO;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import model.Consulente;
import model.Utente;

import java.util.List;

public class ConsulenteDAOFile extends UtenteDAOFile implements ConsulenteDAO {
    // I metodi aggiungiUtente e selezionaUtente sono ereditati da UtenteDAOFile
    // Non serve override, funzionano già

    @Override
    public List<Consulente> trovaTutti() throws DatabaseOperazioneFallitaException {
        try {
            List<Utente> utenti = caricaDaFile();
            return utenti.stream()
                    .filter(Consulente.class::isInstance)
                    .map(Consulente.class::cast)
                    .toList();
        } catch (Exception e) {
            throw new DatabaseOperazioneFallitaException("Errore durante il caricamento dei consulenti dal file.", e);
        }
    }
}
