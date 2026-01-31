package controllers.applicativo;

import engclasses.beans.AnnuncioBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import engclasses.dao.AnnuncioDAO;
import engclasses.exceptions.AnnuncioNonValidoException;
import model.Annuncio;

public class AnnuncioController {

    /**
     * Recupera tutti gli annunci dal database
     * @return lista di AnnuncioBean
     */
    public List<AnnuncioBean> getAnnunci() {

        List<Annuncio> annunci = AnnuncioDAO.getAnnunci();      //recupero annunci da dbms (come entity)
        List<AnnuncioBean> annunciBean = new ArrayList<>();     //creo una lista dove inserire i bean degli annunci che prendo da dbms

        for (Annuncio annuncio : annunci){
            AnnuncioBean bean = convertToBean(annuncio);   //converto gli annunci da model a bean
            annunciBean.add(bean);
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

                // crea oggetto dominio
                Annuncio annuncio = new Annuncio(
                        autore,
                        titolo,
                        descrizione,
                        dataCreazione,
                        nomeProdotto,
                        quantita,
                        citta,
                        dataScadenza
                );

                // persistenza
                AnnuncioDAO.salvaAnnuncio(annuncio);
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
