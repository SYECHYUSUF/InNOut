package innout.controller;

import java.io.IOException;

import innout.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private UserService userService = new UserService();

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (userService.loginUser(email, password)) {
            if (email.equals("admin@email.com")) {
                // Jika email adalah admin, langsung navigasikan ke Admin Dashboard
                navigateToAdminDashboard(event);
            } else {
                showAlert("Login Successful", "Welcome " + email + "!");
                // Navigasi ke halaman user dashboard atau halaman lain jika diperlukan
            }
        } else {
            showAlert("Login Failed", "Invalid credentials.");
        }
    }

    @FXML
    private void showRegisterForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent registerRoot = loader.load();

            // Mendapatkan stage (jendela) yang sedang aktif
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(registerRoot);

            // Ganti scene untuk menampilkan form registrasi
            stage.setScene(scene);
            stage.setTitle("Register New User");
            stage.show();
        } catch (IOException e) {
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

    // Navigasi langsung ke Admin Dashboard
    private void navigateToAdminDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_dashboard.fxml"));
            AnchorPane adminDashboardRoot = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            // stage.setFullScreen(true);
            stage.setScene(new Scene(adminDashboardRoot, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the Admin Dashboard.");
        }
    }
}
