package engclasses.pattern.Factory;

import model.Appuntamento;

import java.time.LocalDateTime;

/**
 * Interfaccia Factory Method per la creazione di Appuntamenti.
 * Ogni factory concreta crea un tipo specifico di appuntamento.
 */
public interface AppuntamentoFactory {

    /**
     * Crea un nuovo appuntamento del tipo specifico.
     *
     * @param idCliente ID del cliente
     * @param idConsulente ID del consulente
     * @param dataOraInizio Data e ora di inizio
     * @param dataOraFine Data e ora di fine
     * @param luogo Luogo (URL per online, indirizzo per altri tipi)
     * @return L'appuntamento creato
     */
    Appuntamento creaAppuntamento(String idCliente, String idConsulente,
                                   LocalDateTime dataOraInizio, LocalDateTime dataOraFine,
                                   String luogo);

    /**
     * Restituisce la durata predefinita in minuti per questo tipo di appuntamento.
     *
     * @return Durata predefinita in minuti
     */
    int getDurataPredefinitaMinuti();

    /**
     * Valida i dati specifici per questo tipo di appuntamento.
     *
     * @param luogo Il luogo dell'appuntamento
     * @param durataMinuti La durata in minuti
     * @return Messaggio di errore se non valido, null se valido
     */
    String valida(String luogo, long durataMinuti);
}
