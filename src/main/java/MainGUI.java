

import engclasses.pattern.ViewFactory.SceneManagerGUI;
import engclasses.pattern.ViewFactory.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import misc.Session;
import misc.UiType;
import misc.ViewType;

public class MainGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
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