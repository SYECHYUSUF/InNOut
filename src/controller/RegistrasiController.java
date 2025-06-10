package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import utils.FileUtils;

// import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RegistrasiController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField idUserField;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        int idUser = FileUtils.getLastUserId() + 1;

        // Validasi input
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Semua field harus diisi!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Password dan konfirmasi password tidak cocok");
            return;
        }

        // Mengecek apakah username sudah digunakan
        if (FileUtils.isUsernameExists(username)) {
            showAlert("Username sudah terdaftar");
            return;
        }

        // Menyimpan data pengguna baru
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("idUser", idUser);
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("type", "pengguna"); // Default type adalah pengguna, bisa diganti sesuai kebutuhan

        FileUtils.saveNewUser(newUser);

        // Menampilkan pesan sukses
        showAlert("Registrasi berhasil! Silakan login.");
        backToLogin();

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void backToLogin() {
        // Kembali ke halaman login
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Terjadi kesalahan saat kembali ke halaman login");
        }
    }
}
