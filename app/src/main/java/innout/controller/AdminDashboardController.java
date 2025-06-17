package innout.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AdminDashboardController {
    @FXML private Button manageEventButton;
    @FXML private Button manageUserButton;
    @FXML private Button logoutButton;

    @FXML
    private void handleManageEvent(ActionEvent event) {
        System.out.println("Manage Event Button Pressed");
        showManageEventPage();
    }

    @FXML
    private void handleManageUser(ActionEvent event) {
        System.out.println("Manage User Button Pressed");
        showManageUserPage();
    }

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
