package view.cli;

import model.Utente;
import misc.TipoUtente;
import java.util.Scanner;

/**
 * View CLI per la gestione del profilo utente
 */
public class ProfiloCLIView {

    private final Scanner scanner = new Scanner(System.in);

    // ==================== OUTPUT ====================

    public void mostraTitolo() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║               👤 IL MIO PROFILO 👤                     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }

    public void mostraProfilo(Utente utente, TipoUtente tipo) {
        System.out.println("\n┌────────────────────────────────────────────────────────┐");
        System.out.println("│  INFORMAZIONI PERSONALI                                │");
        System.out.println("├────────────────────────────────────────────────────────┤");
        System.out.println("│  👤 Nome:        " + padRight(utente.getNome(), 37) + "│");
        System.out.println("│  👤 Cognome:     " + padRight(utente.getCognome(), 37) + "│");
        System.out.println("│  📧 Email:       " + padRight(utente.getEmail(), 37) + "│");
        System.out.println("│  🔑 Username:    " + padRight(utente.getUsername(), 37) + "│");
        System.out.println("│  📍 Città:       " + padRight(utente.getCitta(), 37) + "│");
        System.out.println("│  🏷️  Tipo:        " + padRight(tipo.toString(), 37) + "│");
        System.out.println("└────────────────────────────────────────────────────────┘");
    }

    public void mostraMenu() {
        System.out.println("\n┌────────────────────────────────────────────────────────┐");
        System.out.println("│  AZIONI                                                │");
        System.out.println("├────────────────────────────────────────────────────────┤");
        System.out.println("│  1) ✏️  Modifica Nome                                   │");
        System.out.println("│  2) ✏️  Modifica Cognome                                │");
        System.out.println("│  3) ✏️  Modifica Email                                  │");
        System.out.println("│  4) 🔒 Cambia Password                                 │");
        System.out.println("│  5) 📍 Modifica Città                                  │");
        System.out.println("│  0) 🔙 Torna Indietro                                  │");
        System.out.println("└────────────────────────────────────────────────────────┘");
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

    // ==================== INPUT ====================

    public int chiediScelta() {
        System.out.print("\n➤ Scegli un'opzione: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String chiediNuovoValore(String campo) {
        System.out.print("\n➤ Inserisci nuovo " + campo + ": ");
        return scanner.nextLine().trim();
    }

    public String chiediPassword(String messaggio) {
        System.out.print("\n➤ " + messaggio + ": ");
        return scanner.nextLine().trim();
    }

    public void attendiInvio() {
        System.out.print("\nPremi INVIO per continuare...");
        scanner.nextLine();
    }

    public boolean confermaModifica() {
        System.out.print("\n⚠️  Confermi la modifica? (S/N): ");
        String risposta = scanner.nextLine().trim().toUpperCase();
        return risposta.equals("S") || risposta.equals("SI") || risposta.equals("Y") || risposta.equals("YES");
    }

    // ==================== UTILITY ====================

    private void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private String padRight(String s, int n) {
        if (s.length() >= n) {
            return s.substring(0, n);
        }
        return String.format("%-" + n + "s", s);
    }

    public void mostraSeparatore() {
        System.out.println("════════════════════════════════════════════════════════");
    }
}
