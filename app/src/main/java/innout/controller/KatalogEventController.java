package innout.controller;

import innout.model.Attendance;
import innout.model.Event;
import innout.service.EventService;
import innout.service.UserService;
import innout.service.AttendanceService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.control.ButtonType;

public class KatalogEventController {
    private AttendanceService attendanceService = new AttendanceService();
    private EventService eventService = new EventService();

    @FXML private TilePane eventTilePane;

    @FXML
    private void initialize() {
        muatDanTampilkanKatalogEvent();
    }

    private void createEventCard(Event event) {
        VBox eventCard = new VBox(10);
        eventCard.getStyleClass().add("card");
        eventCard.setAlignment(Pos.TOP_LEFT);
        eventCard.setPadding(new Insets(15));

        Label eventTitle = new Label(event.getNamaEvent());
        eventTitle.getStyleClass().add("card-title");

        Label eventDetails = new Label(
            "Tanggal: " + event.getTanggal() +
            "\nLokasi: " + event.getLokasi() +
            "\nTiket Tersisa: " + (event.getJumlahTiket() - event.getPembeli().size())
        );
        eventDetails.setWrapText(true);
        eventDetails.getStyleClass().add("label");

        Button detailButton = new Button("Lihat Detail");
        detailButton.getStyleClass().add("button");
        detailButton.setOnAction(e -> handleViewEventDetails(event));
        detailButton.setMaxWidth(Double.MAX_VALUE);

        eventCard.getChildren().addAll(eventTitle, eventDetails, detailButton);
        eventTilePane.getChildren().add(eventCard);
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
            showAlert("Navigasi Gagal", "Tidak dapat memuat halaman dashboard. Mohon hubungi admin.");
        }
    }

    private void handleViewEventDetails(Event event) {
        Alert eventDetailAlert = new Alert(Alert.AlertType.NONE);
        eventDetailAlert.setTitle("Detail Event");
        eventDetailAlert.setHeaderText(null);

        String cssPath = getClass().getResource("/style.css") != null ?
                         getClass().getResource("/style.css").toExternalForm() : null;
        if (cssPath != null) {
            eventDetailAlert.getDialogPane().getStylesheets().add(cssPath);
            eventDetailAlert.getDialogPane().getStyleClass().add("dialog-pane");
        }

        Label contentTextLabel = new Label(
                "Event: " + event.getNamaEvent() + "\n" +
                "Tanggal: " + event.getTanggal() + "\n" +
                "Lokasi: " + event.getLokasi() + "\n" +
                "Deskripsi: " + event.getDeskripsi() + "\n" +
                "Tiket Tersisa: " + (event.getJumlahTiket() - event.getPembeli().size())
        );
        contentTextLabel.setWrapText(true);
        contentTextLabel.getStyleClass().add("label");

        ButtonType buyButtonType = new ButtonType("Beli Tiket");
        ButtonType cancelButtonType = ButtonType.CANCEL;

        Button buyButton = new Button("Beli Tiket");
        buyButton.getStyleClass().add("button");
        buyButton.setOnAction(e -> {
            try {
                eventDetailAlert.setResult(buyButtonType);
                eventDetailAlert.close();
            } catch (Exception ex) {
                System.err.println("Error closing alert for buy: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("button");
        cancelButton.setOnAction(e -> {
            try {
                eventDetailAlert.setResult(cancelButtonType);
                eventDetailAlert.close();
            } catch (Exception ex) {
                System.err.println("Error closing alert for cancel: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(10, buyButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(contentTextLabel, buttonBox);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(10));

        eventDetailAlert.getDialogPane().setContent(dialogContent);

        Optional<ButtonType> result = eventDetailAlert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == buyButtonType) {
                handleBuyTicket(event);
            }
        }
    }

    private void handleBuyTicket(Event event) {
        if (event.isTiketAvailable()) {
            String emailPembeli = UserService.getCurrentUserEmail();
            if (emailPembeli != null && !emailPembeli.isEmpty()) {
                if (event.getPembeli().contains(emailPembeli)) {
                    showAlert("Gagal", "Anda sudah membeli tiket untuk event " + event.getNamaEvent() + ". Satu pengguna hanya bisa membeli satu tiket untuk event yang sama.");
                    return;
                }

                event.tambahPembeli(emailPembeli);
                markAttendance(emailPembeli, event);
                eventService.updateEvent(event);
                showAlert("Sukses", "Tiket untuk event " + event.getNamaEvent() + " berhasil dibeli!");
                muatDanTampilkanKatalogEvent();
            } else {
                showAlert("Gagal", "Tidak dapat membeli tiket. Pengguna belum login atau email tidak tersedia.");
            }
        } else {
            showAlert("Gagal", "Tiket untuk event " + event.getNamaEvent() + " sudah habis.");
        }
    }

    private void markAttendance(String userEmail, Event event) {
        List<Attendance> attendanceList = attendanceService.loadAttendanceData();

        Attendance userAttendance = null;
        for (Attendance attendance : attendanceList) {
            if (attendance.getUser().equals(userEmail)) {
                userAttendance = attendance;
                break;
            }
        }

        if (userAttendance == null) {
            userAttendance = new Attendance(userEmail, new HashMap<>());
            attendanceList.add(userAttendance);
        }

        userAttendance.getEvents().put(event.getNamaEvent(), false);
        attendanceService.saveAttendanceData(attendanceList);
    }


    private void muatDanTampilkanKatalogEvent() {
        eventTilePane.getChildren().clear();
        List<Event> events = eventService.muatSemuaEvent();
        for (Event event : events) {
            createEventCard(event);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.getStyleClass().add("label");
        }

        Button okButton = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        if (okButton != null) {
            okButton.getStyleClass().add("button");
        }

        alert.showAndWait();
    }
}