package test;

import controllers.applicativo.AnnuncioController;
import engclasses.beans.AnnuncioBean;
import engclasses.dao.api.AnnuncioDAO;
import engclasses.dao.factory.DAOFactory;
import engclasses.exceptions.AnnuncioNonValidoException;
import misc.PersistenceType;
import misc.Session;
import misc.StatoAnnuncio;
import model.Annuncio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class per AnnuncioController
 * Autore del test: Mohamed Mousa Ali Sarhan
 */
class TestAnnuncioController {

    private AnnuncioController annuncioController;
    private Session session;
    private static AnnuncioDAO annuncioDAO;

    @BeforeEach
    void setUp() {
        session = Session.getInstance();
        session.clearSession();
        session.setPersistenceType(PersistenceType.MEMORY);
        
        if (annuncioDAO == null) {
            DAOFactory factory = DAOFactory.getFactory(PersistenceType.MEMORY);
            annuncioDAO = factory.getAnnuncioDAO();
        } else {
            if (annuncioDAO instanceof engclasses.dao.memory.AnnuncioDAOMemory) {
                ((engclasses.dao.memory.AnnuncioDAOMemory) annuncioDAO).clear();
            }
        }
        
        annuncioController = new AnnuncioController();
    }

    // ✅ TEST 1: Creazione annuncio con dati validi
    @Test
    void testCreaAnnuncio_DatiValidi() throws AnnuncioNonValidoException {
        annuncioController.creaAnnuncio("venditore1", "Vendita Trattore", "Trattore usato", 
            LocalDate.now().plusDays(30), "Trattore", 1, "Roma");

        List<Annuncio> annunci = annuncioDAO.trovaTutti();
        assertEquals(1, annunci.size());
        assertEquals("venditore1", annunci.get(0).getAutore());
    }

    // ❌ TEST 2: Creazione annuncio con dati non validi
    @Test
    void testCreaAnnuncio_DatiNonValidi() {
        assertThrows(AnnuncioNonValidoException.class, () -> {
            annuncioController.creaAnnuncio("venditore1", "", "Desc", 
                LocalDate.now().plusDays(30), "Prodotto", 10, "Roma");
        });
    }

    // ❌ TEST 3: Creazione annuncio con data scadenza passata
    @Test
    void testCreaAnnuncio_DataScadenzaPassata() {
        assertThrows(AnnuncioNonValidoException.class, () -> {
            annuncioController.creaAnnuncio("venditore1", "Titolo", "Desc", 
                LocalDate.now().minusDays(5), "Prodotto", 10, "Roma");
        });
    }

    // ✅ TEST 4: Recupero tutti gli annunci
    @Test
    void testGetAnnunci_ConAnnunci() throws AnnuncioNonValidoException {
        annuncioController.creaAnnuncio("venditore1", "Annuncio 1", "Desc 1", 
            LocalDate.now().plusDays(30), "Prodotto 1", 10, "Roma");
        annuncioController.creaAnnuncio("venditore2", "Annuncio 2", "Desc 2", 
            LocalDate.now().plusDays(20), "Prodotto 2", 5, "Milano");

        List<AnnuncioBean> annunci = annuncioController.getAnnunci();
        assertEquals(2, annunci.size());
    }

    // ✅ TEST 5: Recupero annunci per utente
    @Test
    void testGetAnnunciUtente() throws AnnuncioNonValidoException {
        annuncioController.creaAnnuncio("venditore1", "Annuncio 1", "Desc", 
            LocalDate.now().plusDays(30), "Prodotto", 10, "Roma");
        annuncioController.creaAnnuncio("venditore2", "Annuncio 2", "Desc", 
            LocalDate.now().plusDays(20), "Prodotto", 5, "Milano");

        List<AnnuncioBean> annunci = annuncioController.getAnnunciUtente("venditore1");
        assertEquals(1, annunci.size());
        assertEquals("venditore1", annunci.get(0).getAutore());
    }

    // ✅ TEST 6: Eliminazione annuncio esistente
    @Test
    void testEliminaAnnuncio() throws AnnuncioNonValidoException {
        annuncioController.creaAnnuncio("venditore1", "Annuncio", "Desc", 
            LocalDate.now().plusDays(30), "Prodotto", 10, "Roma");
        
        String idAnnuncio = annuncioDAO.trovaTutti().get(0).getIdAnnuncio();
        boolean risultato = annuncioController.eliminaAnnuncio(idAnnuncio);

        assertTrue(risultato);
        assertEquals(0, annuncioDAO.trovaTutti().size());
    }

    // ✅ TEST 7: Verifica stato ATTIVO
    @Test
    void testGetAnnunci_StatoAttivo() throws AnnuncioNonValidoException {
        annuncioController.creaAnnuncio("venditore1", "Annuncio attivo", "Desc", 
            LocalDate.now().plusDays(30), "Prodotto", 10, "Roma");

        List<AnnuncioBean> annunci = annuncioController.getAnnunci();
        assertEquals(StatoAnnuncio.ATTIVO, annunci.get(0).getStato());
    }

    // ✅ TEST 8: Verifica stato SCADUTO
    @Test
    void testGetAnnunci_StatoScaduto() {
        Annuncio annuncioScaduto = new Annuncio.Builder("venditore1", "Annuncio scaduto", "Desc")
            .dataScadenza(LocalDate.now().minusDays(5))
            .citta("Roma")
            .quantitaDesiderata(10)
            .build();
        annuncioDAO.salva(annuncioScaduto);

        List<AnnuncioBean> annunci = annuncioController.getAnnunci();
        assertEquals(StatoAnnuncio.SCADUTO, annunci.get(0).getStato());
    }
}
