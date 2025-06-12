package innout.service;

import innout.model.User;

public class UserService {

    private UserStorageService storageService = new UserStorageService();

    // Metode untuk login
    public boolean loginUser(String email, String password) {
        User user = storageService.getUserByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    // Metode untuk registrasi pengguna baru
    public void registerUser(User user) {
        storageService.addUser(user);
    }
}
