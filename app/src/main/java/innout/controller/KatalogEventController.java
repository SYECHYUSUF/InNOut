package innout.controller;

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
import innout.model.Attendance;
import innout.model.Event;
import innout.service.EventService;
import innout.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import innout.service.AttendanceService;

public class KatalogEventController {
    private AttendanceService attendanceService = new AttendanceService();

    @FXML
    private TilePane eventTilePane;

    private EventService eventService = new EventService();

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
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat dashboard: " + e.getMessage());
            showAlert("Navigasi Gagal", "Tidak dapat memuat halaman dashboard. Mohon hubungi administrator.");
        }
    }

    private void handleViewEventDetails(Event event) {
        Alert eventDetailAlert = new Alert(Alert.AlertType.INFORMATION);
        eventDetailAlert.setTitle("Detail Event");
        eventDetailAlert.setHeaderText(null);

        // Menambahkan stylesheet langsung ke DialogPane alert
        eventDetailAlert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        // Terapkan style class ke DialogPane Alert
        eventDetailAlert.getDialogPane().getStyleClass().add("dialog-pane");

        Label contentTextLabel = new Label(
                "Event: " + event.getNamaEvent() + "\n" +
                "Tanggal: " + event.getTanggal() + "\n" +
                "Lokasi: " + event.getLokasi() + "\n" +
                "Deskripsi: " + event.getDeskripsi() + "\n" +
                "Tiket Tersisa: " + (event.getJumlahTiket() - event.getPembeli().size())
        );
        contentTextLabel.setWrapText(true);
        contentTextLabel.getStyleClass().add("label");

        Button buyButton = new Button("Beli Tiket");
        buyButton.getStyleClass().add("button");
        buyButton.setOnAction(e -> {
            handleBuyTicket(event);
            eventDetailAlert.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("button");
        cancelButton.setOnAction(e -> eventDetailAlert.close());

        HBox buttonBox = new HBox(10, buyButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(contentTextLabel, buttonBox);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(10));

        eventDetailAlert.getDialogPane().setContent(dialogContent);

        eventDetailAlert.showAndWait();
    }

    private void handleBuyTicket(Event event) {
        if (event.isTiketAvailable()) {
            String emailPembeli = UserService.getCurrentUserEmail();
            if (emailPembeli != null && !emailPembeli.isEmpty()) {
                event.tambahPembeli(emailPembeli);
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
        // Muat data kehadiran dari file attendance
        List<Attendance> attendanceList = attendanceService.loadAttendanceData();

        // Cek jika user sudah tercatat di attendance
        Attendance userAttendance = null;
        for (Attendance attendance : attendanceList) {
            if (attendance.getUser().equals(userEmail)) {
                userAttendance = attendance;
                break;
            }
        }

        // Jika belum ada data untuk user, buat data baru
        if (userAttendance == null) {
            userAttendance = new Attendance(userEmail, new HashMap<>());
            attendanceList.add(userAttendance);
        }

        // Tambahkan event ke daftar event yang dibeli dan tandai belum hadir
        userAttendance.getEvents().put(event.getNamaEvent(), false);

        // Simpan perubahan ke dalam file attendance
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

        // Menambahkan stylesheet langsung ke DialogPane alert
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        // Terapkan gaya dialog-pane ke DialogPane Alert
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        // Perbaiki cara mendapatkan label konten bawaan Alert dan terapkan gaya
        Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.getStyleClass().add("label");
        }

        // Terapkan gaya button ke tombol OK bawaan Alert
        Button okButton = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        if (okButton != null) {
            okButton.getStyleClass().add("button");
        }

        alert.showAndWait();
    }
}