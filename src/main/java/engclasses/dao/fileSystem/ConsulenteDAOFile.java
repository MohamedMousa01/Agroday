package engclasses.dao.fileSystem;

import engclasses.dao.api.ConsulenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Consulente;
import model.Utente;

import java.util.ArrayList;
import java.util.List;

public class ConsulenteDAOFile extends UtenteDAOFile implements ConsulenteDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseOperazioneFallitaException {
        super.aggiungiUtente(utente, tipo);
    }

    @Override
    public Utente selezionaUtente(String username, String password) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        return super.selezionaUtente(username, password);
    }

    @Override
    public List<Consulente> trovaTutti() throws DatabaseOperazioneFallitaException {
        try {
            List<Utente> utenti = caricaDaFile();
            return utenti.stream()
                    .filter(u -> u instanceof Consulente)
                    .map(u -> (Consulente) u)
                    .toList();
        } catch (Exception e) {
            throw new DatabaseOperazioneFallitaException("Errore durante il caricamento dei consulenti dal file.", e);
        }
    }
}
