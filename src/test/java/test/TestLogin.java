package test;

import controllers.applicativo.LoginController;
import engclasses.dao.UtenteDAOFactory;
import engclasses.dao.api.UtenteDAO;
import engclasses.exceptions.LoginFallitoException;
import misc.PersistenceType;
import misc.Session;
import model.Agricoltore;
import model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Autore del test : Mohamed Mousa Ali Sarhan
 */

class TestLogin {

    private LoginController loginController;
    private Session session;

    @BeforeEach
    void setUp() throws Exception {
        session = Session.getInstance();
        session.clearSession();

        // Aggiungi un utente di test nel DAO in memoria
        UtenteDAO utenteDAO = UtenteDAOFactory.getUtenteDAO(PersistenceType.MEMORY);
        Utente utenteTest = new Agricoltore("user11", "user11", "user11@test.com", "111", "Mario", "Rossi", "Roma");
        utenteDAO.aggiungiUtente(utenteTest, PersistenceType.MEMORY);

        loginController = new LoginController(session);
    }

    // ✅ TEST 1: login valido
    @Test
    void loginConCredenzialiValide() throws Exception {
        Utente utente = loginController.login("user11", "111");

        assertNotNull(utente);
        assertEquals("user11", utente.getUsername());
        assertNotNull(session.getUtenteLoggato());
        assertEquals(utente, session.getUtenteLoggato());
    }

    // ❌ TEST 2: credenziali errate
    @Test
    void loginConCredenzialiErrate() throws Exception {
        Utente utente = loginController.login("utente_sbagliato", "password_sbagliata");

        assertNull(utente);
        assertNull(session.getUtenteLoggato());
    }

    // ❌ TEST 3: username nullo
    @Test
    void loginConUsernameNull() {
        assertThrows(LoginFallitoException.class, () -> {
            loginController.login(null, "111");
        });
    }

    // ❌ TEST 4: password nulla
    @Test
    void loginConPasswordNull() {
        assertThrows(LoginFallitoException.class, () -> {
            loginController.login("user11", null);
        });
    }

    // ✅ TEST 5: autentica() valida
    @Test
    void autenticaConCredenzialiValide() throws Exception {
        boolean risultato = loginController.autentica("user11", "111");

        assertTrue(risultato);
        assertNotNull(session.getUtenteLoggato());
    }

    // ❌ TEST 6: autentica con persistenza non impostata
    @Test
    void autenticaSenzaPersistenceType() {
        session.setPersistenceType(null);

        assertThrows(LoginFallitoException.class, () -> {
            loginController.autentica("user11", "111");
        });
    }
}
