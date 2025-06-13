package innout.controller;

import innout.model.Event;
import innout.service.EventService;
import innout.service.AttendanceService;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;

import java.util.List;

public class PresentInEventController {

    @FXML
    private TilePane eventTilePane; // Pane to hold the event cards dynamically

    private EventService eventService = new EventService(); // Event service to load events
    private AttendanceService attendanceService = new AttendanceService(); // Attendance service to update attendance data

    // This method will be called when the view is loaded to populate event cards
    public void initialize() {
        loadPurchasedEvents();
    }

    // Method to load purchased events and create cards for them
    private void loadPurchasedEvents() {
        // Example: get events that the user has bought
        List<Event> purchasedEvents = eventService.muatSemuaEvent(); // Get all events or filter by user purchase data

        // Clear the existing event cards in the UI
        eventTilePane.getChildren().clear();

        // Get the current logged-in user email
        String userEmail = "user@example.com"; // Replace with the actual logged-in user's email

        // Filter the events that the user has not attended yet (status is false)
        for (Event event : purchasedEvents) {
            // Check if the user has already attended the event
            boolean hasAttended = attendanceService.checkAttendance(userEmail, event.getNamaEvent());

            if (!hasAttended) {
                // Create a card for the event
                StackPane eventCard = createEventCard(event);
                eventTilePane.getChildren().add(eventCard);
            }
        }
    }

    // Create a card for a single event
    private StackPane createEventCard(Event event) {
        StackPane card = new StackPane();
        VBox vbox = new VBox(10);

        // Create and add event details
        Label eventName = new Label(event.getNamaEvent());
        eventName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label eventDate = new Label("Date: " + event.getTanggal());
        Label eventLocation = new Label("Location: " + event.getLokasi());
        Label eventTickets = new Label("Tickets Left: " + (event.getJumlahTiket() - event.getPembeli().size()));

        // Add a "Present In" button
        Button presentInButton = new Button("Present In");
        presentInButton.setOnAction(e -> handlePresentIn(event));

        vbox.getChildren().addAll(eventName, eventDate, eventLocation, eventTickets, presentInButton);

        card.getChildren().add(vbox);
        card.setStyle("-fx-border-color: #000; -fx-border-width: 1; -fx-padding: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 5;");

        return card;
    }

    // Handle "Present In" button click
    private void handlePresentIn(Event event) {
        // Show a confirmation alert
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Event Attendance");
        alert.setHeaderText("Are you sure you want to mark your presence for this event?");
        alert.setContentText("Event: " + event.getNamaEvent() + "\nLocation: " + event.getLokasi());

        ButtonType buttonTypeConfirm = new ButtonType("Confirm");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(buttonTypeConfirm, buttonTypeCancel);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeConfirm) {
                // Get the current logged-in user email
                String userEmail = "user@example.com"; // Replace with the actual logged-in user's email

                // Update attendance status for the event
                attendanceService.updateAttendance(userEmail, event.getNamaEvent()); // Mark attendance as true

                loadPurchasedEvents();

                // Show a success message
                showAlert("Success", "You have successfully marked your presence for the event: " + event.getNamaEvent());
            }
        });
    }

    // Helper method to show alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
