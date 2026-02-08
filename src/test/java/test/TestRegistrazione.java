package test;

import controllers.applicativo.RegistrazioneController;
import org.junit.jupiter.api.Test;

import engclasses.beans.RegistrazioneBean;
import misc.Session;

import static misc.Session.getInstance;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Autore del test : Mohamed Mousa Ali Sarhan
 */


public class TestRegistrazione {

    @Test
    void testValidaRegistrazione_DatiValidi() {
        Session session = getInstance();
        session.clearSession();

        RegistrazioneController controller = new RegistrazioneController(session);

        RegistrazioneBean bean = new RegistrazioneBean();
        bean.setNome("Mario");
        bean.setCognome("Rossi");
        bean.setUsername("mario123");
        bean.setPassword("password");
        bean.setConfirmPassword("password");
        bean.setEmail("mario@example.com");

        String errori = controller.validaRegistrazione(bean);

        assertTrue(errori.isEmpty());
    }

    @Test
    void testValidaRegistrazione_DatiInvalidi() {
        Session session = getInstance();
        session.clearSession();
        RegistrazioneController controller = new RegistrazioneController(session);

        RegistrazioneBean bean = new RegistrazioneBean();
        bean.setNome("");
        bean.setCognome("");
        bean.setUsername("a");
        bean.setPassword("pass");
        bean.setConfirmPassword("different");
        bean.setEmail("invalid");

        String errori = controller.validaRegistrazione(bean);

        assertFalse(errori.isEmpty());
    }
}
