package innout.controller;

import innout.model.Event;
import innout.service.EventService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ManajemenEventController {

    @FXML private TableView<Event> eventTableView;
    @FXML private TableColumn<Event, String> kolomNamaEvent;
    @FXML private TableColumn<Event, String> kolomTanggal;
    @FXML private TableColumn<Event, String> kolomLokasi;
    @FXML private TableColumn<Event, String> kolomDeskripsi;
    @FXML private TableColumn<Event, Integer> kolomJumlahTiket;
    @FXML private TextField namaEventField;
    @FXML private DatePicker tanggalEventPicker;
    @FXML private TextField lokasiField;
    @FXML private TextField deskripsiField;
    @FXML private TextField jumlahTiketField;
    @FXML private Button tambahEventButton;
    @FXML private Button editEventButton;
    @FXML private Button hapusEventButton;
    @FXML private Button kembaliKeDashboardButton;

    private EventService eventService;
    private Event editEvent;
    private ObservableList<Event> daftarEventObservable;

    public ManajemenEventController() {
        this.eventService = new EventService();
    }

    @FXML
    public void initialize() {
        kolomNamaEvent.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNamaEvent()));
        kolomTanggal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTanggal()));
        kolomLokasi.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLokasi()));
        kolomDeskripsi.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDeskripsi()));
        kolomJumlahTiket.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getJumlahTiket()).asObject());


        kolomNamaEvent.prefWidthProperty().bind(eventTableView.widthProperty().multiply(0.25));
        kolomTanggal.prefWidthProperty().bind(eventTableView.widthProperty().multiply(0.15));
        kolomLokasi.prefWidthProperty().bind(eventTableView.widthProperty().multiply(0.20));
        kolomDeskripsi.prefWidthProperty().bind(eventTableView.widthProperty().multiply(0.25));
        kolomJumlahTiket.prefWidthProperty().bind(eventTableView.widthProperty().multiply(0.14));



        muatDanTampilkanEvent();

        eventTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        editEvent = newSelection;
                    } else {
                        editEvent = null;
                    }
                }
        );
    }

    private void muatDanTampilkanEvent() {
        daftarEventObservable = FXCollections.observableArrayList(eventService.muatSemuaEvent());
        eventTableView.setItems(daftarEventObservable);
    }

    @FXML
    private void handleTambahEventButtonAction(ActionEvent event) {
        String namaEvent = namaEventField.getText().trim();
        String tanggal = tanggalEventPicker.getValue().toString();
        String lokasi = lokasiField.getText().trim();
        String deskripsi = deskripsiField.getText().trim();
        String jumlahTiketStr = jumlahTiketField.getText().trim();
        int jumlahTiket = Integer.parseInt(jumlahTiketStr);

        if (namaEvent.isEmpty() || tanggal.isEmpty() || lokasi.isEmpty() || deskripsi.isEmpty() || jumlahTiketStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Semua field harus diisi.");
            return;
        }

        if (editEvent == null) {

            Event eventBaru = new Event(namaEvent, tanggal, lokasi, deskripsi, jumlahTiket);

            eventService.tambahEvent(eventBaru);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Event baru berhasil ditambahkan.");
        } else {
            editEvent.setNamaEvent(namaEvent);
            editEvent.setTanggal(tanggal);
            editEvent.setLokasi(lokasi);
            editEvent.setDeskripsi(deskripsi);
            editEvent.setJumlahTiket(jumlahTiket);
            eventService.updateEvent(editEvent);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Event berhasil diperbarui.");
        }

        muatDanTampilkanEvent();
        bersihkanForm();
    }

    @FXML
    private void handleEditEventButtonAction(ActionEvent event) {
        Event eventTerpilih = eventTableView.getSelectionModel().getSelectedItem();
        if (eventTerpilih != null) {
            String tanggalString = eventTerpilih.getTanggal();
            LocalDate tanggal = LocalDate.parse(tanggalString);

            namaEventField.setText(eventTerpilih.getNamaEvent());
            tanggalEventPicker.setValue(tanggal);
            lokasiField.setText(eventTerpilih.getLokasi());
            deskripsiField.setText(eventTerpilih.getDeskripsi());
            jumlahTiketField.setText(String.valueOf(eventTerpilih.getJumlahTiket()));
            tambahEventButton.setText("Simpan Perubahan");
        } else {
            showAlert(Alert.AlertType.WARNING, "Pilih Event", "Pilih event yang ingin diedit.");
        }
    }

    @FXML
    private void handleHapusEventButtonAction(ActionEvent event) {
        Event eventTerpilih = eventTableView.getSelectionModel().getSelectedItem();

        if (eventTerpilih != null) {
            String eventSingkat = String.format("%s", eventTerpilih.getNamaEvent());

            Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION);
            konfirmasi.setTitle("Konfirmasi Hapus");
            konfirmasi.setHeaderText("Hapus Event");
            konfirmasi.setContentText("Apakah Anda yakin ingin menghapus event: " + eventSingkat + "?");

            Optional<ButtonType> result = konfirmasi.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                eventService.hapusEvent(eventTerpilih);
                muatDanTampilkanEvent();
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Event berhasil dihapus.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih event yang akan dihapus dari tabel!");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void bersihkanForm() {
        namaEventField.clear();
        tanggalEventPicker.setValue(null);
        lokasiField.clear();
        deskripsiField.clear();
        jumlahTiketField.clear();
        tambahEventButton.setText("Tambah Event Baru");
    }

    @FXML
    private void handleKembaliKeDashboardButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak dapat membuka halaman Dashboard.");
        }
    }

}
