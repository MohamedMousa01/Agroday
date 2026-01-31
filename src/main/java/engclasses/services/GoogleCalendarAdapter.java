package engclasses.services;

import model.Appuntamento;
import model.observer.AppuntamentoObserver;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Adapter per l'integrazione con Google Calendar.
 * Implementa sia CalendarService (per le operazioni calendario)
 * sia AppuntamentoObserver (per sincronizzare automaticamente le modifiche).
 * 
 * Parte del pattern Adapter.
 * 
 * NOTA: Questa è un'implementazione simulata. Per un'integrazione reale con Google Calendar,
 * sarà necessario:
 * 1. Aggiungere le dipendenze Google Calendar API al pom.xml
 * 2. Configurare le credenziali OAuth2
 * 3. Implementare le chiamate API reali
 */
public class GoogleCalendarAdapter implements CalendarService, AppuntamentoObserver {

    private static GoogleCalendarAdapter instance;
    private boolean isConfigured;
    private String calendarId;
    
    // Simulazione: mappa degli eventi creati (in produzione non servirebbe)
    private final Map<String, String> eventiCreati;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private GoogleCalendarAdapter() {
        this.eventiCreati = new HashMap<>();
        this.isConfigured = false;
        this.calendarId = "primary";
    }

    public static GoogleCalendarAdapter getInstance() {
        if (instance == null) {
            instance = new GoogleCalendarAdapter();
        }
        return instance;
    }

    /**
     * Configura l'adapter con le credenziali Google.
     * In un'implementazione reale, qui si configurerebbero le credenziali OAuth2.
     *
     * @param credentialsPath Percorso al file delle credenziali
     * @param calendarId ID del calendario da utilizzare
     * @return true se la configurazione ha successo
     */
    public boolean configura(String credentialsPath, String calendarId) {
        // Simulazione della configurazione
        System.out.println("[GOOGLE CALENDAR] Configurazione con credenziali: " + credentialsPath);
        System.out.println("[GOOGLE CALENDAR] Calendar ID: " + calendarId);
        
        this.calendarId = calendarId;
        this.isConfigured = true;
        
        return true;
    }

    @Override
    public String creaEvento(Appuntamento appuntamento) {
        if (!isConfigured) {
            System.out.println("[GOOGLE CALENDAR] Servizio non configurato. Evento non creato.");
            return null;
        }

        // Simulazione della creazione evento
        String eventId = "gcal_" + UUID.randomUUID().toString().substring(0, 12);
        
        System.out.println("[GOOGLE CALENDAR] Creazione evento:");
        System.out.println("  - Titolo: Consulenza " + appuntamento.getTipoConsulenza().getDisplayName());
        System.out.println("  - Inizio: " + appuntamento.getDataOraInizio().format(FORMATTER));
        System.out.println("  - Fine: " + appuntamento.getDataOraFine().format(FORMATTER));
        System.out.println("  - Luogo: " + appuntamento.getLuogo());
        System.out.println("  - Event ID: " + eventId);
        
        eventiCreati.put(eventId, appuntamento.getIdAppuntamento());
        
        return eventId;
    }

    @Override
    public boolean aggiornaEvento(Appuntamento appuntamento) {
        if (!isConfigured) {
            System.out.println("[GOOGLE CALENDAR] Servizio non configurato. Evento non aggiornato.");
            return false;
        }

        String eventId = appuntamento.getGoogleCalendarEventId();
        if (eventId == null || !eventiCreati.containsKey(eventId)) {
            System.out.println("[GOOGLE CALENDAR] Evento non trovato: " + eventId);
            return false;
        }

        System.out.println("[GOOGLE CALENDAR] Aggiornamento evento: " + eventId);
        System.out.println("  - Nuovo inizio: " + appuntamento.getDataOraInizio().format(FORMATTER));
        System.out.println("  - Nuova fine: " + appuntamento.getDataOraFine().format(FORMATTER));
        System.out.println("  - Nuovo luogo: " + appuntamento.getLuogo());
        
        return true;
    }

    @Override
    public boolean cancellaEvento(String eventId) {
        if (!isConfigured) {
            System.out.println("[GOOGLE CALENDAR] Servizio non configurato. Evento non cancellato.");
            return false;
        }

        if (eventId == null || !eventiCreati.containsKey(eventId)) {
            System.out.println("[GOOGLE CALENDAR] Evento non trovato: " + eventId);
            return false;
        }

        System.out.println("[GOOGLE CALENDAR] Cancellazione evento: " + eventId);
        eventiCreati.remove(eventId);
        
        return true;
    }

    @Override
    public boolean isDisponibile() {
        return isConfigured;
    }

    @Override
    public String getLinkEvento(String eventId) {
        if (eventId == null || !eventiCreati.containsKey(eventId)) {
            return null;
        }
        // Simulazione del link all'evento Google Calendar
        return "https://calendar.google.com/calendar/event?eid=" + eventId;
    }

    // ==================== AppuntamentoObserver Implementation ====================

    @Override
    public void onAppuntamentoModificato(Appuntamento appuntamento, String evento) {
        if (!isConfigured) {
            return;
        }

        switch (evento) {
            case "CANCELLATO_CLIENTE":
            case "CANCELLATO_CONSULENTE":
                // Cancella l'evento dal calendario
                if (appuntamento.getGoogleCalendarEventId() != null) {
                    cancellaEvento(appuntamento.getGoogleCalendarEventId());
                }
                break;
            case "CONFERMATO":
                // Aggiorna lo stato dell'evento (in un'implementazione reale si cambierebbe il colore/stato)
                if (appuntamento.getGoogleCalendarEventId() != null) {
                    System.out.println("[GOOGLE CALENDAR] Evento confermato: " + appuntamento.getGoogleCalendarEventId());
                }
                break;
            case "COMPLETATO":
                // Segna l'evento come completato
                if (appuntamento.getGoogleCalendarEventId() != null) {
                    System.out.println("[GOOGLE CALENDAR] Evento completato: " + appuntamento.getGoogleCalendarEventId());
                }
                break;
            default:
                System.out.println("[GOOGLE CALENDAR] Evento non gestito: " + evento);
        }
    }

    /**
     * Reset dell'adapter (utile per i test).
     */
    public void reset() {
        eventiCreati.clear();
        isConfigured = false;
    }
}
