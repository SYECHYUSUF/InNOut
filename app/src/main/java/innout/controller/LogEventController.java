package innout.controller;

import innout.model.Event;
import innout.service.EventService;
import innout.service.AttendanceService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.List;

public class LogEventController {

    @FXML
    private TilePane eventTilePane; // Pane untuk menampung kartu event yang dihadiri

    private EventService eventService = new EventService(); // Service untuk mengambil data event
    private AttendanceService attendanceService = new AttendanceService(); // Service untuk memeriksa kehadiran

    // Metode ini dipanggil saat halaman Log Event dimuat
    public void initialize() {
        loadAttendedEvents(); // Menampilkan event yang sudah dihadiri
    }

    // Menampilkan event yang sudah dihadiri oleh pengguna
    private void loadAttendedEvents() {
        String userEmail = "user@example.com"; // Ganti dengan email pengguna yang sedang login
        List<Event> attendedEvents = eventService.muatSemuaEvent(); // Ambil semua event atau filter yang sudah dibeli oleh user

        // Clear semua kartu event yang ada sebelumnya
        eventTilePane.getChildren().clear();

        // Loop untuk menampilkan kartu hanya untuk event yang sudah dihadiri
        for (Event event : attendedEvents) {
            // Cek apakah pengguna sudah hadir pada event ini
            if (attendanceService.checkAttendance(userEmail, event.getNamaEvent())) {
                // Jika sudah hadir, buat kartu event
                StackPane eventCard = createEventCard(event);
                eventTilePane.getChildren().add(eventCard);
            }
        }
    }

    // Membuat kartu event untuk satu event yang dihadiri
    private StackPane createEventCard(Event event) {
        StackPane card = new StackPane();

		VBox vbox = new VBox(10);

        Label eventName = new Label(event.getNamaEvent());
        eventName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label eventDate = new Label("Date: " + event.getTanggal());
        Label eventLocation = new Label("Location: " + event.getLokasi());

        // Tambahkan detail event pada kartu
        vbox.getChildren().addAll(eventName, eventDate, eventLocation);

		card.getChildren().add(vbox);

        card.setStyle("-fx-border-color: #000; -fx-border-width: 1; -fx-padding: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 5;");
        return card;
    }
}
