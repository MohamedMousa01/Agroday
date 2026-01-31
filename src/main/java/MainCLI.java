import engclasses.pattern.ViewFactory.ViewManager;
import misc.Session;
import misc.UiType;
import misc.ViewType;

public class MainCLI {

    public static void main(String[] args) {
        
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   AGRODAY - Modalità CLI              ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        // Inizializza la sessione con UI CLI
        Session.getInstance().setTipoInterfaccia(UiType.CLI);
        
        // Naviga alla schermata di login usando ViewManager
        ViewManager.goTo(ViewType.LOGIN);
    }
}

