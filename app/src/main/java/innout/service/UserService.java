package innout.service;

import innout.model.*;
import java.util.List;

public class UserService {
    private static User currentUser;
    EventService eventService = new EventService();

    public static boolean loginUser(String email, String password) {
        User user = UserStorageService.getUserByEmail(email);
        currentUser = user;
        return user != null && user.getPassword().equals(password);
    }

    public static boolean registerUser(String email, String password) {
        User newUser = new Users(email, password);
        return UserStorageService.addUser(newUser);
    }

    public List<User> getAllUsers() {
        return UserStorageService.getAllUsers();
    }

    public boolean deleteUser(User user) {
        return UserStorageService.removeUser(user);
    }

    public static void logoutUser() {
        if (currentUser != null) {
            currentUser = null;
        } else {
            System.out.println("No user was logged in.");
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentUserEmail() {
        return currentUser.getEmail();
    }

    public static boolean isAdmin() {
        if (currentUser == null) {
            return false;
        }

        return currentUser instanceof Admin;
    }
}
