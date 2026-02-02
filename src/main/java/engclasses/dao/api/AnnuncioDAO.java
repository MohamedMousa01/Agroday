package engclasses.dao.api;

import model.Annuncio;

import java.util.List;

/**
 * Interfaccia DAO per la gestione della persistenza degli annunci.
 */
public interface AnnuncioDAO {

    /**
     * Salva un nuovo annuncio.
     *
     * @param annuncio L'annuncio da salvare
     * @return true se il salvataggio ha successo, false altrimenti
     */
    boolean salva(Annuncio annuncio);

    /**
     * Elimina un annuncio.
     *
     * @param idAnnuncio L'ID dell'annuncio da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean elimina(String idAnnuncio);

    /**
     * Trova un annuncio per ID.
     *
     * @param idAnnuncio L'ID dell'annuncio
     * @return L'annuncio trovato, o null se non esiste
     */
    Annuncio trovaPerId(String idAnnuncio);

    /**
     * Trova tutti gli annunci di un autore.
     *
     * @param autore Il nome dell'autore
     * @return Lista degli annunci dell'autore
     */
    List<Annuncio> trovaPerAutore(String autore);

    /**
     * Trova tutti gli annunci in una città.
     *
     * @param citta La città
     * @return Lista degli annunci nella città specificata
     */
    List<Annuncio> trovaPerCitta(String citta);

    /**
     * Restituisce tutti gli annunci.
     *
     * @return Lista di tutti gli annunci
     */
    List<Annuncio> trovaTutti();

    /**
     * Trova gli annunci non scaduti.
     *
     * @return Lista degli annunci ancora validi
     */
    List<Annuncio> trovaAttivi();
}
