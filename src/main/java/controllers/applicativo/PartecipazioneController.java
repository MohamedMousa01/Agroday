package controllers.applicativo;

import engclasses.beans.AnnuncioBean;
import engclasses.beans.PartecipazioneBean;
import engclasses.dao.api.AnnuncioDAO;
import engclasses.dao.api.PartecipazioneDAO;
import engclasses.dao.factory.DAOFactory;
import misc.PersistenceType;
import misc.Session;
import model.Annuncio;
import model.Partecipazione;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller applicativo per gestire le partecipazioni agli annunci.
 */
public class PartecipazioneController {

    private final PartecipazioneDAO partecipazioneDAO;
    private final AnnuncioDAO annuncioDAO;

    public PartecipazioneController() {
        PersistenceType persistenceType = Session.getInstance().getPersistenceType();
        DAOFactory factory = DAOFactory.getFactory(persistenceType);
        this.partecipazioneDAO = factory.getPartecipazioneDAO();
        this.annuncioDAO = factory.getAnnuncioDAO();
    }

    /**
     * Permette a un agricoltore di partecipare a un annuncio di gruppo.
     *
     * @param idAnnuncio ID dell'annuncio
     * @param idAgricoltore Username dell'agricoltore
     * @param quantita Quantità richiesta dall'agricoltore
     * @return true se la partecipazione ha successo
     * @throws IllegalArgumentException se i parametri non sono validi
     * @throws IllegalStateException se l'annuncio è scaduto o l'agricoltore ha già partecipato
     */
    public boolean partecipa(String idAnnuncio, String idAgricoltore, int quantita) {
        
        // Validazione
        if (idAnnuncio == null || idAnnuncio.isBlank()) {
            throw new IllegalArgumentException("ID annuncio non valido");
        }
        if (idAgricoltore == null || idAgricoltore.isBlank()) {
            throw new IllegalArgumentException("ID agricoltore non valido");
        }
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere maggiore di zero");
        }

        // Verifica che l'annuncio esista
        Annuncio annuncio = annuncioDAO.trovaPerId(idAnnuncio);
        if (annuncio == null) {
            throw new IllegalArgumentException("Annuncio non trovato");
        }

        // Verifica che l'annuncio sia attivo
        if (annuncio.isScaduto()) {
            throw new IllegalStateException("L'annuncio è scaduto. Non è possibile partecipare.");
        }

        // Verifica che l'agricoltore non sia l'autore dell'annuncio
        if (annuncio.getAutore().equals(idAgricoltore)) {
            throw new IllegalStateException("Non puoi partecipare al tuo stesso annuncio");
        }

        // Verifica che l'agricoltore non abbia già partecipato
        if (partecipazioneDAO.haPartecipatoGia(idAnnuncio, idAgricoltore)) {
            throw new IllegalStateException("Hai già partecipato a questo annuncio");
        }

        // Crea la partecipazione
        Partecipazione partecipazione = new Partecipazione(idAnnuncio, idAgricoltore, quantita);
        
        return partecipazioneDAO.salva(partecipazione);
    }

    /**
     * Ottiene tutte le partecipazioni di un annuncio.
     *
     * @param idAnnuncio ID dell'annuncio
     * @return Lista di bean delle partecipazioni
     */
    public List<PartecipazioneBean> getPartecipazioniAnnuncio(String idAnnuncio) {
        List<Partecipazione> partecipazioni = partecipazioneDAO.trovaPerAnnuncio(idAnnuncio);
        List<PartecipazioneBean> beans = new ArrayList<>();

        for (Partecipazione p : partecipazioni) {
            beans.add(convertToBean(p));
        }

        return beans;
    }

    /**
     * Ottiene tutte le partecipazioni di un agricoltore.
     *
     * @param usernameAgricoltore Username dell'agricoltore
     * @return Lista di bean delle partecipazioni
     */
    public List<PartecipazioneBean> getPartecipazioniUtente(String usernameAgricoltore) {
        List<Partecipazione> partecipazioni = partecipazioneDAO.trovaPerAgricoltore(usernameAgricoltore);
        List<PartecipazioneBean> beans = new ArrayList<>();

        for (Partecipazione p : partecipazioni) {
            beans.add(convertToBean(p));
        }

        return beans;
    }

    /**
     * Calcola la quantità totale richiesta per un annuncio (autore + partecipanti).
     *
     * @param idAnnuncio ID dell'annuncio
     * @return Quantità totale
     */
    public int getQuantitaTotale(String idAnnuncio) {
        Annuncio annuncio = annuncioDAO.trovaPerId(idAnnuncio);
        if (annuncio == null) {
            return 0;
        }

        List<Partecipazione> partecipazioni = partecipazioneDAO.trovaPerAnnuncio(idAnnuncio);
        
        // Quantità autore + somma partecipanti
        int totale = annuncio.getQuantitaDesiderata();
        for (Partecipazione p : partecipazioni) {
            totale += p.getQuantitaRichiesta();
        }

        return totale;
    }

    /**
     * Verifica se un agricoltore ha già partecipato a un annuncio.
     *
     * @param idAnnuncio ID dell'annuncio
     * @param idAgricoltore Username dell'agricoltore
     * @return true se ha già partecipato
     */
    public boolean haGiaPartecipato(String idAnnuncio, String idAgricoltore) {
        return partecipazioneDAO.haPartecipatoGia(idAnnuncio, idAgricoltore);
    }

    /**
     * Ottiene il numero di partecipanti a un annuncio (INCLUSO l'autore).
     *
     * @param idAnnuncio ID dell'annuncio
     * @return Numero di partecipanti (autore + altri agricoltori)
     */
    public int getNumeroPartecipanti(String idAnnuncio) {
        // Conta le partecipazioni esplicite + 1 (l'autore dell'annuncio)
        return partecipazioneDAO.trovaPerAnnuncio(idAnnuncio).size() + 1;
    }

    // Metodo privato per convertire Partecipazione a Bean
    private PartecipazioneBean convertToBean(Partecipazione p) {
        return new PartecipazioneBean(
            p.getIdPartecipazione(),
            p.getIdAnnuncio(),
            p.getIdAgricoltore(),
            p.getQuantitaRichiesta()
        );
    }
}
