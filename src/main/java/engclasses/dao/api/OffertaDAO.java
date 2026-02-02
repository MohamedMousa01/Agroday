package engclasses.dao.api;

import model.Offerta;

import java.util.List;

/**
 * Interfaccia DAO per la gestione della persistenza delle offerte dei venditori.
 */
public interface OffertaDAO {

    /**
     * Salva una nuova offerta.
     *
     * @param offerta L'offerta da salvare
     * @return true se il salvataggio ha successo, false altrimenti
     */
    boolean salva(Offerta offerta);

    /**
     * Elimina un'offerta.
     *
     * @param idOfferta L'ID dell'offerta da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean elimina(String idOfferta);

    /**
     * Trova un'offerta per ID.
     *
     * @param idOfferta L'ID dell'offerta
     * @return L'offerta trovata, o null se non esiste
     */
    Offerta trovaPerId(String idOfferta);

    /**
     * Trova tutte le offerte per un annuncio.
     *
     * @param idAnnuncio L'ID dell'annuncio
     * @return Lista delle offerte per l'annuncio
     */
    List<Offerta> trovaPerAnnuncio(String idAnnuncio);

    /**
     * Trova tutte le offerte di un venditore.
     *
     * @param usernameVenditore Username del venditore
     * @return Lista delle offerte del venditore
     */
    List<Offerta> trovaPerVenditore(String usernameVenditore);

    /**
     * Trova le offerte in stato PENDING di un venditore.
     *
     * @param usernameVenditore Username del venditore
     * @return Lista delle offerte in attesa
     */
    List<Offerta> trovaOffertePendingPerVenditore(String usernameVenditore);

    /**
     * Aggiorna lo stato di un'offerta.
     *
     * @param idOfferta ID dell'offerta
     * @param nuovoStato Nuovo stato (PENDING, ACCETTATA, RIFIUTATA)
     * @return true se l'aggiornamento ha successo
     */
    boolean aggiornaStato(String idOfferta, String nuovoStato);

    /**
     * Restituisce tutte le offerte.
     *
     * @return Lista di tutte le offerte
     */
    List<Offerta> trovaTutte();

    /**
     * Trova tutte le offerte ricevute da un agricoltore (per i suoi annunci).
     *
     * @param usernameAgricoltore Username dell'agricoltore
     * @return Lista delle offerte ricevute sui suoi annunci
     */
    List<Offerta> trovaOfferteRicevutePerAgricoltore(String usernameAgricoltore);
}
