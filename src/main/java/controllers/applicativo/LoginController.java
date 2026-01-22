package controllers.applicativo;

import engclasses.beans.LoginBean;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.factory.DAOFactory;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.exceptions.LoginFallitoException;
import misc.PersistenceType;
import misc.Session;
import model.Utente;

public class LoginController {      //dovrò mettere anche qui il tipo di persistenza, è necessario per vedere se le modifiche
                                    // resteranno anche dopo aver chiuso l'applicazione

    //dedvo mettere due tipi di persistenza dentro a sessione: una variabile che mi salva il tipo di persistenza in registrazione
    // e un altra variabile che mi salva il tipo di persistenza dopo il login.


    private final Session session;

    public LoginController(Session session) {
        this.session = session;
    }

    public boolean autentica(String username, String password) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException, LoginFallitoException {
        PersistenceType persistenceType = session.getPersistenceType();
        if (persistenceType == null) {
            throw new LoginFallitoException("Tipo di persistenza non selezionato nella sessione.");
        }

        try {
            UtenteDAO utenteDAO = DAOFactory.getFactory(persistenceType).getLoginUtenteDAO();
            Utente utente = utenteDAO.selezionaUtente(username, password);

            if (utente != null) {
                session.setUtenteLoggato(utente);
                return true;
            }
            return false; // Credenziali non valide
        } catch (DatabaseConnessioneFallitaException | DatabaseOperazioneFallitaException e) {
            throw e; // Rilancia le eccezioni specifiche del database
        } catch (Exception e) {
            throw new LoginFallitoException("Errore sconosciuto durante l'autenticazione: " + e.getMessage());
        }
    }

//    public effettuaLogin(LoginBean loginBean, PersistenceType persistenzaLogin) throws LoginFallitoException, DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException
//        {
//
//
//
//        // Validazione campi di login
//        String errori = validaCampiLogin(loginBean);
//        if (!errori.isEmpty()) {
//            throw new LoginFallitoException(errori);
//        }
//
//    }
//


    // Metodo per validare i campi di login
    private String validaCampiLogin(LoginBean loginBean) {
        StringBuilder errori = new StringBuilder();

        if (loginBean.getUsername() == null || loginBean.getUsername().trim().isEmpty()) {
            errori.append("Il campo username non può essere vuoto.\n");
        }
        if (loginBean.getPassword() == null || loginBean.getPassword().trim().isEmpty()) {
            errori.append("Il campo password non può essere vuoto.\n");
        }
        return errori.toString();
    }


}
