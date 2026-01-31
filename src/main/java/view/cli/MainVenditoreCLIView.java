package view.cli;

import model.Utente;
import java.util.Scanner;

/**
 * View CLI per il pannello principale del Venditore
 */
public class MainVenditoreCLIView {

    private final Scanner scanner = new Scanner(System.in);

    // ==================== OUTPUT ====================

    public void mostraTitolo(Utente utente) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║          🛒 AGRODAY - PANNELLO VENDITORE 🛒           ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("  Benvenuto, " + utente.getNome() + " " + utente.getCognome() + "!");
        System.out.println("  📧 " + utente.getEmail() + " | 📍 " + utente.getCitta());
        System.out.println("════════════════════════════════════════════════════════");
    }

    public void mostraMenu() {
        System.out.println("\n┌────────────────────────────────────────────────────────┐");
        System.out.println("│  MENU PRINCIPALE                                       │");
        System.out.println("├────────────────────────────────────────────────────────┤");
        System.out.println("│  1) 🔍 Cerca Annunci                                   │");
        System.out.println("│  2) 📦 Visualizza Tutti gli Annunci                    │");
        System.out.println("│  3) 🛍️  I Miei Ordini                                  │");
        System.out.println("│  4) 📊 Storico Acquisti                                │");
        System.out.println("│  5) 👤 Il Mio Profilo                                  │");
        System.out.println("│  6) ⚙️  Impostazioni                                    │");
        System.out.println("│  0) 🚪 Logout                                          │");
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

    public void mostraCaricamento(String messaggio) {
        System.out.println("\n⏳ " + messaggio + "...");
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

    public String chiediRicerca() {
        System.out.print("\n🔍 Inserisci termine di ricerca (prodotto/città): ");
        return scanner.nextLine().trim();
    }

    public void attendiInvio() {
        System.out.print("\nPremi INVIO per continuare...");
        scanner.nextLine();
    }

    public boolean confermaAzione(String messaggio) {
        System.out.print("\n" + messaggio + " (S/N): ");
        String risposta = scanner.nextLine().trim().toUpperCase();
        return risposta.equals("S") || risposta.equals("SI") || risposta.equals("Y") || risposta.equals("YES");
    }

    // ==================== UTILITY ====================

    private void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public void mostraSeparatore() {
        System.out.println("════════════════════════════════════════════════════════");
    }
}
