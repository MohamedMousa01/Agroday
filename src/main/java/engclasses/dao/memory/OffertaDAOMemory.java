package engclasses.dao.memory;

import engclasses.dao.api.OffertaDAO;
import model.Offerta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementazione in memoria del DAO per le offerte.
 */
public class OffertaDAOMemory implements OffertaDAO {

    private static final Map<String, Offerta> offerte = new HashMap<>();

    @Override
    public boolean salva(Offerta offerta) {
        if (offerta == null || offerta.getIdOfferta() == null) {
            return false;
        }
        offerte.put(offerta.getIdOfferta(), offerta);
        return true;
    }

    @Override
    public boolean elimina(String idOfferta) {
        return offerte.remove(idOfferta) != null;
    }

    @Override
    public Offerta trovaPerId(String idOfferta) {
        return offerte.get(idOfferta);
    }

    @Override
    public List<Offerta> trovaPerAnnuncio(String idAnnuncio) {
        return offerte.values().stream()
                .filter(o -> idAnnuncio.equals(o.getIdAnnuncio()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Offerta> trovaPerVenditore(String usernameVenditore) {
        return offerte.values().stream()
                .filter(o -> usernameVenditore.equals(o.getUsernameVenditore()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Offerta> trovaOffertePendingPerVenditore(String usernameVenditore) {
        return offerte.values().stream()
                .filter(o -> usernameVenditore.equals(o.getUsernameVenditore()) && o.isPending())
                .collect(Collectors.toList());
    }

    @Override
    public boolean aggiornaStato(String idOfferta, String nuovoStato) {
        Offerta offerta = offerte.get(idOfferta);
        if (offerta != null) {
            offerta.setStato(nuovoStato);
            return true;
        }
        return false;
    }

    @Override
    public List<Offerta> trovaTutte() {
        return new ArrayList<>(offerte.values());
    }

    @Override
    public List<Offerta> trovaOfferteRicevutePerAgricoltore(String usernameAgricoltore) {
        // Per implementazione memory, dobbiamo accedere anche agli annunci
        // Soluzione semplificata: restituiamo lista vuota o implementazione base
        return new ArrayList<>();
    }

    public static void clear() {
        offerte.clear();
    }
}
