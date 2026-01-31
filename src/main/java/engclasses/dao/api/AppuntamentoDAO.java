package engclasses.dao.api;

import model.Appuntamento;
import misc.StatoAppuntamento;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaccia DAO per la gestione della persistenza degli appuntamenti.
 */
public interface AppuntamentoDAO {

    /**
     * Salva un nuovo appuntamento.
     *
     * @param appuntamento L'appuntamento da salvare
     * @return true se il salvataggio ha successo, false altrimenti
     */
    boolean salva(Appuntamento appuntamento);

    /**
     * Aggiorna un appuntamento esistente.
     *
     * @param appuntamento L'appuntamento con i dati aggiornati
     * @return true se l'aggiornamento ha successo, false altrimenti
     */
    boolean aggiorna(Appuntamento appuntamento);

    /**
     * Elimina un appuntamento.
     *
     * @param idAppuntamento L'ID dell'appuntamento da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean elimina(String idAppuntamento);

    /**
     * Trova un appuntamento per ID.
     *
     * @param idAppuntamento L'ID dell'appuntamento
     * @return L'appuntamento trovato, o null se non esiste
     */
    Appuntamento trovaPerId(String idAppuntamento);

    /**
     * Trova tutti gli appuntamenti di un cliente.
     *
     * @param idCliente L'ID del cliente
     * @return Lista degli appuntamenti del cliente
     */
    List<Appuntamento> trovaPerCliente(String idCliente);

    /**
     * Trova tutti gli appuntamenti di un consulente.
     *
     * @param idConsulente L'ID del consulente
     * @return Lista degli appuntamenti del consulente
     */
    List<Appuntamento> trovaPerConsulente(String idConsulente);

    /**
     * Trova tutti gli appuntamenti con un determinato stato.
     *
     * @param stato Lo stato degli appuntamenti da cercare
     * @return Lista degli appuntamenti con lo stato specificato
     */
    List<Appuntamento> trovaPerStato(StatoAppuntamento stato);

    /**
     * Trova gli appuntamenti in un intervallo di date.
     *
     * @param da Data/ora di inizio intervallo
     * @param a  Data/ora di fine intervallo
     * @return Lista degli appuntamenti nell'intervallo
     */
    List<Appuntamento> trovaPerIntervallo(LocalDateTime da, LocalDateTime a);

    /**
     * Trova gli appuntamenti di un consulente in un intervallo di date.
     *
     * @param idConsulente L'ID del consulente
     * @param da           Data/ora di inizio intervallo
     * @param a            Data/ora di fine intervallo
     * @return Lista degli appuntamenti del consulente nell'intervallo
     */
    List<Appuntamento> trovaPerConsulenteEIntervallo(String idConsulente, LocalDateTime da, LocalDateTime a);

    /**
     * Restituisce tutti gli appuntamenti.
     *
     * @return Lista di tutti gli appuntamenti
     */
    List<Appuntamento> trovaTutti();

    /**
     * Verifica se esiste un conflitto di orario per un consulente.
     *
     * @param idConsulente L'ID del consulente
     * @param inizio       Data/ora di inizio del nuovo appuntamento
     * @param fine         Data/ora di fine del nuovo appuntamento
     * @param escludiId    ID dell'appuntamento da escludere (per gli aggiornamenti), può essere null
     * @return true se esiste un conflitto, false altrimenti
     */
    boolean esisteConflitto(String idConsulente, LocalDateTime inizio, LocalDateTime fine, String escludiId);
}
