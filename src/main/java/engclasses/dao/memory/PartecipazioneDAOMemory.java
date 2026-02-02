package engclasses.dao.memory;

import engclasses.dao.api.PartecipazioneDAO;
import model.Partecipazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementazione in memoria del DAO per le partecipazioni.
 */
public class PartecipazioneDAOMemory implements PartecipazioneDAO {

    private static final Map<String, Partecipazione> partecipazioni = new HashMap<>();

    @Override
    public boolean salva(Partecipazione partecipazione) {
        if (partecipazione == null || partecipazione.getIdPartecipazione() == null) {
            return false;
        }
        partecipazioni.put(partecipazione.getIdPartecipazione(), partecipazione);
        return true;
    }

    @Override
    public boolean elimina(String idPartecipazione) {
        if (idPartecipazione == null) {
            return false;
        }
        return partecipazioni.remove(idPartecipazione) != null;
    }

    @Override
    public Partecipazione trovaPerId(String idPartecipazione) {
        if (idPartecipazione == null) {
            return null;
        }
        return partecipazioni.get(idPartecipazione);
    }

    @Override
    public List<Partecipazione> trovaPerAnnuncio(String idAnnuncio) {
        if (idAnnuncio == null) {
            return new ArrayList<>();
        }
        return partecipazioni.values().stream()
                .filter(p -> idAnnuncio.equals(p.getIdAnnuncio()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Partecipazione> trovaPerAgricoltore(String idAgricoltore) {
        if (idAgricoltore == null) {
            return new ArrayList<>();
        }
        return partecipazioni.values().stream()
                .filter(p -> idAgricoltore.equals(p.getIdAgricoltore()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean haPartecipatoGia(String idAnnuncio, String idAgricoltore) {
        if (idAnnuncio == null || idAgricoltore == null) {
            return false;
        }
        return partecipazioni.values().stream()
                .anyMatch(p -> idAnnuncio.equals(p.getIdAnnuncio()) && 
                              idAgricoltore.equals(p.getIdAgricoltore()));
    }

    @Override
    public List<Partecipazione> trovaTutte() {
        return new ArrayList<>(partecipazioni.values());
    }

    /**
     * Pulisce tutti i dati in memoria.
     */
    public static void clear() {
        partecipazioni.clear();
    }
}
