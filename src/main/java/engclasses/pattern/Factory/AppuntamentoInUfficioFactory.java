package engclasses.pattern.Factory;

import model.Appuntamento;
import model.AppuntamentoInUfficio;

import java.time.LocalDateTime;

/**
 * Factory concreta per la creazione di AppuntamentoInUfficio.
 * Implementa il pattern Factory Method.
 */
public class AppuntamentoInUfficioFactory implements AppuntamentoFactory {

    private static final int DURATA_PREDEFINITA_MINUTI = 90;

    @Override
    public Appuntamento creaAppuntamento(String idCliente, String idConsulente,
                                          LocalDateTime dataOraInizio, LocalDateTime dataOraFine,
                                          String luogo) {
        return new AppuntamentoInUfficio(
                idCliente, idConsulente, dataOraInizio, dataOraFine, luogo);
    }

    @Override
    public int getDurataPredefinitaMinuti() {
        return DURATA_PREDEFINITA_MINUTI;
    }

    @Override
    public String valida(String luogo, long durataMinuti) {
        if (luogo == null || luogo.trim().isEmpty()) {
            return "L'indirizzo dell'ufficio è obbligatorio per le consulenze in ufficio.";
        }
        return null;
    }
}
