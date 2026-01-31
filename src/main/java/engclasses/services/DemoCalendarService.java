package engclasses.services;

import model.Appuntamento;

/**
 * Implementazione "demo" del CalendarService che non effettua operazioni reali.
 * Utilizzata in modalità Demo per evitare chiamate esterne.
 */
public class DemoCalendarService implements CalendarService {

    private static DemoCalendarService instance;

    private DemoCalendarService() {
        // Singleton
    }

    public static DemoCalendarService getInstance() {
        if (instance == null) {
            instance = new DemoCalendarService();
        }
        return instance;
    }

    @Override
    public String creaEvento(Appuntamento appuntamento) {
        // In modalità demo, non creiamo eventi reali
        System.out.println("[DEMO CALENDAR] Simulazione creazione evento per appuntamento: " + appuntamento.getIdAppuntamento());
        return "demo_event_" + appuntamento.getIdAppuntamento().substring(0, 8);
    }

    @Override
    public boolean aggiornaEvento(Appuntamento appuntamento) {
        System.out.println("[DEMO CALENDAR] Simulazione aggiornamento evento: " + appuntamento.getGoogleCalendarEventId());
        return true;
    }

    @Override
    public boolean cancellaEvento(String eventId) {
        System.out.println("[DEMO CALENDAR] Simulazione cancellazione evento: " + eventId);
        return true;
    }

    @Override
    public boolean isDisponibile() {
        // In demo è sempre "disponibile" ma non fa nulla di reale
        return true;
    }

    @Override
    public String getLinkEvento(String eventId) {
        // Restituisce un link fittizio
        return "demo://calendar/event/" + eventId;
    }
}
