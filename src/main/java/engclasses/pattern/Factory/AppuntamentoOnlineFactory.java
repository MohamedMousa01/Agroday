package engclasses.pattern.Factory;

import model.Appuntamento;
import model.AppuntamentoOnline;

import java.time.LocalDateTime;

/**
 * Factory concreta per la creazione di AppuntamentoOnline.
 * Implementa il pattern Factory Method.
 */
public class AppuntamentoOnlineFactory implements AppuntamentoFactory {

    private static final int DURATA_PREDEFINITA_MINUTI = 60;

    @Override
    public Appuntamento creaAppuntamento(String idCliente, String idConsulente,
                                          LocalDateTime dataOraInizio, LocalDateTime dataOraFine,
                                          String luogo) {
        AppuntamentoOnline appuntamento = new AppuntamentoOnline(
                idCliente, idConsulente, dataOraInizio, dataOraFine, luogo);
        
        // Se il luogo (URL) non è specificato, genera automaticamente
        if (luogo == null || luogo.trim().isEmpty()) {
            appuntamento.generaUrlMeeting();
        }
        
        return appuntamento;
    }

    @Override
    public int getDurataPredefinitaMinuti() {
        return DURATA_PREDEFINITA_MINUTI;
    }

    @Override
    public String valida(String luogo, long durataMinuti) {
        // Per le consulenze online, il luogo (URL) può essere generato automaticamente
        // quindi non è obbligatorio
        return null;
    }
}
