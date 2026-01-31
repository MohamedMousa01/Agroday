package controllers.grafico.cli;

import controllers.applicativo.LoginController;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.exceptions.LoginFallitoException;
import engclasses.pattern.ViewFactory.ViewManager;
import misc.ViewType;
import view.cli.LoginCLIView;
import misc.PersistenceType;
import misc.Session;

public class LoginCLIController {

    private final LoginCLIView view;
    private final Session session;

    public LoginCLIController() {
        this.view = new LoginCLIView();
        this.session = Session.getInstance();
    }

    public void start() {
        view.mostraTitolo();

        int azione = view.chiediAzione();

        switch (azione) {
            case 1 -> eseguiLogin();
            case 2 -> vaiARegistrazione();
            case 0 -> view.mostraMessaggio("Uscita dall'applicazione.");
            default -> view.mostraErrore("Scelta non valida.");
        }
    }

    // ==================== LOGIN ====================

    private void eseguiLogin() {
        PersistenceType persistenceType = determinaPersistenza();
        session.setPersistenceType(persistenceType);

        String username = view.chiediUsername();
        String password = view.chiediPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.mostraErrore("Username e password obbligatori.");
            return;
        }

        try {
            LoginController controller = new LoginController(session);
            boolean autenticato = controller.autentica(username, password);

            if (autenticato) {
                view.mostraSuccesso("Accesso effettuato!");
                
                // Naviga al menu principale usando ViewManager
                ViewManager.goTo(ViewType.MAIN);
            } else {
                view.mostraErrore("Username o password errati.");
            }

        } catch (LoginFallitoException e) {
            view.mostraErrore(e.getMessage());
        } catch (DatabaseConnessioneFallitaException | DatabaseOperazioneFallitaException e) {
            view.mostraErrore("Errore di sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== SUPPORTO ====================

    private PersistenceType determinaPersistenza() {
        int scelta = view.chiediPersistenza();

        return switch (scelta) {
            case 1 -> PersistenceType.MEMORY;
            case 2 -> PersistenceType.FILE;
            case 3 -> PersistenceType.DB;
            default -> {
                view.mostraMessaggio("Scelta non valida, uso MEMORY.");
                yield PersistenceType.MEMORY;
            }
        };
    }

    private void vaiARegistrazione() {
        ViewManager.goTo(ViewType.REGISTRAZIONE);
    }
}

