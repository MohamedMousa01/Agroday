package engclasses.pattern.Factory;

import misc.TipoConsulenza;

/**
 * Provider che restituisce la factory appropriata in base al tipo di consulenza.
 * Utilizzato per semplificare la selezione della factory corretta.
 */
public class AppuntamentoFactoryProvider {

    private AppuntamentoFactoryProvider() {
        // Utility class, costruttore privato
    }

    /**
     * Restituisce la factory appropriata per il tipo di consulenza specificato.
     *
     * @param tipoConsulenza Il tipo di consulenza
     * @return La factory corrispondente
     * @throws IllegalArgumentException se il tipo di consulenza è null
     */
    public static AppuntamentoFactory getFactory(TipoConsulenza tipoConsulenza) {
        if (tipoConsulenza == null) {
            throw new IllegalArgumentException("Il tipo di consulenza non può essere null");
        }
        
        return switch (tipoConsulenza) {
            case ONLINE -> new AppuntamentoOnlineFactory();
            case IN_UFFICIO -> new AppuntamentoInUfficioFactory();
            case SUL_CAMPO -> new AppuntamentoSulCampoFactory();
        };
    }
}
