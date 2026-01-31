package model.observer;

import model.Appuntamento;

/**
 * Servizio di notifica che implementa AppuntamentoObserver.
 * Gestisce l'invio di notifiche agli utenti coinvolti negli appuntamenti.
 */
public class NotificationService implements AppuntamentoObserver {

    private static NotificationService instance;

    private NotificationService() {
        // Singleton
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    @Override
    public void onAppuntamentoModificato(Appuntamento appuntamento, String evento) {
        switch (evento) {
            case "CANCELLATO_CLIENTE":
                notificaCancellazioneAlConsulente(appuntamento);
                break;
            case "CANCELLATO_CONSULENTE":
                notificaCancellazioneAlCliente(appuntamento);
                break;
            case "CONFERMATO":
                notificaConferma(appuntamento);
                break;
            case "COMPLETATO":
                notificaCompletamento(appuntamento);
                break;
            default:
                System.out.println("[NOTIFICATION] Evento non gestito: " + evento);
        }
    }

    /**
     * Notifica al consulente che il cliente ha cancellato l'appuntamento.
     */
    private void notificaCancellazioneAlConsulente(Appuntamento appuntamento) {
        String messaggio = String.format(
                "[NOTIFICA CONSULENTE] L'appuntamento del %s è stato cancellato dal cliente. Motivo: %s",
                appuntamento.getDataOraInizio().toString(),
                appuntamento.getMotivoCancellazione() != null ? appuntamento.getMotivoCancellazione() : "Non specificato"
        );
        inviaNotifica(appuntamento.getIdConsulente(), messaggio);
    }

    /**
     * Notifica al cliente che il consulente ha cancellato l'appuntamento.
     */
    private void notificaCancellazioneAlCliente(Appuntamento appuntamento) {
        String messaggio = String.format(
                "[NOTIFICA CLIENTE] L'appuntamento del %s è stato cancellato dal consulente. Motivo: %s",
                appuntamento.getDataOraInizio().toString(),
                appuntamento.getMotivoCancellazione() != null ? appuntamento.getMotivoCancellazione() : "Non specificato"
        );
        inviaNotifica(appuntamento.getIdCliente(), messaggio);
    }

    /**
     * Notifica entrambe le parti della conferma dell'appuntamento.
     */
    private void notificaConferma(Appuntamento appuntamento) {
        String messaggio = String.format(
                "[NOTIFICA] L'appuntamento del %s è stato confermato. Tipo: %s, Luogo: %s",
                appuntamento.getDataOraInizio().toString(),
                appuntamento.getTipoConsulenza().getDisplayName(),
                appuntamento.getLuogo()
        );
        inviaNotifica(appuntamento.getIdCliente(), messaggio);
        inviaNotifica(appuntamento.getIdConsulente(), messaggio);
    }

    /**
     * Notifica entrambe le parti del completamento dell'appuntamento.
     */
    private void notificaCompletamento(Appuntamento appuntamento) {
        String messaggio = String.format(
                "[NOTIFICA] L'appuntamento del %s è stato completato con successo.",
                appuntamento.getDataOraInizio().toString()
        );
        inviaNotifica(appuntamento.getIdCliente(), messaggio);
        inviaNotifica(appuntamento.getIdConsulente(), messaggio);
    }

    /**
     * Metodo di invio notifica (simulato per ora).
     * In una implementazione reale, questo invierebbe email, push notifications, etc.
     */
    private void inviaNotifica(String idUtente, String messaggio) {
        // Per ora stampa sulla console - in produzione invierebbe email/push
        System.out.println("[NOTIFICATION SERVICE] Destinatario: " + idUtente);
        System.out.println(messaggio);
        System.out.println("---");
    }

    /**
     * Invia una notifica personalizzata a un utente specifico.
     */
    public void inviaNotificaPersonalizzata(String idUtente, String titolo, String messaggio) {
        System.out.println("[NOTIFICATION SERVICE] Notifica personalizzata a: " + idUtente);
        System.out.println("Titolo: " + titolo);
        System.out.println("Messaggio: " + messaggio);
        System.out.println("---");
    }
}
