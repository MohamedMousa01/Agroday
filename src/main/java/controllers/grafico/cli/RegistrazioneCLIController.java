package controllers.grafico.cli;


import view.cli.RegistrazioneCLIView;
import controllers.applicativo.RegistrazioneController;
import engclasses.beans.RegistrazioneBean;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.exceptions.RegistrazioneFallitaException;
import engclasses.pattern.ViewFactory.ViewManager;
import misc.ViewType;
import misc.PersistenceType;
import misc.Session;
import misc.TipoUtente;

public class RegistrazioneCLIController {

    private final RegistrazioneCLIView view;
    private final Session session;
    private final RegistrazioneController controllerApplicativo;

    public RegistrazioneCLIController() {
        this.view = new RegistrazioneCLIView();
        this.session = Session.getInstance();
        this.controllerApplicativo = new RegistrazioneController(session);
    }

    public void start() {
        view.mostraTitolo();

        // 1️⃣ Tipo utente (input → interpretazione)
        TipoUtente tipoUtente = determinaTipoUtente();
        session.setTipoUtente(tipoUtente);

        // 2️⃣ Persistenza
        PersistenceType persistenceType = determinaPersistenza();
        session.setPersistenceType(persistenceType);

        // 3️⃣ Creazione Bean usando SOLO la View
        RegistrazioneBean bean = creaBean(tipoUtente, persistenceType);

        // 4️⃣ Registrazione
        try {
            controllerApplicativo.registraUtente(bean);
            view.mostraSuccesso("Registrazione completata con successo!");
            vaiAlLogin();
        } catch (RegistrazioneFallitaException e) {
            view.mostraErrore(e.getMessage());
        } catch (DatabaseConnessioneFallitaException | DatabaseOperazioneFallitaException e) {
            view.mostraErrore("Errore di sistema: " + e.getMessage());
        }
    }

    private void vaiAlLogin() {
        view.mostraMessaggio("\n➡ Reindirizzamento al login...\n");
        ViewManager.goTo(ViewType.LOGIN);
    }

    // ==================== METODI DI CONTROLLO ====================

    private TipoUtente determinaTipoUtente() {
        int scelta = view.chiediTipoUtente();

        return switch (scelta) {
            case 1 -> TipoUtente.AGRICOLTORE;
            case 2 -> TipoUtente.VENDITORE;
            case 3 -> TipoUtente.CONSULENTE;
            default -> {
                view.mostraMessaggio("Scelta non valida, uso AGRICOLTORE di default.");
                yield TipoUtente.AGRICOLTORE;
            }
        };
    }

    private PersistenceType determinaPersistenza() {
        int scelta = view.chiediPersistenza();

        return switch (scelta) {
            case 1 -> PersistenceType.MEMORY;
            case 2 -> PersistenceType.FILE;
            case 3 -> PersistenceType.DB;
            default -> {
                view.mostraMessaggio("Scelta non valida, uso MEMORY di default.");
                yield PersistenceType.MEMORY;
            }
        };
    }

    // ==================== CREAZIONE BEAN ====================

    private RegistrazioneBean creaBean(TipoUtente tipoUtente,
                                       PersistenceType persistenceType) {

        RegistrazioneBean bean = new RegistrazioneBean();

        bean.setNome(view.chiediNome());
        bean.setCognome(view.chiediCognome());
        bean.setUsername(view.chiediUsername());
        bean.setEmail(view.chiediEmail());
        bean.setPassword(view.chiediPassword());
        bean.setConfirmPassword(view.chiediConfermaPassword());
        bean.setCitta(view.chiediCitta());



        bean.setTipoUtente(tipoUtente);
        bean.setPersistenceType(persistenceType);

        return bean;
    }
}
