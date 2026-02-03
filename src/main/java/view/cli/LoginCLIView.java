package view.cli;
import misc.UIConstants;

import java.util.Scanner;

public class LoginCLIView {

    private final Scanner scanner = new Scanner(System.in);

    // ==================== OUTPUT ====================

    public void mostraTitolo() {
        System.out.println("\n=== LOGIN AGRODAY ===");
    }

    public void mostraMessaggio(String messaggio) {
        System.out.println(messaggio);
    }

    public void mostraErrore(String messaggio) {
        System.out.println("❌ " + messaggio);
    }

    public void mostraSuccesso(String messaggio) {
        System.out.println("✅ " + messaggio);
    }

    // ==================== INPUT ====================

    public String chiediUsername() {
        System.out.print("Username: ");
        return scanner.nextLine();
    }

    public String chiediPassword() {
        System.out.print("Password: ");
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

    public int chiediAzione() {
        System.out.println("\n1) Login");
        System.out.println("2) Registrazione");
        System.out.println("0) Esci");
        System.out.print("> ");
        return Integer.parseInt(scanner.nextLine());
    }
}
