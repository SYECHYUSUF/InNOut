package innout.controller;

import innout.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterUserController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;

    @FXML
    private void handleRegister(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Registration Failed", "Please fill in all fields.");
            return;
        }

        boolean result = UserService.registerUser(email, password);
        String message = result ? "Registration successful! You can now log in." : "Email is already registered. Please choose another email.";

        showAlert("Registration", message);
    }

    @FXML
    private void showLoginForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            VBox loginRoot = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
