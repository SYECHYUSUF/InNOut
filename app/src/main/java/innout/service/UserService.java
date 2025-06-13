package innout.service;

import innout.model.User;
import java.util.List;

public class UserService {

    private UserStorageService storageService = new UserStorageService();

    // Metode untuk login
    public boolean loginUser(String email, String password) {
        User user = storageService.getUserByEmail(email);
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
}
