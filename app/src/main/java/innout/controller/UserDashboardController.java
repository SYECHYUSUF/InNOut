package innout.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class UserDashboardController {

    @FXML private Button katalogEventButton;
    @FXML private Button presentInButton;
    @FXML private Button logEventButton;
    @FXML private Button logoutButton;

    @FXML
    private void logoutHandle(ActionEvent event) {
        System.out.println("Logout Button Pressed");
        logOut();
    }

    private void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            VBox root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Login System");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogEventAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/log_event.fxml"));
            VBox logEventRoot = loader.load();
            Stage stage = (Stage) logEventButton.getScene().getWindow();
            Scene scene = new Scene(logEventRoot, 600, 400);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Terjadi masalah saat memuat halaman katalog event.");
        }
    }

    @FXML
    private void handleKatalogEventAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/katalog_event.fxml"));
            VBox katalogEventRoot = loader.load();
            Stage stage = (Stage) logEventButton.getScene().getWindow();
            Scene scene = new Scene(katalogEventRoot, 600, 400);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Terjadi masalah saat memuat halaman katalog event.");
        }
    }

    @FXML
    private void handlePresentInAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/present_in.fxml"));
            VBox presentInRoot = loader.load();
            Stage stage = (Stage) presentInButton.getScene().getWindow();
            Scene scene = new Scene(presentInRoot, 600, 400);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Terjadi masalah saat memuat halaman katalog event.");
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
