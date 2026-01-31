package engclasses.pattern.ViewFactory;

import controllers.grafico.cli.MainAgricoltoreCLIController;
import controllers.grafico.cli.MainVenditoreCLIController;
import controllers.grafico.cli.MainConsulenteCLIController;
import view.cli.ProfiloCLIView;

import java.util.Scanner;

/**
 * Manager per gestire la visualizzazione delle view CLI
 * Delega ai controller CLI appropriati per ogni tipo di utente
 */
public class CliViewManager {

    private static final Scanner scanner = new Scanner(System.in);

    private CliViewManager() {
        // Costruttore privato per utility class
    }

    // ==================== MAIN VIEWS ====================

    public static void showMainAgricoltore() {
        MainAgricoltoreCLIController controller = new MainAgricoltoreCLIController();
        controller.start();
    }

    public static void showMainVenditore() {
        MainVenditoreCLIController controller = new MainVenditoreCLIController();
        controller.start();
    }

    public static void showMainConsulente() {
        MainConsulenteCLIController controller = new MainConsulenteCLIController();
        controller.start();
    }

    // ==================== FEATURE VIEWS ====================

    public static void showProfilo() {
        ProfiloCLIView profiloView = new ProfiloCLIView();
        profiloView.mostraTitolo();
        profiloView.mostraProfilo(
            misc.Session.getInstance().getUtenteLoggato(),
            misc.Session.getInstance().getTipoUtente()
        );
        profiloView.mostraMenu();
        
        int scelta = profiloView.chiediScelta();
        
        switch (scelta) {
            case 1, 2, 3, 4, 5 -> {
                profiloView.mostraMessaggio("Modifica profilo - Funzionalità in sviluppo");
                profiloView.attendiInvio();
            }
        }
    }

    public static void showGestioneProdotti() {
        showMessage("🌾 Gestione Prodotti - Funzionalità in sviluppo");
        waitForEnter();
    }

    public static void showOrdini() {
        showMessage("📦 Gestione Ordini - Funzionalità in sviluppo");
        waitForEnter();
    }

    public static void showConsulenza() {
        showMessage("💬 Consulenze - Funzionalità in sviluppo");
        waitForEnter();
    }

    // ==================== UTILITY ====================

    public static void showMessage(String message) {
        System.out.println("\n💬 " + message);
    }

    public static void showError(String error) {
        System.out.println("\n❌ ERRORE: " + error);
    }

    public static void showSuccess(String message) {
        System.out.println("\n✅ " + message);
    }

    public static void waitForEnter() {
        System.out.println("\nPremi INVIO per continuare...");
        scanner.nextLine();
    }

    public static void clearScreen() {
        // Stampa righe vuote per "pulire" lo schermo
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}



