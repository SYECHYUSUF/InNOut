package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class AdminDashboardController {

    @FXML
    private void handleRegisterAdmin() {
        try {
            // Memuat halaman registrasi admin
            Parent regisRoot = FXMLLoader.load(getClass().getResource("view/RegisterAdmin.fxml"));
            Stage stage = (Stage) (regisRoot.getScene().getWindow());
            stage.setScene(new Scene(regisRoot));
            stage.setTitle("Register Admin");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Terjadi kesalahan saat memuat halaman registrasi admin");
        }
    }

    @FXML
    private void handleManageUsers() {
        try {
            // Memuat halaman manajemen user
            Parent manageUsersRoot = FXMLLoader.load(getClass().getResource("view/ManageUsers.fxml"));
            Stage stage = (Stage) (manageUsersRoot.getScene().getWindow());
            stage.setScene(new Scene(manageUsersRoot));
            stage.setTitle("Manajemen User");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Terjadi kesalahan saat memuat halaman manajemen user");
        }
    }

    @FXML
    private void handleUserAttendanceHistory() {
        try {
            // Memuat halaman riwayat presensi user
            Parent attendanceHistoryRoot = FXMLLoader.load(getClass().getResource("view/UserAttendanceHistory.fxml"));
            Stage stage = (Stage) (attendanceHistoryRoot.getScene().getWindow());
            stage.setScene(new Scene(attendanceHistoryRoot));
            stage.setTitle("Riwayat Presensi User");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Terjadi kesalahan saat memuat halaman riwayat presensi user");
        }
    }

    private void showAlert(String message) {
        // Menampilkan alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

