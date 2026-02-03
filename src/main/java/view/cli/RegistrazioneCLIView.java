package view.cli;
import misc.UIConstants;

import java.util.Scanner;

public class RegistrazioneCLIView {

    private final Scanner scanner = new Scanner(System.in);

    // ==================== OUTPUT ====================

    public void mostraTitolo() {
        System.out.println("\n=== REGISTRAZIONE AGRODAY ===");
    }

    public void mostraMessaggio(String messaggio) {
        System.out.println(messaggio);
    }

    public void mostraErrore(String errore) {
        System.out.println("❌ " + errore);
    }

    public void mostraSuccesso(String messaggio) {
        System.out.println("✅ " + messaggio);
    }

    // ==================== INPUT ====================

    public String chiediNome() {
        System.out.print("Nome: ");
        return scanner.nextLine();
    }

    public String chiediCognome() {
        System.out.print("Cognome: ");
        return scanner.nextLine();
    }

    public String chiediUsername() {
        System.out.print("Username: ");
        return scanner.nextLine();
    }

    public String chiediEmail() {
        System.out.print("Email: ");
        return scanner.nextLine();
    }

    public String chiediPassword() {
        System.out.print("Password: ");
        return scanner.nextLine();
    }

    public String chiediConfermaPassword() {
        System.out.print("Conferma Password: ");
        return scanner.nextLine();
    }

    public String chiediCitta() {
        System.out.print("Città: ");
        return scanner.nextLine();
    }

    public int chiediTipoUtente() {
        System.out.println("Tipo utente:");
        System.out.println("1) Agricoltore");
        System.out.println("2) Venditore");
        System.out.println("3) Consulente");
        System.out.print("> ");
        return Integer.parseInt(scanner.nextLine());
    }

    public String chiediTitoloDiStudio() {
        System.out.print("Titolo di studio: ");
        return scanner.nextLine();
    }

    public int chiediPersistenza() {
        System.out.println("Tipo di persistenza:");
        System.out.println("1) Memory");
        System.out.println("2) File");
        System.out.println("3) Database");
        System.out.print("> ");
        return Integer.parseInt(scanner.nextLine());
    }
}
