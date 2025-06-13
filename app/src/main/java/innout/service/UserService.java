package innout.service;

import innout.model.*;
import java.util.List;

public class UserService {
    private static User currentUser;
    private UserStorageService storageService = new UserStorageService();
    EventService eventService = new EventService();

    // Metode untuk login
    public boolean loginUser(String email, String password) {
        User user = storageService.getUserByEmail(email);
        currentUser = user;
        return user != null && user.getPassword().equals(password);
    }

    // Metode untuk registrasi pengguna baru
    public String registerUser(String email, String password) {
        User newUser = new User(email, password);
        boolean success = storageService.addUser(newUser);  // Cek jika registrasi berhasil

        if (success) {
            return "Registration successful! You can now log in.";
        } else {
            return "Email is already registered. Please choose another email.";
        }
    }

    // Mendapatkan daftar semua pengguna
    public List<User> getAllUsers() {
        return storageService.getAllUsers();  // Ambil semua user dari storage
    }

    // Menghapus pengguna
    public boolean deleteUser(User user) {
        return storageService.removeUser(user);  // Menghapus user dari storage
    }

    // Menampilkan event yang sudah dibeli oleh user
    public List<Event> getPurchasedEvents(String userEmail) {
        // Logika untuk mengambil event yang sudah dibeli oleh user
        // Kita asumsikan user memiliki daftar event yang dibeli
        return eventService.muatSemuaEvent();  // Ini hanya contoh, sesuaikan dengan data pembelian
    }

    public static String getCurrentUserEmail() {
        return currentUser.getEmail();
    }
}
