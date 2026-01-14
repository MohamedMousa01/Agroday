package main.java.engclasses.pattern.Factory;

import main.java.misc.Session;

public class UtenteFactoryProvider {

    public static UtenteFactory getFactory(Session session) {
        if (session.isAgricoltore()) {
            return new AgricoltoreFactory();
        }
        return new VenditoreFactory();
    }
}