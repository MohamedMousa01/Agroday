package engclasses.dao.api;

import model.Partecipazione;

import java.util.List;

/**
 * Interfaccia DAO per la gestione della persistenza delle partecipazioni agli annunci.
 */
public interface PartecipazioneDAO {

    /**
     * Salva una nuova partecipazione.
     *
     * @param partecipazione La partecipazione da salvare
     * @return true se il salvataggio ha successo, false altrimenti
     */
    boolean salva(Partecipazione partecipazione);

    /**
     * Elimina una partecipazione.
     *
     * @param idPartecipazione L'ID della partecipazione da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean elimina(String idPartecipazione);

    /**
     * Trova una partecipazione per ID.
     *
     * @param idPartecipazione L'ID della partecipazione
     * @return La partecipazione trovata, o null se non esiste
     */
    Partecipazione trovaPerId(String idPartecipazione);

    /**
     * Trova tutte le partecipazioni di un annuncio.
     *
     * @param idAnnuncio L'ID dell'annuncio
     * @return Lista delle partecipazioni all'annuncio
     */
    List<Partecipazione> trovaPerAnnuncio(String idAnnuncio);

    /**
     * Trova tutte le partecipazioni di un agricoltore.
     *
     * @param idAgricoltore L'ID (username) dell'agricoltore
     * @return Lista delle partecipazioni dell'agricoltore
     */
    List<Partecipazione> trovaPerAgricoltore(String idAgricoltore);

    /**
     * Verifica se un agricoltore ha già partecipato a un annuncio.
     *
     * @param idAnnuncio L'ID dell'annuncio
     * @param idAgricoltore L'ID (username) dell'agricoltore
     * @return true se ha già partecipato, false altrimenti
     */
    boolean haPartecipatoGia(String idAnnuncio, String idAgricoltore);

    /**
     * Restituisce tutte le partecipazioni.
     *
     * @return Lista di tutte le partecipazioni
     */
    List<Partecipazione> trovaTutte();
}
