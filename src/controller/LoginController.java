package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AppContext;
import model.Pengguna;
import model.Admin;
import utils.FileUtils;

import java.util.List;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Cek apakah username dan password valid
        List<Map<String, Object>> users = FileUtils.loadUsers();

        for (Map<String, Object> user : users) {
            if (user.get("username").equals(username) && user.get("password").equals(password)) {
                String type = (String) user.get("type");

                if ("admin".equalsIgnoreCase(type)) {
                    AppContext.setCurrentUser(new Admin((int) user.get("idUser"), username, password));
                } else if ("pengguna".equalsIgnoreCase(type)) {
                    AppContext.setCurrentUser(new Pengguna((int) user.get("idUser"), username, password));
                }

                // Arahkan ke halaman dashboard yang sesuai
                try {
                    Parent dashboardRoot;
                    if ("admin".equalsIgnoreCase(type)) {
                        dashboardRoot = FXMLLoader.load(getClass().getResource("view/AdminDashboard.fxml"));
                    } else {
                        dashboardRoot = FXMLLoader.load(getClass().getResource("view/PenggunaDashboard.fxml"));
                    }

                    // Tambahkan stylesheet
                    dashboardRoot.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(dashboardRoot));
                    stage.setTitle("Dashboard");
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Terjadi kesalahan saat memuat dashboard");
                }

                return;
            }
        }

        // Jika login gagal, tampilkan alert
        showAlert("Login gagal, periksa username/password");
    }

    @FXML
    private void goToRegis() {
        try {
            // Memuat halaman registrasi
            Parent regisRoot = FXMLLoader.load(getClass().getResource("view/Registrasi.fxml"));
            regisRoot.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            // Menampilkan halaman registrasi
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(regisRoot));
            stage.setTitle("Registrasi");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Terjadi kesalahan saat memuat halaman registrasi");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
