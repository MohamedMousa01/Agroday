import engclasses.pattern.ViewFactory.ViewManager;
import misc.Session;
import misc.UIConstants;
import misc.UiType;
import misc.ViewType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainCLI {

    private static final Logger logger = LoggerFactory.getLogger(MainCLI.class);

    public static void main(String[] args) {
        
        logger.info(UIConstants.BOX_TOP_SHORT);
        logger.info("║   AGRODAY - Modalità CLI              ║");
        logger.info(UIConstants.BOX_BOTTOM_SHORT);
        
        // Inizializza la sessione con UI CLI
        Session.getInstance().setTipoInterfaccia(UiType.CLI);
        
        // Naviga alla schermata di login usando ViewManager
        ViewManager.goTo(ViewType.LOGIN);
    }
}

