package engclasses.pattern.Factory;

import model.Appuntamento;
import model.AppuntamentoSulCampo;

import java.time.LocalDateTime;

/**
 * Factory concreta per la creazione di AppuntamentoSulCampo.
 * Implementa il pattern Factory Method.
 */
public class AppuntamentoSulCampoFactory implements AppuntamentoFactory {

    private static final int DURATA_PREDEFINITA_MINUTI = 120;
    private static final int DURATA_MINIMA_MINUTI = 60;

    @Override
    public Appuntamento creaAppuntamento(String idCliente, String idConsulente,
                                          LocalDateTime dataOraInizio, LocalDateTime dataOraFine,
                                          String luogo) {
        return new AppuntamentoSulCampo(
                idCliente, idConsulente, dataOraInizio, dataOraFine, luogo);
    }

    @Override
    public int getDurataPredefinitaMinuti() {
        return DURATA_PREDEFINITA_MINUTI;
    }

    @Override
    public String valida(String luogo, long durataMinuti) {
        if (luogo == null || luogo.trim().isEmpty()) {
            return "L'indirizzo del terreno/azienda è obbligatorio per le consulenze sul campo.";
        }
        if (durataMinuti < DURATA_MINIMA_MINUTI) {
            return "Le consulenze sul campo richiedono una durata minima di " + DURATA_MINIMA_MINUTI + " minuti.";
        }
        return null;
    }
}
