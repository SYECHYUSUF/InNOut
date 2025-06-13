package innout.controller;

import java.io.IOException;

import innout.model.User;
import innout.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;

public class ManageUserController {

    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> emailColumn;  // Kolom untuk email
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, Void> deleteColumn;  // Kolom untuk tombol hapus (Void karena kita menggunakan Button)

    @FXML
    private Button kembaliButton;

    private UserService userService;
    private ObservableList<User> userList;

    public ManageUserController() {
        userService = new UserService();  // Gunakan UserService yang sudah ada
    }

    @FXML
    private void initialize() {

        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        // Setup kolom email untuk menampilkan email
        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        // Setup kolom password untuk menampilkan password <-- TAMBAHKAN BLOK INI
        passwordColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPassword()));

        // Setup kolom hapus (dengan tombol hapus)
        deleteColumn.setCellFactory(column -> {
            return new TableCell<User, Void>() {
                private final Button deleteButton = new Button("Delete");

                {
                    setAlignment(Pos.CENTER);
                    deleteButton.setOnAction(e -> handleDeleteUser(getTableRow().getItem()));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
        });

        // Muat daftar user dari service dan tampilkan di tabel
        loadUserList();
    }

    // Memuat dan menampilkan semua user
    private void loadUserList() {
        userList = FXCollections.observableArrayList(userService.getAllUsers());
        userTableView.setItems(userList);
    }

    // Menghapus user
    private void handleDeleteUser(User user) {
        if (user != null) {
            // Menggunakan service untuk menghapus user
            boolean isDeleted = userService.deleteUser(user);
            if (isDeleted) {
                userList.remove(user);  // Hapus user dari daftar di UI
                showAlert(AlertType.INFORMATION, "Success", "User berhasil dihapus.");
            } else {
                showAlert(AlertType.ERROR, "Error", "Gagal menghapus user.");
            }
        }
    }

    // Menampilkan alert
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
private void handleAddNewUserAction(ActionEvent event) {
    try {
        // 1. Muat FXML untuk form pop-up
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_user.fxml")); // Ganti path jika perlu
        Parent root = loader.load();

        // 2. Buat Stage (jendela) baru untuk pop-up
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add New User");
        dialogStage.setScene(new Scene(root));

        // 3. Atur agar window utama tidak bisa diklik sebelum pop-up ditutup
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(userTableView.getScene().getWindow());

        // 4. Tampilkan jendela pop-up dan tunggu sampai ditutup
        dialogStage.showAndWait();

        // 5. Setelah jendela ditutup, muat ulang data di tabel untuk menampilkan user baru
        loadUserList();

    } catch (IOException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error", "Could not open the registration form.");
    }
}

 @FXML
    private void handleKembaliButtonAction(ActionEvent event) {
        try {
            // Logika ini sama seperti di ManajemenEventController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) kembaliButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak dapat kembali ke Dashboard.");
        }
    }
}
