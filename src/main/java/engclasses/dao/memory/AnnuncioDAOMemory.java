package engclasses.dao.memory;

import engclasses.dao.api.AnnuncioDAO;
import model.Annuncio;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione in memoria del DAO per gli annunci.
 * Utilizzata in modalità Demo.
 */
public class AnnuncioDAOMemory extends InMemoryDAO<Annuncio, String> implements AnnuncioDAO {

    @Override
    protected String getId(Annuncio entity) {
        return entity.getIdAnnuncio();
    }

    @Override
    public List<Annuncio> trovaPerAutore(String autore) {
        if (autore == null) {
            return new ArrayList<>();
        }
        return trovaPerCriterio(a -> autore.equals(a.getAutore()));
    }

    @Override
    public List<Annuncio> trovaPerCitta(String citta) {
        if (citta == null) {
            return new ArrayList<>();
        }
        return trovaPerCriterio(a -> citta.equals(a.getCitta()));
    }

    @Override
    public List<Annuncio> trovaAttivi() {
        return trovaPerCriterio(a -> !a.isScaduto());
    }
}
