package controllers.applicativo;

import engclasses.beans.LoginBean;
import engclasses.dao.UtenteDAOFactory;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.factory.DAOFactory;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.exceptions.LoginFallitoException;
import misc.AppResult;
import misc.PersistenceType;
import misc.Session;
import model.Utente;

public class LoginController {

    private final Session session;

    public LoginController(Session session) {
        this.session = session;
    }

    //verifica che le credenziali inserite corrispondono ad un utente esistente
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
                // Imposta anche il tipo utente nella sessione
                session.setTipoUtente(utente.getTipo());
                return true;
            }
            return false; // Credenziali non valide
        } catch (DatabaseConnessioneFallitaException | DatabaseOperazioneFallitaException e) {
            // Rilancia le eccezioni specifiche del database senza wrapping
            throw e;
        } catch (Exception e) {
            throw new LoginFallitoException("Errore sconosciuto durante l'autenticazione: " + e.getMessage(), e);
        }
    }


    public Utente login(String username, String password)
            throws DatabaseOperazioneFallitaException, LoginFallitoException {

        validaCampiLogin(username, password);

        try {
            UtenteDAO dao = UtenteDAOFactory.getUtenteDAO(
                    Session.getInstance().getPersistenceType());

            Utente utente = dao.selezionaUtente(username, password);


            if (utente == null) {
                return null;
            }

            // Imposta utente loggato nella sessione
            Session.getInstance().setUtenteLoggato(utente);
            // Imposta anche il tipo utente
            Session.getInstance().setTipoUtente(utente.getTipo());

            return utente;
        } catch (DatabaseConnessioneFallitaException | DatabaseOperazioneFallitaException e) {
            throw new LoginFallitoException("Errore di sistema durante il login", e);
        }
    }



    // Metodo per validare i campi di login
    private String validaCampiLogin(String username, String password) {
        StringBuilder errori = new StringBuilder();

        if (username == null || username.trim().isEmpty()) {
            errori.append("Il campo username non può essere vuoto.\n");
        }
        if (password == null || password.trim().isEmpty()) {
            errori.append("Il campo password non può essere vuoto.\n");
        }
        return errori.toString();
    }


}
