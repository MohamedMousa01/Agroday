package controllers.applicativo;


import engclasses.beans.RegistrazioneBean;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.factory.DAOFactory;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.exceptions.RegistrazioneFallitaException;
import engclasses.pattern.factory.UtenteFactory;
import engclasses.pattern.factory.UtenteFactoryProvider;
import misc.PersistenceType;
import misc.Session;
import misc.TipoUtente;
import model.Utente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.UUID;

public class RegistrazioneController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrazioneController.class);

    private final Session session;

    public RegistrazioneController(Session session) {
        this.session = session;
    }

    public boolean registraUtente(RegistrazioneBean bean) throws RegistrazioneFallitaException, DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {

        // Genera un ID univoco per l'utente
        String idUtente = UUID.randomUUID().toString();
        bean.setIdUtente(idUtente);

        String errori = validaRegistrazione(bean);

        //validazione di dati
        if (!errori.isEmpty()) {                              //se ci sono errori lancio eccezione
            throw new RegistrazioneFallitaException(errori);
        }


        verificaUnicita(bean);


        //capisco con che tipo di utente ho a che fare, e quale tipo di persistenza è stata scelta
                    TipoUtente tipoUtente = bean.getTipoUtente();
                    PersistenceType tipoPersistenza = bean.getPersistenceType();
                    logger.debug("RegistrazioneController - TipoUtente: {}, Persistenza: {}", tipoUtente, tipoPersistenza);


                    // creo la factory giusta dell'utente che mi serve
                    UtenteFactory factory = UtenteFactoryProvider.getFactory(tipoUtente);
                    Utente utente = factory.creaUtente(idUtente, bean);
                    logger.debug("RegistrazioneController - Utente creato: {}", utente.getUsername());
        
                    // Salvo in Sessione l'utente e il tipo di persistenza
                    salvaDatiSessione(utente, tipoPersistenza);
                    logger.debug("RegistrazioneController - Dati sessione salvati.");
        
                    // Ora lavoro nella DAO
                    DAOFactory factoryDAO = DAOFactory.getFactory(session.getPersistenceType());
                    UtenteDAO dao = factoryDAO.getUtenteDAO(tipoUtente);

                    dao.aggiungiUtente(utente, session.getPersistenceType());

        return true;

    }

    private void salvaDatiSessione(Utente utente, PersistenceType tipoPersistenza) {
        session.setUtenteLoggato(utente);
        session.setPersistenceType(tipoPersistenza);
        // in caso mi dovesse servire aggiungo ----> session.setTipoUtente(TipoUtente utente);   prima però aggiungi il setter in Session
    }

    public String validaRegistrazione(RegistrazioneBean bean) {
        StringBuilder errori = new StringBuilder();

        // Validazione campi
        validaNome(bean.getNome(), errori);
        validaCognome(bean.getCognome(), errori);
        validaUsername(bean.getUsername(), errori);
        validaPassword(bean.getPassword(), bean.getConfirmPassword(), errori);
        validaEmail(bean.getEmail(), errori);

        return errori.toString();
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
    private void validaUsername(String username, StringBuilder errori) {
        if (username == null || username.trim().isEmpty()) {
            errori.append("L'username non può essere vuoto.\n");
        } else if (!username.matches("^\\w{4,20}$")) {
            errori.append("Username non valido (4-20 caratteri alfanumerici).\n");
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
    private void validaEmail(String email, StringBuilder errori) {
        if (email == null || email.trim().isEmpty()) {
            errori.append("L'email non può essere vuota.\n");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            errori.append("L'email non è valida.\n");
        }
    }


    private void verificaUnicita(RegistrazioneBean bean)
            throws DatabaseConnessioneFallitaException,
            DatabaseOperazioneFallitaException,
            RegistrazioneFallitaException {


            DAOFactory factory = DAOFactory.getFactory(bean.getPersistenceType());
            UtenteDAO utenteDAO = factory.getUtenteDAO(bean.getTipoUtente());


            if (utenteDAO.esisteUsername(bean.getUsername())) {
                throw new RegistrazioneFallitaException("Username già in uso.");
            }

            if (utenteDAO.esisteEmail(bean.getEmail())) {
                throw new RegistrazioneFallitaException("Email già in uso.");
            }


    }
}
