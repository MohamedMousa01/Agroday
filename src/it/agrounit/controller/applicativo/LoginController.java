package it.agrounit.controller.applicativo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loginButton;


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
