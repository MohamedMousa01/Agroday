package main.java.engclasses.dao;

import main.java.engclasses.dao.AgricoltoreDAO;
import main.java.engclasses.dao.VenditoreDAO;
import main.java.model.Agricoltore;
import main.java.model.Utente;

public class UtenteDAOFactory {

    public static UtenteDAO getDAO(Utente utente){

        if (utente instanceof Agricoltore){
            return new AgricoltoreDAO();
        }
        return new VenditoreDAO();
    }
}
