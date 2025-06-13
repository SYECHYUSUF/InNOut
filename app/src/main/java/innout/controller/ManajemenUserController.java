package innout.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import innout.model.User;
import innout.service.UserStorageService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ManajemenUserController implements Initializable {

    @FXML
    private TableView<User> userTableView;
    
    @FXML
    private TableColumn<User, String> kolomEmail;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kolomEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        loadUsersFromJson(); 
        userTableView.setItems(userList);
        kolomEmail.prefWidthProperty().bind(userTableView.widthProperty());
    }

    @FXML
    private void handleHapusUserButtonAction(ActionEvent event) {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Panggil metode static dari service untuk menghapus data secara permanen
            boolean isDeleted = UserStorageService.deleteUser(selectedUser);

            if (isDeleted) {
                // Jika berhasil dihapus di file, hapus juga dari tampilan tabel
                userTableView.getItems().remove(selectedUser);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "User berhasil dihapus.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus user dari file.");
            }

        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Silakan pilih user yang ingin dihapus terlebih dahulu.");
        }
    }
    @FXML
    private void handleKembaliKeDashboardButtonAction(ActionEvent event) throws IOException {
        Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/innout/view/AdminDashboard.fxml")); 
        Scene scene = new Scene(dashboardRoot);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * [FIX] Membaca file users.json dari path sistem file, bukan dari resources.
     */
    private void loadUsersFromJson() {
        // Menggunakan FileReader dengan path relatif dari root proyek
        // Ini akan bekerja jika Anda menjalankan aplikasi dari folder root proyek (InNOut)
        try (Reader reader = new FileReader("users.json")) {
            
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            Gson gson = new Gson();
            List<User> users = gson.fromJson(reader, userListType);
            
            // Pastikan list tidak null setelah parsing
            if (users != null) {
                userList.setAll(users);
            }
            
        } catch (FileNotFoundException e) {
            // Tangani error jika file tidak ditemukan secara spesifik
            showAlert(Alert.AlertType.ERROR, "Error", "File tidak ditemukan. Pastikan file ada di sana.");
            e.printStackTrace();
        } catch (Exception e) {
            // Tangani error lain saat membaca atau parsing file
            showAlert(Alert.AlertType.ERROR, "Error Membaca File", "Gagal memuat data dari users.json.");
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        alert.showAndWait();
    }
}