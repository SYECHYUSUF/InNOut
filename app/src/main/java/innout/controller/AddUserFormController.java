package innout.controller;

import innout.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserFormController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    void handleRegisterAction(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Email and password cannot be empty.");
            return;
        }

        boolean result = UserService.registerUser(email, password);
        String message = result ? "Registration successful! You can now log in." : "Email is already registered. Please choose another email.";

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Success", message);
            closeWindow(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", message);
        }
    }

    @FXML
    void handleCancelAction(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}