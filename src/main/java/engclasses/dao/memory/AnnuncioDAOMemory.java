package engclasses.dao.memory;

import engclasses.dao.api.AnnuncioDAO;
import model.Annuncio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementazione in memoria del DAO per gli annunci.
 * Utilizzata in modalità Demo.
 */
public class AnnuncioDAOMemory implements AnnuncioDAO {

    // Storage in memoria condiviso (Singleton-like per mantenere i dati durante la sessione)
    private static final Map<String, Annuncio> annunci = new HashMap<>();

    @Override
    public boolean salva(Annuncio annuncio) {
        if (annuncio == null || annuncio.getIdAnnuncio() == null) {
            return false;
        }
        annunci.put(annuncio.getIdAnnuncio(), annuncio);
        return true;
    }

    @Override
    public boolean elimina(String idAnnuncio) {
        if (idAnnuncio == null) {
            return false;
        }
        return annunci.remove(idAnnuncio) != null;
    }

    @Override
    public Annuncio trovaPerId(String idAnnuncio) {
        if (idAnnuncio == null) {
            return null;
        }
        return annunci.get(idAnnuncio);
    }

    @Override
    public List<Annuncio> trovaPerAutore(String autore) {
        if (autore == null) {
            return new ArrayList<>();
        }
        return annunci.values().stream()
                .filter(a -> autore.equals(a.getAutore()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Annuncio> trovaPerCitta(String citta) {
        if (citta == null) {
            return new ArrayList<>();
        }
        return annunci.values().stream()
                .filter(a -> citta.equals(a.getCitta()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Annuncio> trovaTutti() {
        return new ArrayList<>(annunci.values());
    }

    @Override
    public List<Annuncio> trovaAttivi() {
        return annunci.values().stream()
                .filter(a -> !a.isScaduto())
                .collect(Collectors.toList());
    }

    /**
     * Pulisce tutti i dati in memoria.
     * Utile per i test.
     */
    public static void clear() {
        annunci.clear();
    }
}
