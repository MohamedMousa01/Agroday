package controllers.grafico.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginGUIController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ToggleButton persistenceToggle;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loginButton;

    @FXML
    private void initialize() {
        persistenceToggle.setSelected(false);
        persistenceToggle.setOnAction(e -> { persistenceToggle.setText(persistenceToggle.isSelected() ? "ON" : "OFF"); }); }


    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.equals("admin") && password.equals("1234")) {
            messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            messageLabel.setText("Accesso effettuato!");
        } else {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            messageLabel.setText("Username o password errati!");
        }
    }
}