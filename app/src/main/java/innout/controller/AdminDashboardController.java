package innout.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

// import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class AdminDashboardController {
    @FXML private Button manageEventButton;
    @FXML private Button manageUserButton;

    @FXML
    private void handleManageEvent(ActionEvent event) {
        // Tindakan ketika tombol Manage Event ditekan
        System.out.println("Manage Event Button Pressed");
        showManageEventPage(); // Tampilkan halaman manajemen event
    }

    @FXML
    private void handleManageUser(ActionEvent event) {
        // Tindakan ketika tombol Manage User ditekan
        System.out.println("Manage User Button Pressed");
        showManageUserPage(); // Tampilkan halaman manajemen user
    }

    private void showManageEventPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage_event.fxml"));
            BorderPane  eventRoot = loader.load();
            Stage stage = (Stage) manageEventButton.getScene().getWindow();
            stage.setScene(new Scene(eventRoot, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showManageUserPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage_user.fxml"));
            BorderPane userRoot = loader.load();
            Stage stage = (Stage) manageUserButton.getScene().getWindow();
            stage.setScene(new Scene(userRoot, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
