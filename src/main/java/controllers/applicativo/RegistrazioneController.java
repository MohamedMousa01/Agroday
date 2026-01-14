package main.java.controllers.applicativo;


import main.java.engclasses.beans.RegistrazioneBean;
import main.java.engclasses.dao.AgricoltoreDAO;
import main.java.engclasses.dao.UtenteDAOFactory;
import main.java.engclasses.dao.VenditoreDAO;
import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.engclasses.exceptions.RegistrazioneFallitaException;
import main.java.engclasses.pattern.Factory.UtenteFactory;
import main.java.engclasses.pattern.Factory.UtenteFactoryProvider;
import main.java.misc.Session;
import main.java.model.Agricoltore;
import main.java.model.Utente;
import main.java.model.Venditore;

import java.lang.runtime.SwitchBootstraps;
import java.util.UUID;

public class RegistrazioneController {

    private final Session session;
    private String idUtente;

    public RegistrazioneController(Session session){
        this.session = session;
    }

    public boolean registraUtente(RegistrazioneBean bean, boolean persistence) throws RegistrazioneFallitaException, DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {

        // Genera un ID univoco per l'utente
        this.idUtente = UUID.randomUUID().toString();

        String errori = validaRegistrazione(bean);
        //validazione di dati
        if(!errori.isEmpty()){                              //se ci sono errori lancio eccezione
            throw new RegistrazioneFallitaException(errori);
        }

        // creo la factory giusta dell'utente che mi serve
        UtenteFactory factory = UtenteFactoryProvider.getFactory(session);
        Utente utente = factory.creaUtente(idUtente, bean);

        salvaDatiSessione(utente);

        UtenteDAOFactory.getDAO(utente).aggiungiUtente(utente, persistence);   //chiamo la Factory che mi crea la DAO per l'utente che voglio

        return true;


//        if(session.isAgricoltore()){          //se l'accesso è dell'agricoltore associo ad utente model Agricoltore
//            utente = new Agricoltore(idUtente, bean.getNome(), bean.getCognome(), bean.getUsername(), bean.getEmail(), bean.getPassword(), bean.getCitta());
//            salvaDatiSessione(utente);
//            AgricoltoreDAO.aggiungiAgricoltore(utente, persistence);
//        } else {                            //altrimenti creo model Venditore
//            utente = new Venditore(idUtente, bean.getNome(), bean.getCognome(), bean.getUsername(), bean.getEmail(), bean.getPassword());
//            salvaDatiSessione(utente);
//            VenditoreDAO.aggiungiVenditore(utente, persistence);
//        }
//        return true;


    }

    private void salvaDatiSessione(Utente utente) {
        session.setIdUtente(utente.getIdUtente());
        session.setUsername(utente.getUsername());
        session.setNome(utente.getNome());

        if (utente instanceof Agricoltore agr) {
            session.setCognomeAgricoltore(agr.getCognome());
            session.setNomeAgricoltore(agr.getNome());
        }
    }

    public String validaRegistrazione(RegistrazioneBean bean) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        StringBuilder errori = new StringBuilder();

        // Recupera utente corrente
        recuperaUtente();

        // Validazione campi
        validaNome(bean.getNome(), errori);
        validaCognome(bean.getCognome(), errori);
        validaUsername(bean.getUsername(), errori);
        validaPassword(bean.getPassword(), bean.getConfirmPassword(), errori);
        validaEmail(bean.getEmail(), errori);

//        if (session.isAgricoltore()) {
//            validaTitoloDiStudio(bean.getTitoloDiStudio(), errori);
//        }
        return errori.toString();
    }

    // Metodo per recuperare l'utente corrente
    private void recuperaUtente() throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        if (session.isAgricoltore()) {
            AgricoltoreDAO.selezionaAgricoltore("idUtente", idUtente, session.isPersistence());
        } else {
            VenditoreDAO.selezionaVenditore("idUtente", idUtente, session.isPersistence());
        }
    }

    // Metodo per validare il nome
    private void validaNome(String nome, StringBuilder errori) {
        if (nome == null || nome.trim().isEmpty()) {
            errori.append("Il nome non può essere vuoto.\n");
        } else if (!nome.matches("^[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\s]{2,30}$")) {
            errori.append("Il nome deve contenere solo lettere, spazi e apostrofi (2-30 caratteri).\n");
        }
    }

    // Metodo per validare il cognome
    private void validaCognome(String cognome, StringBuilder errori) {
        if (cognome == null || cognome.trim().isEmpty()) {
            errori.append("Il cognome non può essere vuoto.\n");
        } else if (!cognome.matches("^[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\s]{2,30}$")) {
            errori.append("Il cognome deve contenere solo lettere, spazi e apostrofi (2-30 caratteri).\n");
        }
    }

    // Metodo per validare l'username
    private void validaUsername(String username, StringBuilder errori) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        if (username == null || username.trim().isEmpty()) {
            errori.append("L'username non può essere vuoto.\n");
        } else if (controllaCampo("username", username)) {
            errori.append("L'username è già in uso.\n");
        }
    }

    //  Metodo per validare la password
    private void validaPassword(String password, String confirmPassword, StringBuilder errori) {
        if (password == null || password.trim().isEmpty()) {
            errori.append("La password non può essere vuota.\n");
        }
        if (password != null && !password.equals(confirmPassword)) {
            errori.append("Le password non corrispondono.\n");
        }
    }

    // Metodo per validare l'email
    private void validaEmail(String email, StringBuilder errori) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        if (email == null || email.trim().isEmpty()) {
            errori.append("L'email non può essere vuota.\n");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            errori.append("L'email non è valida.\n");
        } else if (controllaCampo("email", email)) {
            errori.append("L'email è già in uso.\n");
        }
    }

    public boolean controllaCampo(String campo, String valore) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        // Recupera l'utente (Organizzatore o Partecipante) in base al tipo di sessione
        Utente utente = session.isAgricoltore()
                ? AgricoltoreDAO.selezionaAgricoltore(campo, valore, session.isPersistence())
                : VenditoreDAO.selezionaVenditore(campo, valore, session.isPersistence());

        // Se l'utente non esiste, il campo è disponibile
        return utente != null;
    }


}

