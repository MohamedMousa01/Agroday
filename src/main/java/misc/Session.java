package misc;

import engclasses.beans.AnnuncioBean;
import engclasses.beans.AppuntamentoBean;
import model.Utente;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static Session instance;
    private Utente utenteLoggato;
    private PersistenceType persistenceType;    //tra Memory, FIleSytem o Database
    private UiType tipoInterfaccia;
    private TipoUtente tipoUtente;


    private long idAnnuncio;
    private List<AnnuncioBean> Annunci;

    // Gestione appuntamenti nella sessione
    private List<AppuntamentoBean> appuntamentiCorrente;
    private AppuntamentoBean appuntamentoSelezionato;

    // Flag per modalità operativa
    private boolean modalitaDemo = true; // true = Demo (senza Google Calendar), false = Full

    private Session() {
        // default: DEMO
        this.persistenceType = PersistenceType.MEMORY;
        this.appuntamentiCorrente = new ArrayList<>();
        this.modalitaDemo = true;
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // 🔹 SETTER (chiamato dal controller applicativo)
    public void setPersistenceType(PersistenceType persistenceType) {
        this.persistenceType = persistenceType;
        // Aggiorna automaticamente la modalità
        this.modalitaDemo = (persistenceType == PersistenceType.MEMORY);
    }

    // 🔹 GETTER (usato dalle factory / controller)
    public PersistenceType getPersistenceType() {
        return persistenceType;
    }


    public Utente getUtenteLoggato(){
        return utenteLoggato;
    }

    public void setUtenteLoggato(Utente utenteLoggato) {
        this.utenteLoggato = utenteLoggato;
    }


    public List<AnnuncioBean> getAnnunci() {
        return Annunci;
    }

    public void setAnnunci(List<AnnuncioBean> Annunci) {
        this.Annunci = Annunci;
    }

    // ==================== Gestione Appuntamenti ====================

    public List<AppuntamentoBean> getAppuntamentiCorrente() {
        return appuntamentiCorrente;
    }

    public void setAppuntamentiCorrente(List<AppuntamentoBean> appuntamenti) {
        this.appuntamentiCorrente = appuntamenti != null ? appuntamenti : new ArrayList<>();
    }

    public void addAppuntamento(AppuntamentoBean appuntamento) {
        if (appuntamentiCorrente == null) {
            appuntamentiCorrente = new ArrayList<>();
        }
        appuntamentiCorrente.add(appuntamento);
    }

    public void removeAppuntamento(String idAppuntamento) {
        if (appuntamentiCorrente != null) {
            appuntamentiCorrente.removeIf(a -> a.getIdAppuntamento().equals(idAppuntamento));
        }
    }

    public AppuntamentoBean getAppuntamentoSelezionato() {
        return appuntamentoSelezionato;
    }

    public void setAppuntamentoSelezionato(AppuntamentoBean appuntamento) {
        this.appuntamentoSelezionato = appuntamento;
    }

    // ==================== Modalità Operativa ====================

    public boolean isModalitaDemo() {
        return modalitaDemo;
    }

    public void setModalitaDemo(boolean modalitaDemo) {
        this.modalitaDemo = modalitaDemo;
    }

    /**
     * Verifica se il sistema è in modalità Full (con persistenza reale e Google Calendar).
     */
    public boolean isModalitaFull() {
        return !modalitaDemo;
    }

    // ==================== Clear Session ====================


    public UiType getTipoInterfaccia(){return tipoInterfaccia;}
    public void setTipoInterfaccia(UiType tipoInterfaccia){
        this.tipoInterfaccia = tipoInterfaccia;
    }

    // Alias per compatibilità
    public Utente getUtente() {
        return utenteLoggato;
    }

    public void clearSession(){
        utenteLoggato = null;
        idAnnuncio = 0;

        if (Annunci != null) {
            Annunci.clear();
        }

        if (appuntamentiCorrente != null) {
            appuntamentiCorrente.clear();
        }

        appuntamentoSelezionato = null;
        persistenceType = PersistenceType.MEMORY;
        modalitaDemo = true;
    }

    public void setTipoUtente(TipoUtente tipoUtente) {
        this.tipoUtente = tipoUtente;
    }
    public TipoUtente getTipoUtente(){
        return tipoUtente;
    }
}
