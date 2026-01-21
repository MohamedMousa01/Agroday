package engclasses.dao.fileSystem;

import engclasses.dao.api.AgricoltoreDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Utente;

public class AgricoltoreDAOFile extends UtenteDAOFile implements AgricoltoreDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo)  {

    }

    @Override
    public Utente selezionaUtente(String campo, String valore, boolean persistence) {

        return selezionaUtente(campo, valore, persistence); //non ha senso cancella
    }

    public boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence){
        return true;
    }

}
