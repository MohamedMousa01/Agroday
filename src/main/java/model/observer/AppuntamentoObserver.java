package model.observer;

import model.Appuntamento;

/**
 * Interfaccia Observer per ricevere notifiche sulle modifiche degli appuntamenti.
 * Parte del pattern Observer.
 */
public interface AppuntamentoObserver {

    /**
     * Chiamato quando un appuntamento viene modificato.
     *
     * @param appuntamento L'appuntamento modificato
     * @param evento       Il tipo di evento (es. "CANCELLATO_CLIENTE", "CANCELLATO_CONSULENTE", "CONFERMATO", etc.)
     */
    void onAppuntamentoModificato(Appuntamento appuntamento, String evento);
}
