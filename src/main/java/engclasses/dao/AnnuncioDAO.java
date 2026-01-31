package engclasses.dao;

import model.Annuncio;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per gestire gli annunci.
 * Attualmente usa una lista in memoria, da sostituire con DB.
 */
public class AnnuncioDAO {
    
    private static final List<Annuncio> annunci = new ArrayList<>();
    
    private AnnuncioDAO() {
        // Costruttore privato per utility class
    }
    
    /**
     * Recupera tutti gli annunci
     * @return lista di annunci
     */
    public static List<Annuncio> getAnnunci() {
        return new ArrayList<>(annunci);
    }
    
    /**
     * Salva un nuovo annuncio
     * @param annuncio l'annuncio da salvare
     */
    public static void salvaAnnuncio(Annuncio annuncio) {
        if (annuncio == null) {
            throw new IllegalArgumentException("Annuncio non può essere null");
        }
        annunci.add(annuncio);
    }
    
    /**
     * Cerca annunci per autore
     * @param autore username dell'autore
     * @return lista di annunci dell'autore
     */
    public static List<Annuncio> getAnnunciByAutore(String autore) {
        return annunci.stream()
                .filter(a -> a.getAutore().equals(autore))
                .toList();
    }
    
    /**
     * Cerca annunci per città
     * @param citta la città
     * @return lista di annunci nella città
     */
    public static List<Annuncio> getAnnunciByCitta(String citta) {
        return annunci.stream()
                .filter(a -> a.getCitta().equals(citta))
                .toList();
    }
    
    /**
     * Elimina tutti gli annunci (utile per testing)
     */
    public static void clear() {
        annunci.clear();
    }
}
