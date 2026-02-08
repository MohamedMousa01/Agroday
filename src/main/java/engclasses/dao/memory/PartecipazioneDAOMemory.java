package engclasses.dao.memory;

import engclasses.dao.api.PartecipazioneDAO;
import model.Partecipazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementazione in memoria del DAO per le partecipazioni.
 */
public class PartecipazioneDAOMemory extends InMemoryDAO<Partecipazione, String> implements PartecipazioneDAO {

    @Override
    protected String getId(Partecipazione entity) {
        return entity.getIdPartecipazione();
    }

    @Override
    public List<Partecipazione> trovaPerAnnuncio(String idAnnuncio) {
        if (idAnnuncio == null) {
            return new ArrayList<>();
        }
        return trovaPerCriterio(p -> idAnnuncio.equals(p.getIdAnnuncio()));
    }

    @Override
    public List<Partecipazione> trovaPerAgricoltore(String idAgricoltore) {
        if (idAgricoltore == null) {
            return new ArrayList<>();
        }
        return trovaPerCriterio(p -> idAgricoltore.equals(p.getIdAgricoltore()));
    }

    @Override
    public boolean haPartecipatoGia(String idAnnuncio, String idAgricoltore) {
        if (idAnnuncio == null || idAgricoltore == null) {
            return false;
        }
        return storage.values().stream()
                .anyMatch(p -> idAnnuncio.equals(p.getIdAnnuncio()) && 
                              idAgricoltore.equals(p.getIdAgricoltore()));
    }

    @Override
    public List<Partecipazione> trovaTutte() {
        return trovaTutti();
    }
}
