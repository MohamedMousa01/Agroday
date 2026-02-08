package engclasses.dao.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe base per tutti i DAO in-memory.
 * Fornisce implementazione comune per ridurre duplicazioni.
 *
 * @param <T> tipo dell'entità
 * @param <ID> tipo dell'identificatore
 */
public abstract class InMemoryDAO<T, ID> {

    protected final Map<ID, T> storage = new HashMap<>();

    /**
     * Ottiene l'ID di un'entità.
     */
    protected abstract ID getId(T entity);

    /**
     * Salva un'entità nella memoria.
     */
    public boolean salva(T entity) {
        if (entity == null) {
            return false;
        }
        storage.put(getId(entity), entity);
        return true;
    }

    /**
     * Elimina un'entità per ID.
     */
    public boolean elimina(ID id) {
        return storage.remove(id) != null;
    }

    /**
     * Trova un'entità per ID.
     */
    public T trovaPerId(ID id) {
        return storage.get(id);
    }

    /**
     * Trova tutte le entità.
     */
    public List<T> trovaTutti() {
        return new ArrayList<>(storage.values());
    }

    /**
     * Trova entità che soddisfano un predicato.
     */
    protected List<T> trovaPerCriterio(java.util.function.Predicate<T> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }

    /**
     * Trova la prima entità che soddisfa un predicato.
     */
    protected T trovaPrimoPer(java.util.function.Predicate<T> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Pulisce lo storage.
     */
    public void clear() {
        storage.clear();
    }

    /**
     * Conta il numero di entità.
     */
    public int count() {
        return storage.size();
    }

    /**
     * Verifica se esiste un'entità con l'ID specificato.
     */
    public boolean exists(ID id) {
        return storage.containsKey(id);
    }
}
