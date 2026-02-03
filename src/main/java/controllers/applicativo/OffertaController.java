package controllers.applicativo;

import engclasses.dao.api.OffertaDAO;
import engclasses.dao.factory.DAOFactory;
import misc.PersistenceType;
import misc.Session;
import model.Offerta;

import java.util.List;

/**
 * Controller applicativo per gestire le offerte dei venditori.
 */
public class OffertaController {

    private final OffertaDAO offertaDAO;

    public OffertaController() {
        PersistenceType persistenceType = Session.getInstance().getPersistenceType();
        DAOFactory factory = DAOFactory.getFactory(persistenceType);
        this.offertaDAO = factory.getOffertaDAO();
    }

    /**
     * Crea e salva una nuova offerta.
     *
     * @param idAnnuncio ID dell'annuncio
     * @param usernameVenditore Username del venditore
     * @param prezzoAlKg Prezzo proposto al kg
     * @param quantitaTotale Quantità totale dell'annuncio
     * @return true se l'offerta è stata salvata con successo
     */
    public boolean creaOfferta(String idAnnuncio, String usernameVenditore, double prezzoAlKg, int quantitaTotale) {
        
        if (prezzoAlKg <= 0) {
            throw new IllegalArgumentException("Il prezzo deve essere maggiore di zero");
        }
        
        double prezzoTotale = prezzoAlKg * quantitaTotale;
        
        Offerta offerta = new Offerta(idAnnuncio, usernameVenditore, prezzoAlKg, prezzoTotale);
        
        return offertaDAO.salva(offerta);
    }

    /**
     * Ottiene tutte le offerte di un venditore.
     *
     * @param usernameVenditore Username del venditore
     * @return Lista delle offerte
     */
    public List<Offerta> getOfferteVenditore(String usernameVenditore) {
        return offertaDAO.trovaPerVenditore(usernameVenditore);
    }

    /**
     * Ottiene le offerte in attesa (PENDING) di un venditore.
     *
     * @param usernameVenditore Username del venditore
     * @return Lista delle offerte in attesa
     */
    public List<Offerta> getOffertePendingVenditore(String usernameVenditore) {
        return offertaDAO.trovaOffertePendingPerVenditore(usernameVenditore);
    }

    /**
     * Ottiene tutte le offerte per un annuncio.
     *
     * @param idAnnuncio ID dell'annuncio
     * @return Lista delle offerte
     */
    public List<Offerta> getOfferteAnnuncio(String idAnnuncio) {
        return offertaDAO.trovaPerAnnuncio(idAnnuncio);
    }

    /**
     * Aggiorna lo stato di un'offerta.
     *
     * @param idOfferta ID dell'offerta
     * @param nuovoStato Nuovo stato (ACCETTATA, RIFIUTATA)
     * @return true se l'aggiornamento ha successo
     */
    public boolean aggiornaStatoOfferta(String idOfferta, String nuovoStato) {
        return offertaDAO.aggiornaStato(idOfferta, nuovoStato);
    }

    /**
     * Conta il numero di offerte in attesa per un venditore.
     *
     * @param usernameVenditore Username del venditore
     * @return Numero di offerte in attesa
     */
    public int contaOffertePending(String usernameVenditore) {
        return getOffertePendingVenditore(usernameVenditore).size();
    }

    /**
     * Ottiene tutte le offerte ricevute da un agricoltore sui suoi annunci.
     *
     * @param usernameAgricoltore Username dell'agricoltore
     * @return Lista delle offerte ricevute
     */
    public List<Offerta> getOfferteRicevute(String usernameAgricoltore) {
        return offertaDAO.trovaOfferteRicevutePerAgricoltore(usernameAgricoltore);
    }

    /**
     * Ottiene le offerte in attesa (PENDING) ricevute da un agricoltore.
     *
     * @param usernameAgricoltore Username dell'agricoltore
     * @return Lista delle offerte in attesa
     */
    public List<Offerta> getOfferteRicevutePending(String usernameAgricoltore) {
        return getOfferteRicevute(usernameAgricoltore).stream()
                .filter(Offerta::isPending)
                .toList();
    }

    /**
     * Accetta un'offerta (cambia stato a ACCETTATA).
     *
     * @param idOfferta ID dell'offerta da accettare
     * @return true se l'accettazione ha successo
     */
    public boolean accettaOfferta(String idOfferta) {
        return aggiornaStatoOfferta(idOfferta, "ACCETTATA");
    }

    /**
     * Rifiuta un'offerta (cambia stato a RIFIUTATA).
     *
     * @param idOfferta ID dell'offerta da rifiutare
     * @return true se il rifiuto ha successo
     */
    public boolean rifiutaOfferta(String idOfferta) {
        return aggiornaStatoOfferta(idOfferta, "RIFIUTATA");
    }
}
