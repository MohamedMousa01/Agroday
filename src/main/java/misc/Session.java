package misc;

import engclasses.beans.AnnuncioBean;
import model.Utente;

import java.util.List;

public class Session {

    private static Session instance;
    private Utente utenteLoggato;
    private PersistenceType persistenceType;    //tra Memory, FIleSytem o Database


    private long idAnnuncio;
    private List<AnnuncioBean> Annunci;


    private Session() {
        // default: DEMO
        this.persistenceType = PersistenceType.MEMORY;
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

    public void clearSession(){
        utenteLoggato = null;
        idAnnuncio = 0;

        if (Annunci != null) {
            Annunci.clear();
        }

        persistenceType = PersistenceType.MEMORY;
    }

}
