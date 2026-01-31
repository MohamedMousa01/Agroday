package engclasses.services;

import model.Appuntamento;

/**
 * Interfaccia per il servizio di calendario.
 * Definisce le operazioni per la gestione degli eventi sul calendario.
 * Parte del pattern Adapter.
 */
public interface CalendarService {

    /**
     * Crea un nuovo evento sul calendario.
     *
     * @param appuntamento L'appuntamento da aggiungere al calendario
     * @return L'ID dell'evento creato, o null se la creazione fallisce
     */
    String creaEvento(Appuntamento appuntamento);

    /**
     * Aggiorna un evento esistente sul calendario.
     *
     * @param appuntamento L'appuntamento con i dati aggiornati
     * @return true se l'aggiornamento ha successo, false altrimenti
     */
    boolean aggiornaEvento(Appuntamento appuntamento);

    /**
     * Cancella un evento dal calendario.
     *
     * @param eventId L'ID dell'evento da cancellare
     * @return true se la cancellazione ha successo, false altrimenti
     */
    boolean cancellaEvento(String eventId);

    /**
     * Verifica se il servizio di calendario è disponibile e configurato.
     *
     * @return true se il servizio è disponibile, false altrimenti
     */
    boolean isDisponibile();

    /**
     * Ottiene il link dell'evento sul calendario.
     *
     * @param eventId L'ID dell'evento
     * @return Il link all'evento, o null se non disponibile
     */
    String getLinkEvento(String eventId);
}
