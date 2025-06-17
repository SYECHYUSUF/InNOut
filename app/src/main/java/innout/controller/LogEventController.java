package innout.controller;

import innout.model.Event;
import innout.service.EventService;
import innout.service.AttendanceService;
import innout.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;
import java.util.Collections;

public class LogEventController {

    @FXML
    private TilePane eventTilePane;

    private EventService eventService = new EventService();
    private AttendanceService attendanceService = new AttendanceService();

    public void initialize() {
        loadAttendedEvents();
    }

    private void loadAttendedEvents() {
        String userEmail = UserService.getCurrentUserEmail();

        if (userEmail == null || userEmail.isEmpty()) {
            showAlert("Error", "Anda harus login untuk melihat log event.");
            return;
        }

        List<Event> allEvents = eventService.muatSemuaEvent();
        eventTilePane.getChildren().clear();

        if (allEvents.isEmpty()) {
        }

        for (Event event : allEvents) {
            List<String> eventPembeli = (event.getPembeli() != null) ? event.getPembeli() : Collections.emptyList();

            boolean isPurchasedByUser = eventPembeli.contains(userEmail);
            boolean hasAttended = attendanceService.checkAttendance(userEmail, event.getNamaEvent());

            if (isPurchasedByUser && hasAttended) {
                StackPane eventCard = createEventCard(event);
                eventTilePane.getChildren().add(eventCard);
            }
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user_dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat dashboard: " + e.getMessage());
            showAlert("Navigasi Gagal", "Tidak dapat memuat halaman dashboard. Mohon hubungi administrator.");
        }
    }

    private StackPane createEventCard(Event event) {
        StackPane card = new StackPane();
        card.getStyleClass().add("card");

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setPadding(new Insets(15));

        Label eventName = new Label(event.getNamaEvent());
        eventName.getStyleClass().add("card-title");

        Label eventDate = new Label("Tanggal: " + event.getTanggal());
        eventDate.getStyleClass().add("label");
        Label eventLocation = new Label("Lokasi: " + event.getLokasi());
        eventLocation.getStyleClass().add("label");

        vbox.getChildren().addAll(eventName, eventDate, eventLocation);
        card.getChildren().add(vbox);
        return card;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setHeaderText(null);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        Label customContentLabel = new Label(message);
        customContentLabel.setWrapText(true);
        customContentLabel.getStyleClass().add("label");
        customContentLabel.setAlignment(Pos.CENTER);

        Button okButton = new Button("OK");
        okButton.getStyleClass().add("button");
        okButton.setOnAction(e -> alert.close());

        HBox buttonBox = new HBox(10, okButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(customContentLabel, buttonBox);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(10));

        alert.getDialogPane().setContent(dialogContent);

        alert.showAndWait();
    }
}