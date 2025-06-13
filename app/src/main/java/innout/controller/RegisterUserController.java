package innout.controller;

import innout.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterUserController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;

    private UserService userService = new UserService();

    @FXML
    private void handleRegister(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Validasi input kosong
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Registration Failed", "Please fill in all fields.");
            return;
        }

        // Cek apakah email sudah terdaftar
        String result = userService.registerUser(email, password);
        showAlert("Registration", result);  // Tampilkan hasil registrasi
    }

    @FXML
    private void showLoginForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            AnchorPane loginRoot = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            // stage.setFullScreen(true);
            stage.setScene(new Scene(loginRoot, 400, 300));
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
