package innout.controller;

import innout.model.User;
import innout.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserFormController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private UserService userService = new UserService();

    // Metode ini akan dipanggil saat tombol "Register" ditekan
   @FXML
    void handleRegisterAction(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Email and password cannot be empty.");
            return;
        }

        String registrationResult = userService.registerUser(email, password);

        // --- UBAH KONDISI IF DI SINI ---
        // Cocokkan dengan pesan sukses yang sebenarnya dari service Anda.
        if ("Registration successful! You can now log in.".equalsIgnoreCase(registrationResult)) {
            
            // Sekarang blok ini akan dijalankan saat sukses
            // Kita bisa gunakan pesan suksesnya langsung di alert
            showAlert(Alert.AlertType.INFORMATION, "Success", registrationResult); 
            
            closeWindow(event);

        } else {
            // Blok ini sekarang hanya akan dijalankan jika ada pesan error asli
            showAlert(Alert.AlertType.ERROR, "Registration Failed", registrationResult);
        }
    }

    // Metode ini akan dipanggil saat tombol "Cancel" ditekan
    @FXML
    void handleCancelAction(ActionEvent event) {
        closeWindow(event);
    }

    // Metode bantuan untuk menutup jendela
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    // Metode bantuan untuk menampilkan alert
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}