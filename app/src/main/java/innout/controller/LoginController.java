package innout.controller;

import innout.service.UserService;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (UserService.loginUser(email, password)) {
            boolean isAdmin = UserService.isAdmin();

            if (isAdmin) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_dashboard.fxml"));
                    VBox adminDashboardRoot = loader.load();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(new Scene(adminDashboardRoot, 600, 400));
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Navigation Error", "Could not load the Admin Dashboard.");
                }
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user_dashboard.fxml"));
                    VBox userDashboardRoot = loader.load();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(new Scene(userDashboardRoot, 600, 400));
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Navigation Error", "Could not load the User Dashboard.");
                }
            }
        } else {
            showAlert("Login Failed", "Invalid credentials.");
        }
    }

    @FXML
    private void showRegisterForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            VBox registerRoot = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(registerRoot, 600, 400);

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

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
            getClass().getResource("/style.css").toExternalForm());

    alert.showAndWait();
    }
}
