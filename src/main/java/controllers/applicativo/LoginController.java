package controllers.applicativo;

import engclasses.beans.LoginBean;
import engclasses.exceptions.LoginFallitoException;
import misc.PersistenceType;
import misc.Session;

public class LoginController {      //dovrò mettere anche qui il tipo di persistenza, è necessario per vedere se le modifiche
                                    // resteranno anche dopo aver chiuso l'applicazione

    //dedvo mettere due tipi di persistenza dentro a sessione: una variabile che mi salva il tipo di persistenza in registrazione
    // e un altra variabile che mi salva il tipo di persistenza dopo il login.


    private Session session;
    private String idUtente;





    // Metodo per validare i campi di login
    private String validaCampiLogin(LoginBean loginBean) {
        StringBuilder errori = new StringBuilder();

        if (loginBean.getUsername() == null || loginBean.getUsername().trim().isEmpty()) {
            errori.append("Il campo username non può essere vuoto.\n");
        }
        if (loginBean.getPassword() == null || loginBean.getPassword().trim().isEmpty()) {
            errori.append("Il campo password non può essere vuoto.\n");
        }
        return errori.toString();
    }


}
