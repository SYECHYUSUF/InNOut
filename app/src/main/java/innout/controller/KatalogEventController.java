package innout.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import innout.model.Event;
import innout.service.EventService;
import innout.service.UserService;

import java.io.IOException;
import java.util.List;

public class KatalogEventController {

    @FXML
    private TilePane eventTilePane;  // Referensi ke TilePane yang ada di FXML

    private EventService eventService = new EventService();

    @FXML
    private void initialize() {
        List<Event> events = eventService.muatSemuaEvent();  // Memuat semua event
        for (Event event : events) {
            createEventCard(event);  // Membuat card untuk setiap event
        }
    }

    private void createEventCard(Event event) {
        VBox eventCard = new VBox(10);
        eventCard.setStyle("-fx-border-color: #000; -fx-padding: 10; -fx-background-color: #fff;");

        // Judul event
        Label eventTitle = new Label(event.getNamaEvent());
        eventTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Deskripsi dan detail
        Label eventDetails = new Label("Tanggal: " + event.getTanggal() + "\nLokasi: " + event.getLokasi() + "\nTiket Tersisa: " + (event.getJumlahTiket() - event.getPembeli().size()));

        // Tombol untuk melihat detail event
        Button detailButton = new Button("Lihat Detail");
        detailButton.setOnAction(e -> handleViewEventDetails(event));

        // Menambahkan elemen ke dalam card
        eventCard.getChildren().addAll(eventTitle, eventDetails, detailButton);

        // Pastikan card benar-benar ditambahkan ke TilePane
        eventTilePane.getChildren().add(eventCard);  // Add card to the TilePane

        // return eventCard;
    }

    // Menangani aksi tombol "Lihat Detail"
    private void handleViewEventDetails(Event event) {
        // Membuat alert untuk menampilkan detail event
        Alert eventDetailAlert = new Alert(Alert.AlertType.INFORMATION);
        eventDetailAlert.setTitle("Detail Event");
        eventDetailAlert.setHeaderText(event.getNamaEvent());

        // Menambahkan konten detail event
        eventDetailAlert.setContentText(
                "Tanggal: " + event.getTanggal() + "\n" +
                "Lokasi: " + event.getLokasi() + "\n" +
                "Deskripsi: " + event.getDeskripsi() + "\n" +
                "Tiket Tersisa: " + (event.getJumlahTiket() - event.getPembeli().size())
        );

        // Tombol "Beli" untuk membeli tiket
        Button buyButton = new Button("Beli Tiket");
        buyButton.setOnAction(e -> {
            handleBuyTicket(event);
            eventDetailAlert.close();  // Menutup alert setelah beli
        });

        // Tombol "Cancel" untuk menutup alert tanpa melakukan apa-apa
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> eventDetailAlert.close());

        // Mengatur button dalam sebuah HBox
        HBox buttonBox = new HBox(10, buyButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Menambahkan tombol-tombol ke dalam alert
        eventDetailAlert.getDialogPane().setContent(buttonBox);

        // Menampilkan alert
        eventDetailAlert.showAndWait();
    }

    // Menangani aksi "Beli Tiket"
    private void handleBuyTicket(Event event) {
        if (event.isTiketAvailable()) {
            // Menambahkan pembeli (misalnya, menggunakan nama pengguna yang sudah login)
            String emailPembeli = UserService.getCurrentUserEmail(); // Ganti dengan nama pengguna yang sudah login
            event.tambahPembeli(emailPembeli);

            // Menyimpan perubahan ke file
            eventService.updateEvent(event);

            muatDanTampilkanKatalogEvent();

            showAlert("Sukses", "Tiket untuk event " + event.getNamaEvent() + " berhasil dibeli!");
        } else {
            showAlert("Gagal", "Tiket untuk event " + event.getNamaEvent() + " sudah habis.");
        }
    }

    private void muatDanTampilkanKatalogEvent() {
        List<Event> eventList = eventService.muatSemuaEvent();  // Memuat daftar event yang terbaru dari file

        // Mengosongkan daftar event pada tampilan
        eventTilePane.getChildren().clear();

        List<Event> events = eventService.muatSemuaEvent();  // Memuat semua event
        for (Event event : events) {
            createEventCard(event);  // Membuat card untuk setiap event
        }
    }


    // Menampilkan alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

