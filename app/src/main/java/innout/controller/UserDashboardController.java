package innout.controller;

import java.io.IOException;
import java.util.List;

import innout.service.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import innout.model.Event;

public class UserDashboardController {

    // Tombol untuk menangani event klik
    @FXML private Button katalogEventButton;
    @FXML private Button presentInButton;
    @FXML private Button logEventButton;

    // @FXML
    // private void handleKatalogEventAction(ActionEvent event) {
    //     System.out.println("Katalog Event button clicked!");
    //     // Implement the navigation or action for Katalog Event
    // }

    @FXML
    private void handleLogEventAction(ActionEvent event) {
        System.out.println("Log Event button clicked!");
        // Implement the action for Log Event
    }

    // @FXML
    // private TilePane eventGrid;  // This will be your grid container

    // private EventService eventService = new EventService();

    // This method is triggered when the user clicks on "Katalog Event"
    @FXML
    private void handleKatalogEventAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/katalog_event.fxml"));
            VBox katalogEventRoot = loader.load();

            // Ambil stage saat ini dan set scene ke halaman katalog event
            Stage stage = (Stage) katalogEventButton.getScene().getWindow();
            Scene scene = new Scene(katalogEventRoot, 800, 600); // Atur ukuran sesuai kebutuhan
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();  // Menampilkan detail error jika file tidak ditemukan
            showAlert("Error", "Terjadi masalah saat memuat halaman katalog event.");
        }
    }

    @FXML
    private void handlePresentInAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/present_in.fxml"));
            VBox presentInRoot = loader.load();

            // Ambil stage saat ini dan set scene ke halaman katalog event
            Stage stage = (Stage) presentInButton.getScene().getWindow();
            Scene scene = new Scene(presentInRoot, 800, 600); // Atur ukuran sesuai kebutuhan
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();  // Menampilkan detail error jika file tidak ditemukan
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
