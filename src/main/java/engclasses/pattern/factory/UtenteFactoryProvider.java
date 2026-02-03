package engclasses.pattern.factory;

import misc.TipoUtente;


public class UtenteFactoryProvider {

    public static UtenteFactory getFactory(TipoUtente tipoUtente) {
        return switch (tipoUtente){
            case AGRICOLTORE -> new AgricoltoreFactory();
            case VENDITORE -> new VenditoreFactory();
            case CONSULENTE -> new ConsulenteFactory();
        };
    }
}