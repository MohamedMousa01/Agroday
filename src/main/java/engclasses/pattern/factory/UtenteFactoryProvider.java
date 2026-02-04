package engclasses.pattern.factory;

import misc.TipoUtente;


public class UtenteFactoryProvider {

    private UtenteFactoryProvider() {
        // Private constructor to hide the implicit public one
        throw new UnsupportedOperationException("Utility class");
    }

    public static UtenteFactory getFactory(TipoUtente tipoUtente) {
        return switch (tipoUtente){
            case AGRICOLTORE -> new AgricoltoreFactory();
            case VENDITORE -> new VenditoreFactory();
            case CONSULENTE -> new ConsulenteFactory();
        };
    }
}