package controllers.applicativo;

import engclasses.beans.AnnuncioBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import engclasses.dao.api.AnnuncioDAO;
import engclasses.dao.factory.DAOFactory;
import engclasses.exceptions.AnnuncioNonValidoException;
import misc.PersistenceType;
import misc.Session;
import misc.StatoAnnuncio;
import model.Annuncio;

public class AnnuncioController {

    private final AnnuncioDAO annuncioDAO;

    public AnnuncioController() {
        // Ottiene il tipo di persistenza dalla sessione corrente
        PersistenceType persistenceType = Session.getInstance().getPersistenceType();
        DAOFactory factory = DAOFactory.getFactory(persistenceType);
        this.annuncioDAO = factory.getAnnuncioDAO();
    }

    /**
     * Recupera tutti gli annunci
     * @return lista di AnnuncioBean
     */
    public List<AnnuncioBean> getAnnunci() {

        List<Annuncio> annunci = annuncioDAO.trovaTutti();      //recupero annunci dalla persistenza
        List<AnnuncioBean> annunciBean = new ArrayList<>();     //creo una lista dove inserire i bean degli annunci

        // Ottieni il DAO delle partecipazioni per calcolare i dati
        controllers.applicativo.PartecipazioneController partController = 
            new controllers.applicativo.PartecipazioneController();

        for (Annuncio annuncio : annunci){
            // Aggiorna lo stato se scaduto
            annuncio.aggiornaStato();
            
            AnnuncioBean bean = convertToBean(annuncio);   //converto gli annunci da model a bean
            
            // Imposta stato corretto (ATTIVO o SCADUTO)
            String stato = annuncio.isScaduto() ? StatoAnnuncio.SCADUTO : StatoAnnuncio.ATTIVO;
            bean.setStato(stato);
            bean.setQuantitaTotale(partController.getQuantitaTotale(annuncio.getIdAnnuncio()));
            bean.setNumeroPartecipanti(partController.getNumeroPartecipanti(annuncio.getIdAnnuncio()));
            
            annunciBean.add(bean);
        }

        return annunciBean;
    }

    /**
     * Ottiene gli annunci a cui un agricoltore ha partecipato.
     *
     * @param usernameAgricoltore Username dell'agricoltore
     * @return Lista di annunci a cui ha partecipato
     */
    public List<AnnuncioBean> getAnnunciPartecipazioniUtente(String usernameAgricoltore) {
        controllers.applicativo.PartecipazioneController partController = 
            new controllers.applicativo.PartecipazioneController();
        
        // Ottieni tutte le partecipazioni dell'utente
        List<engclasses.beans.PartecipazioneBean> partecipazioni = 
            partController.getPartecipazioniUtente(usernameAgricoltore);
        
        List<AnnuncioBean> annunciBean = new ArrayList<>();
        
        for (engclasses.beans.PartecipazioneBean part : partecipazioni) {
            // Recupera l'annuncio corrispondente
            model.Annuncio annuncio = annuncioDAO.trovaPerId(part.getIdAnnuncio());
            if (annuncio != null) {
                annuncio.aggiornaStato();
                AnnuncioBean bean = convertToBean(annuncio);
                
                String stato = annuncio.isScaduto() ? StatoAnnuncio.SCADUTO : StatoAnnuncio.ATTIVO;
                bean.setStato(stato);
                bean.setQuantitaTotale(partController.getQuantitaTotale(annuncio.getIdAnnuncio()));
                bean.setNumeroPartecipanti(partController.getNumeroPartecipanti(annuncio.getIdAnnuncio()));
                
                annunciBean.add(bean);
            }
        }
        
        return annunciBean;
    }
    
    /**
     * Converte un Annuncio (entity) in AnnuncioBean (DTO)
     */
    private AnnuncioBean convertToBean(Annuncio annuncio) {
        AnnuncioBean bean = new AnnuncioBean();
        bean.setIdAnnuncio(annuncio.getIdAnnuncio());
        bean.setAutore(annuncio.getAutore());
        bean.setTitolo(annuncio.getTitolo());
        bean.setDescrizione(annuncio.getDescrizione());
        bean.setDataCreazione(annuncio.getDataCreazione());
        bean.setQuantita(annuncio.getQuantita());
        bean.setCitta(annuncio.getCitta());
        bean.setDataScadenza(annuncio.getDataScadenza());
        bean.setPrezzo(annuncio.getPrezzo());
        return bean;
    }


    public void creaAnnuncio(
                    String autore,
                    String titolo,
                    String descrizione,
                    LocalDate dataScadenza,
                    String nomeProdotto,
                    int quantita,
                    String citta
            ) throws AnnuncioNonValidoException {

                validaDati(titolo, autore, dataScadenza, nomeProdotto, quantita, citta);

                LocalDate dataCreazione = LocalDate.now();

                // crea oggetto dominio usando il Builder
                Annuncio annuncio = new Annuncio.Builder(autore, titolo, descrizione)
                        .dataPubblicazione(dataCreazione)
                        .dataScadenza(dataScadenza)
                        .citta(citta)
                        .quantitaDesiderata(quantita)
                        .build(
                );

                // persistenza
                if (!annuncioDAO.salva(annuncio)) {
                    throw new AnnuncioNonValidoException("Errore nel salvataggio dell'annuncio");
                }
    }

    /**
     * Elimina un annuncio.
     *
     * @param idAnnuncio ID dell'annuncio da eliminare
     * @return true se l'eliminazione ha successo
     */
    public boolean eliminaAnnuncio(String idAnnuncio) {
        return annuncioDAO.elimina(idAnnuncio);
    }

    /**
     * Ottiene gli annunci creati da un utente.
     *
     * @param username Username dell'autore
     * @return Lista degli annunci creati dall'utente
     */
    public List<AnnuncioBean> getAnnunciUtente(String username) {
        List<Annuncio> annunci = annuncioDAO.trovaPerAutore(username);
        List<AnnuncioBean> annunciBean = new ArrayList<>();
        
        PartecipazioneController partController = new PartecipazioneController();
        
        for (Annuncio annuncio : annunci) {
            annuncio.aggiornaStato();
            AnnuncioBean bean = convertToBean(annuncio);
            
            String stato = annuncio.isScaduto() ? StatoAnnuncio.SCADUTO : StatoAnnuncio.ATTIVO;
            bean.setStato(stato);
            bean.setQuantitaTotale(partController.getQuantitaTotale(annuncio.getIdAnnuncio()));
            bean.setNumeroPartecipanti(partController.getNumeroPartecipanti(annuncio.getIdAnnuncio()));
            
            annunciBean.add(bean);
        }
        
        return annunciBean;
    }

    private void validaDati(String titolo, String autore, LocalDate dataScadenza, 
                            String nomeProdotto, int quantita, String citta) 
                            throws AnnuncioNonValidoException {
        
        if (titolo == null || titolo.trim().isEmpty()) {
            throw new AnnuncioNonValidoException("Il titolo non può essere vuoto");
        }
        
        if (autore == null || autore.trim().isEmpty()) {
            throw new AnnuncioNonValidoException("L'autore non può essere vuoto");
        }
        
        if (nomeProdotto == null || nomeProdotto.trim().isEmpty()) {
            throw new AnnuncioNonValidoException("Il nome del prodotto non può essere vuoto");
        }
        
        if (quantita <= 0) {
            throw new AnnuncioNonValidoException("La quantità deve essere maggiore di zero");
        }
        
        if (citta == null || citta.trim().isEmpty()) {
            throw new AnnuncioNonValidoException("La città non può essere vuota");
        }
        
        if (dataScadenza == null || dataScadenza.isBefore(LocalDate.now())) {
            throw new AnnuncioNonValidoException("La data di scadenza deve essere futura");
        }
    }
}
