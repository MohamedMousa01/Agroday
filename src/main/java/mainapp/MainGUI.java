package mainapp;

import engclasses.pattern.viewfactory.SceneManagerGUI;
import engclasses.pattern.viewfactory.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import misc.Session;
import misc.UiType;
import misc.ViewType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainGUI extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainGUI.class);

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Starting Agroday GUI application");
        
        // Inizializza la sessione con UI GUI
        Session.getInstance().setTipoInterfaccia(UiType.GUI);
        
        // Registra lo stage in SceneManagerGUI
        SceneManagerGUI.setStage(stage);
        
        // Configura finestra
        stage.setTitle("Agroday - Login");
        stage.setResizable(false);
        
        // Naviga alla schermata di login usando ViewManager
        ViewManager.goTo(ViewType.LOGIN);
    }

    public static void main(String[] args) {
        launch(args);
    }
}