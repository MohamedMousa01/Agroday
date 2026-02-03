package view.cli;
import misc.UIConstants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * View CLI per la creazione di un nuovo annuncio
 */
public class CreaAnnuncioCLIView {

    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ==================== OUTPUT ====================

    public void mostraTitolo() {
        clearScreen();
        System.out.println(UIConstants.BOX_TOP_LONG);
        System.out.println("║            📝 CREA NUOVO ANNUNCIO 📝                   ║");
        System.out.println(UIConstants.BOX_BOTTOM_LONG);
        System.out.println("\nCompila i seguenti campi per creare il tuo annuncio:");
        mostraSeparatore();
    }

    public void mostraRiepilogo(String titolo, String descrizione, String nomeProdotto, 
                                int quantita, String citta, LocalDate dataScadenza) {
        System.out.println(UIConstants.BOX_TOP_LONG);
        System.out.println("║               📋 RIEPILOGO ANNUNCIO                    ║");
        System.out.println(UIConstants.BOX_BOTTOM_LONG);
        System.out.println("\n  📌 Titolo:        " + titolo);
        System.out.println("  📝 Descrizione:   " + descrizione);
        System.out.println("  🌾 Prodotto:      " + nomeProdotto);
        System.out.println("  📦 Quantità:      " + quantita + " kg");
        System.out.println("  📍 Città:         " + citta);
        System.out.println("  📅 Scadenza:      " + dataScadenza.format(dateFormatter));
        mostraSeparatore();
    }

    public void mostraMessaggio(String messaggio) {
        System.out.println("\n💬 " + messaggio);
    }

    public void mostraSuccesso(String messaggio) {
        System.out.println("\n✅ " + messaggio);
    }

    public void mostraErrore(String errore) {
        System.out.println("\n❌ ERRORE: " + errore);
    }

    public void mostraInfo(String info) {
        System.out.println("\nℹ️  " + info);
    }

    // ==================== INPUT ====================

    public String chiediTitolo() {
        System.out.print("\n📌 Titolo dell'annuncio: ");
        return scanner.nextLine().trim();
    }

    public String chiediDescrizione() {
        System.out.print("\n📝 Descrizione dettagliata: ");
        return scanner.nextLine().trim();
    }

    public String chiediNomeProdotto() {
        System.out.print("\n🌾 Nome del prodotto: ");
        return scanner.nextLine().trim();
    }

    public int chiediQuantita() {
        System.out.print("\n📦 Quantità disponibile (in kg): ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String chiediCitta() {
        System.out.print("\n📍 Città: ");
        return scanner.nextLine().trim();
    }

    public LocalDate chiediDataScadenza() {
        System.out.print("\n📅 Data di scadenza (formato: gg/mm/aaaa): ");
        String input = scanner.nextLine().trim();
        try {
            return LocalDate.parse(input, dateFormatter);
        } catch (DateTimeParseException e) {
            mostraErrore("Formato data non valido. Usa il formato gg/mm/aaaa (es: 31/12/2024)");
            return null;
        }
    }

    public boolean confermaCreazione() {
        System.out.print("\n✅ Confermi la creazione dell'annuncio? (S/N): ");
        String risposta = scanner.nextLine().trim().toUpperCase();
        return risposta.equals("S") || risposta.equals("SI") || risposta.equals("Y") || risposta.equals("YES");
    }

    public boolean vuoiCreareAltroAnnuncio() {
        System.out.print("\n📝 Vuoi creare un altro annuncio? (S/N): ");
        String risposta = scanner.nextLine().trim().toUpperCase();
        return risposta.equals("S") || risposta.equals("SI") || risposta.equals("Y") || risposta.equals("YES");
    }

    public void attendiInvio() {
        System.out.print("\nPremi INVIO per continuare...");
        scanner.nextLine();
    }

    // ==================== UTILITY ====================

    private void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public void mostraSeparatore() {
        System.out.println(UIConstants.BOX_SEPARATOR_LONG);
    }
}
