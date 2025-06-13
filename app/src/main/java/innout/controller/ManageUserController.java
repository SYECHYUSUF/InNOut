package innout.controller;

import java.io.IOException;

import innout.model.User;
import innout.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;

public class ManageUserController {

    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> emailColumn;  // Kolom untuk email
    @FXML
    private TableColumn<User, Void> deleteColumn;  // Kolom untuk tombol hapus (Void karena kita menggunakan Button)

    private UserService userService;
    private ObservableList<User> userList;

    public ManageUserController() {
        userService = new UserService();  // Gunakan UserService yang sudah ada
    }

    @FXML
    private void initialize() {
        // Setup kolom email untuk menampilkan email
        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        // Setup kolom hapus (dengan tombol hapus)
        deleteColumn.setCellFactory(column -> {
            return new TableCell<User, Void>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(e -> handleDeleteUser(getTableRow().getItem())); // Delete user ketika tombol diklik
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);  // Jika baris kosong, tidak tampilkan grafik
                    } else {
                        setGraphic(deleteButton);  // Jika ada data, tampilkan tombol hapus
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

    // Menambahkan user baru (misalnya tombol add new user)
    @FXML
    private void handleAddNewUserAction(ActionEvent event) {
        // Menampilkan form registrasi
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register_user.fxml"));
            Parent root = loader.load();

            // Mendapatkan stage (jendela) yang sedang aktif
            Stage stage = (Stage) userTableView.getScene().getWindow();
            Scene scene = new Scene(root);

            // Ganti scene untuk menampilkan form registrasi
            stage.setScene(scene);
            stage.setTitle("Register New User");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Tidak dapat membuka form registrasi.");
        }
    }

}
