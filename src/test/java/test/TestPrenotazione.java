package test;

import controllers.applicativo.AppuntamentoController;
import engclasses.beans.AppuntamentoBean;
import engclasses.dao.api.AppuntamentoDAO;
import engclasses.dao.factory.DAOFactory;
import misc.PersistenceType;
import misc.Session;
import misc.StatoAppuntamento;
import misc.TipoConsulenza;
import model.Agricoltore;
import model.Consulente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class per Annuncio
 * Autore del test: Mohamed Boussaidi
 */

class TestPrenotazione {

    private static final String TEST_CLIENT_ID = "agricoltore1";
    private static final String TEST_CONSULTANT_ID = "consulente1";

    private AppuntamentoController controller;
    private Session session;
    private static AppuntamentoDAO appuntamentoDAO;

    @BeforeEach
    void setUp() {
        session = Session.getInstance();
        session.clearSession();
        session.setPersistenceType(PersistenceType.MEMORY);

        if (appuntamentoDAO == null) {
            DAOFactory factory = DAOFactory.getFactory(PersistenceType.MEMORY);
            appuntamentoDAO = factory.getAppuntamentoDAO();
        } else {
            if (appuntamentoDAO instanceof engclasses.dao.memory.AppuntamentoDAOMemory) {
                ((engclasses.dao.memory.AppuntamentoDAOMemory) appuntamentoDAO).clear();
            }
        }

        // Simula utente agricoltore loggato
        Agricoltore agricoltore = new Agricoltore(
                TEST_CLIENT_ID,
                "mario_rossi",
                "mario@example.com",
                "password123",
                "Mario",
                "Rossi",
                "Roma"
        );
        session.setUtenteLoggato(agricoltore);

        controller = new AppuntamentoController(session);
    }

    // ✅ TEST 1: Creazione appuntamento ONLINE valido
    @Test
    void testCreaAppuntamento_Online() {
        AppuntamentoBean bean = creaBean(TipoConsulenza.ONLINE, 
            LocalDate.now().plusDays(3), LocalTime.of(10, 0), LocalTime.of(11, 0), null);
        
        AppuntamentoBean risultato = controller.prenotaAppuntamento(bean);

        assertNotNull(risultato.getIdAppuntamento());
        assertEquals(TipoConsulenza.ONLINE, risultato.getTipoConsulenza());
        assertEquals(StatoAppuntamento.PRENOTATO, risultato.getStato());
        assertEquals(60, risultato.getDurataMinuti());
    }

    // ✅ TEST 2: Creazione appuntamento IN_UFFICIO valido
    @Test
    void testCreaAppuntamento_InUfficio() {
        AppuntamentoBean bean = creaBean(TipoConsulenza.IN_UFFICIO, 
            LocalDate.now().plusDays(5), LocalTime.of(14, 30), LocalTime.of(16, 0), 
            "Via Roma 123, Milano");
        
        AppuntamentoBean risultato = controller.prenotaAppuntamento(bean);

        assertEquals("Via Roma 123, Milano", risultato.getLuogo());
        assertEquals(StatoAppuntamento.PRENOTATO, risultato.getStato());
    }

    // ✅ TEST 3: Creazione appuntamento SUL_CAMPO valido
    @Test
    void testCreaAppuntamento_SulCampo() {
        AppuntamentoBean bean = creaBean(TipoConsulenza.SUL_CAMPO, 
            LocalDate.now().plusDays(7), LocalTime.of(9, 0), LocalTime.of(12, 0), 
            "Azienda Agricola Verdi");
        
        AppuntamentoBean risultato = controller.prenotaAppuntamento(bean);

        assertEquals(TipoConsulenza.SUL_CAMPO, risultato.getTipoConsulenza());
        assertEquals(180, risultato.getDurataMinuti());
    }

    // ❌ TEST 4: Creazione appuntamento senza luogo per IN_UFFICIO - deve fallire
    @Test
    void testCreaAppuntamento_SenzaLuogo() {
        AppuntamentoBean bean = creaBean(TipoConsulenza.IN_UFFICIO, 
            LocalDate.now().plusDays(3), LocalTime.of(10, 0), LocalTime.of(11, 0), null);

        assertThrows(IllegalArgumentException.class, () -> {
            controller.prenotaAppuntamento(bean);
        });
    }

    // ❌ TEST 5: Creazione appuntamento con data passata - deve fallire
    @Test
    void testCreaAppuntamento_DataPassata() {
        AppuntamentoBean bean = creaBean(TipoConsulenza.ONLINE, 
            LocalDate.now().minusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0), null);

        assertThrows(IllegalArgumentException.class, () -> {
            controller.prenotaAppuntamento(bean);
        });
    }

    // ✅ TEST 6: Cancellazione appuntamento da parte del cliente
    @Test
    void testCancellaAppuntamento_Cliente() {
        // Crea appuntamento
        AppuntamentoBean bean = creaBean(TipoConsulenza.ONLINE, 
            LocalDate.now().plusDays(3), LocalTime.of(10, 0), LocalTime.of(11, 0), null);
        AppuntamentoBean creato = controller.prenotaAppuntamento(bean);

        // Cancella
        boolean risultato = controller.cancellaAppuntamentoCliente(
            creato.getIdAppuntamento(), "Imprevisto");

        assertTrue(risultato);
        
        // Verifica stato
        AppuntamentoBean aggiornato = controller.getAppuntamento(creato.getIdAppuntamento());
        assertEquals(StatoAppuntamento.CANCELLATO_CLIENTE, aggiornato.getStato());
        assertEquals("Imprevisto", aggiornato.getMotivoCancellazione());
    }

    // ✅ TEST 7: Cancellazione appuntamento da parte del consulente
    @Test
    void testCancellaAppuntamento_Consulente() {
        // Crea appuntamento
        AppuntamentoBean bean = creaBean(TipoConsulenza.ONLINE, 
            LocalDate.now().plusDays(3), LocalTime.of(10, 0), LocalTime.of(11, 0), null);
        AppuntamentoBean creato = controller.prenotaAppuntamento(bean);

        // Simula login consulente
        Consulente consulente = new Consulente(TEST_CONSULTANT_ID, "consulente", 
            "cons@test.com", "pass", "Con", "Sulente", "Roma");
        session.setUtenteLoggato(consulente);
        AppuntamentoController controllerConsulente = new AppuntamentoController(session);

        // Cancella
        boolean risultato = controllerConsulente.cancellaAppuntamentoConsulente(
            creato.getIdAppuntamento(), "Malattia");

        assertTrue(risultato);
        
        AppuntamentoBean aggiornato = controller.getAppuntamento(creato.getIdAppuntamento());
        assertEquals(StatoAppuntamento.CANCELLATO_CONSULENTE, aggiornato.getStato());
    }

    // ✅ TEST 8: Conferma appuntamento
    @Test
    void testConfermaAppuntamento() {
        // Crea appuntamento
        AppuntamentoBean bean = creaBean(TipoConsulenza.ONLINE, 
            LocalDate.now().plusDays(3), LocalTime.of(10, 0), LocalTime.of(11, 0), null);
        AppuntamentoBean creato = controller.prenotaAppuntamento(bean);

        // Conferma
        boolean risultato = controller.confermaAppuntamento(creato.getIdAppuntamento());

        assertTrue(risultato);
        
        AppuntamentoBean aggiornato = controller.getAppuntamento(creato.getIdAppuntamento());
        assertEquals(StatoAppuntamento.CONFERMATO, aggiornato.getStato());
    }

    // ✅ TEST 9: Recupero appuntamenti utente corrente
    @Test
    void testGetAppuntamentiUtente() {
        // Crea 2 appuntamenti
        controller.prenotaAppuntamento(creaBean(TipoConsulenza.ONLINE, 
            LocalDate.now().plusDays(3), LocalTime.of(10, 0), LocalTime.of(11, 0), null));
        controller.prenotaAppuntamento(creaBean(TipoConsulenza.IN_UFFICIO, 
            LocalDate.now().plusDays(5), LocalTime.of(14, 0), LocalTime.of(15, 0), "Milano"));

        List<AppuntamentoBean> appuntamenti = controller.getAppuntamentiUtenteCorrente();

        assertEquals(2, appuntamenti.size());
    }

    // ✅ TEST 10: Recupero appuntamenti per consulente
    @Test
    void testGetAppuntamentiConsulente() {
        // Crea appuntamenti
        controller.prenotaAppuntamento(creaBean(TipoConsulenza.ONLINE, 
            LocalDate.now().plusDays(3), LocalTime.of(10, 0), LocalTime.of(11, 0), null));

        List<AppuntamentoBean> appuntamenti = controller.getAppuntamentiConsulente(TEST_CONSULTANT_ID);

        assertEquals(1, appuntamenti.size());
        assertEquals(TEST_CONSULTANT_ID, appuntamenti.get(0).getIdConsulente());
    }

    // Metodo helper
    private AppuntamentoBean creaBean(TipoConsulenza tipo, LocalDate data, 
                                      LocalTime inizio, LocalTime fine, String luogo) {
        AppuntamentoBean bean = new AppuntamentoBean();
        bean.setIdCliente(TEST_CLIENT_ID);
        bean.setIdConsulente(TEST_CONSULTANT_ID);
        bean.setTipoConsulenza(tipo);
        bean.setData(data);
        bean.setOraInizio(inizio);
        bean.setOraFine(fine);
        bean.setLuogo(luogo);
        bean.setNote("Test automatico");
        return bean;
    }
}
