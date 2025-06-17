package innout.controller;

import innout.model.Event;
import innout.service.EventService;
import innout.service.AttendanceService;
import innout.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PresentInEventController {

    @FXML
    private TilePane eventTilePane;

    private EventService eventService = new EventService();
    private AttendanceService attendanceService = new AttendanceService();

    @FXML
    public void initialize() {
        loadPurchasedEvents();
    }

    private void loadPurchasedEvents() {
        List<Event> allEvents = eventService.muatSemuaEvent();
        eventTilePane.getChildren().clear();

        String userEmail = UserService.getCurrentUserEmail();
        System.out.println("DEBUG: User email saat ini: " + userEmail);

        if (userEmail == null || userEmail.isEmpty()) {
            showAlert("Error", "Anda harus login untuk melihat event yang sudah dibeli.");
            return;
        }

        for (Event event : allEvents) {
            System.out.println("DEBUG: Memproses event: " + event.getNamaEvent());
            System.out.println("DEBUG: Pembeli event " + event.getNamaEvent() + ": " + event.getPembeli());

            boolean isPurchasedByUser = event.getPembeli().contains(userEmail);
            System.out.println("DEBUG: Event " + event.getNamaEvent() + " dibeli oleh " + userEmail + "? " + isPurchasedByUser);

            boolean hasAttended = attendanceService.checkAttendance(userEmail, event.getNamaEvent());
            System.out.println("DEBUG: User " + userEmail + " sudah hadir di " + event.getNamaEvent() + "? " + hasAttended);

            if (isPurchasedByUser && !hasAttended) {
                System.out.println("DEBUG: Menampilkan event: " + event.getNamaEvent());
                StackPane eventCard = createEventCard(event);
                eventTilePane.getChildren().add(eventCard);
            } else {
                System.out.println("DEBUG: TIDAK menampilkan event: " + event.getNamaEvent());
            }
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
        Label eventTickets = new Label("Tiket Tersisa: " + (event.getJumlahTiket() - event.getPembeli().size()));
        eventTickets.getStyleClass().add("label");
        eventTickets.setWrapText(true);

        Button presentInButton = new Button("Present In");
        presentInButton.getStyleClass().add("button");
        presentInButton.setOnAction(e -> handlePresentIn(event));
        presentInButton.setMaxWidth(Double.MAX_VALUE);

        vbox.getChildren().addAll(eventName, eventDate, eventLocation, eventTickets, presentInButton);

        card.getChildren().add(vbox);

        return card;
    }

    private void handlePresentIn(Event event) { // Tambahkan ActionEvent parameter
        Alert alert = new Alert(AlertType.NONE);
        alert.setTitle("Konfirmasi Kehadiran Event");
        alert.setHeaderText(null);

        // --- Setel Owner Window (penting!) ---
        // Stage ownerStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        // if (ownerStage != null) {
        //     alert.initOwner(ownerStage);
        // }

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        Label contentTextLabel = new Label("Apakah Anda yakin ingin menandai kehadiran Anda untuk event ini?\n\nEvent: " + event.getNamaEvent() + "\nLokasi: " + event.getLokasi());
        contentTextLabel.setWrapText(true);
        contentTextLabel.getStyleClass().add("label");
        contentTextLabel.setAlignment(Pos.CENTER);

        // --- Definisikan ButtonType kustom ---
        ButtonType confirmButtonType = new ButtonType("Konfirmasi");
        ButtonType cancelButtonType = ButtonType.CANCEL; // Menggunakan ButtonType.CANCEL standar

        // --- Tambahkan ButtonType ini ke DialogPane Alert (Ini Kunci!) ---
        // alert.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

        // --- Buat Tombol Visual Kustom ---
        Button buttonConfirm = new Button("Konfirmasi");
        buttonConfirm.getStyleClass().add("button");
        Button buttonCancel = new Button("Batal");
        buttonCancel.getStyleClass().add("button");

        // --- Atur Aksi untuk Tombol Visual ---
        buttonConfirm.setOnAction(e -> {
            // Ketika tombol diklik, set hasil Alert dan kemudian tutup Alert
            alert.setResult(confirmButtonType);
            alert.close();
        });

        buttonCancel.setOnAction(e -> {
            // Ketika tombol dibatalkan, set hasil Alert dan kemudian tutup Alert
            alert.setResult(cancelButtonType);
            alert.close();
        });

        HBox buttonBox = new HBox(10, buttonConfirm, buttonCancel);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(contentTextLabel, buttonBox);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(10));

        alert.getDialogPane().setContent(dialogContent);

        // --- Tampilkan Alert dan Tangani Hasilnya Setelah Ditutup ---
        Optional<ButtonType> result = alert.showAndWait();

        // Logika bisnis dijalankan HANYA JIKA Alert berhasil ditutup dan tombol Konfirmasi dipilih
        if (result.isPresent() && result.get() == confirmButtonType) {
            String userEmail = null;
            if (UserService.getCurrentUserEmail() != null) {
                userEmail = UserService.getCurrentUserEmail(); // Menggunakan metode yang benar
            }

            if (userEmail != null && !userEmail.isEmpty()) {
                attendanceService.updateAttendance(userEmail, event.getNamaEvent(), true); // Asumsi AttendanceService ada
                loadPurchasedEvents(); // Muat ulang daftar event setelah kehadiran ditandai
                showAlert("Sukses", "Anda telah berhasil menandai kehadiran Anda untuk event: " + event.getNamaEvent());
            } else {
                showAlert("Gagal", "Tidak dapat menandai kehadiran. Pengguna belum login atau email tidak tersedia.");
            }
        } else {
            // User membatalkan atau menutup dialog tanpa konfirmasi
            System.out.println("Penandaan kehadiran dibatalkan.");
        }
    }


    // private void handlePresentIn(Event event) {
    //     Alert alert = new Alert(AlertType.NONE);
    //     alert.setTitle("Konfirmasi Kehadiran Event");
    //     alert.setHeaderText(null);

    //     alert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    //     alert.getDialogPane().getStyleClass().add("dialog-pane");

    //     Label contentTextLabel = new Label("Apakah Anda yakin ingin menandai kehadiran Anda untuk event ini?\n\nEvent: " + event.getNamaEvent() + "\nLokasi: " + event.getLokasi());
    //     contentTextLabel.setWrapText(true);
    //     contentTextLabel.getStyleClass().add("label");
    //     contentTextLabel.setAlignment(Pos.CENTER);

    //     Button buttonConfirm = new Button("Konfirmasi");
    //     buttonConfirm.getStyleClass().add("button");
    //     Button buttonCancel = new Button("Batal");
    //     buttonCancel.getStyleClass().add("button");

    //     buttonConfirm.setOnAction(e -> {
    //         String userEmail = UserService.getCurrentUserEmail();
    //         if (userEmail != null && !userEmail.isEmpty()) {
    //             attendanceService.updateAttendance(userEmail, event.getNamaEvent(), true);
    //             loadPurchasedEvents();
    //             showAlert("Sukses", "Anda telah berhasil menandai kehadiran Anda untuk event: " + event.getNamaEvent());
    //         } else {
    //             showAlert("Gagal", "Tidak dapat menandai kehadiran. Pengguna belum login atau email tidak tersedia.");
    //         }
    //         alert.close();
    //     });

    //     buttonCancel.setOnAction(e -> alert.close());

    //     HBox buttonBox = new HBox(10, buttonConfirm, buttonCancel);
    //     buttonBox.setAlignment(Pos.CENTER);
    //     buttonBox.setPadding(new Insets(15, 0, 0, 0));

    //     VBox dialogContent = new VBox(10);
    //     dialogContent.getChildren().addAll(contentTextLabel, buttonBox);
    //     dialogContent.setAlignment(Pos.CENTER);
    //     dialogContent.setPadding(new Insets(10));

    //     alert.getDialogPane().setContent(dialogContent);

    //     alert.showAndWait();
    // }

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
            System.err.println("Gagal memuat halaman dashboard: " + e.getMessage());
            showAlert("Navigasi Gagal", "Tidak dapat memuat halaman dashboard. Mohon hubungi administrator.");
        }
    }
}