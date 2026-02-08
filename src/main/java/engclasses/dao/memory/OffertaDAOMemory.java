package engclasses.dao.memory;

import engclasses.dao.api.OffertaDAO;
import model.Offerta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementazione in memoria del DAO per le offerte.
 */
public class OffertaDAOMemory extends InMemoryDAO<Offerta, String> implements OffertaDAO {

    @Override
    protected String getId(Offerta entity) {
        return entity.getIdOfferta();
    }

    @Override
    public List<Offerta> trovaPerAnnuncio(String idAnnuncio) {
        return trovaPerCriterio(o -> idAnnuncio.equals(o.getIdAnnuncio()));
    }

    @Override
    public List<Offerta> trovaPerVenditore(String usernameVenditore) {
        return trovaPerCriterio(o -> usernameVenditore.equals(o.getUsernameVenditore()));
    }

    @Override
    public List<Offerta> trovaOffertePendingPerVenditore(String usernameVenditore) {
        return trovaPerCriterio(o -> usernameVenditore.equals(o.getUsernameVenditore()) && o.isPending());
    }

    @Override
    public boolean aggiornaStato(String idOfferta, String nuovoStato) {
        Offerta offerta = trovaPerId(idOfferta);
        if (offerta != null) {
            offerta.setStato(nuovoStato);
            return true;
        }
        return false;
    }

    @Override
    public List<Offerta> trovaTutte() {
        return trovaTutti();
    }

    @Override
    public List<Offerta> trovaOfferteRicevutePerAgricoltore(String usernameAgricoltore) {
        // Per implementazione memory, dobbiamo accedere anche agli annunci
        return new ArrayList<>();
    }
}
