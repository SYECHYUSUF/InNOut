package innout.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import innout.model.Admin;
import innout.model.User; // Pastikan ini public abstract class User
import innout.model.Users; // Pastikan ini public class Users extends User

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserStorageService {

    private static final String FILE_NAME = "users.json";

    private static List<User> allUsers = new ArrayList<>();
    private static List<Users> regularUsers = new ArrayList<>();
    private static List<Admin> admins = new ArrayList<>();

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(User.class, new UserDeserializer()) // Ini KRUSIAL untuk handling abstrak User
            .create();

    static {
        loadUsers();
    }

    public static void loadUsers() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                // Di sini, TypeToken<List<User>>() sudah benar,
                // karena UserDeserializer yang terdaftar akan tahu cara membuat Admin atau Users.
                List<User> loadedUsers = gson.fromJson(reader, new TypeToken<List<User>>() {}.getType());

                allUsers.clear();
                regularUsers.clear();
                admins.clear();

                if (loadedUsers != null) {
                    allUsers.addAll(loadedUsers);

                    for (User user : allUsers) {
                        if (user instanceof Admin) {
                            admins.add((Admin) user);
                        } else if (user instanceof Users) {
                            regularUsers.add((Users) user);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading users from file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void saveUsers() {
        try (Writer writer = new FileWriter(FILE_NAME, StandardCharsets.UTF_8)) {
            gson.toJson(allUsers, writer);
        } catch (IOException e) {
            System.err.println("Error saving users to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean addUser(User user) {
        if (getUserByEmail(user.getEmail()) != null) {
            return false;
        }

        allUsers.add(user);
        if (user instanceof Admin) {
            admins.add((Admin) user);
        } else if (user instanceof Users) {
            regularUsers.add((Users) user);
        } else {
            System.err.println("Attempted to add an unknown user type: " + user.getClass().getName());
            allUsers.remove(user);
            return false;
        }

        saveUsers();
        return true;
    }

    public static User getUserByEmail(String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(allUsers);
    }

    public static List<Users> getRegularUsers() {
        return new ArrayList<>(regularUsers);
    }

    public static List<Admin> getAdmins() {
        return new ArrayList<>(admins);
    }

    public static boolean removeUser(User user) {
        boolean removed = allUsers.remove(user);
        if (removed) {
            if (user instanceof Admin) {
                admins.remove(user);
            } else if (user instanceof Users) {
                regularUsers.remove(user);
            }
            saveUsers();
        }
        return removed;
    }

    public static User toggleUserType(String email) {
        User existingUser = getUserByEmail(email);

        if (existingUser == null) {
            System.out.println("User dengan email " + email + " tidak ditemukan. Ada masalah dalam pembacaan data!");
            return null;
        }

        User newUser = null;

        if (existingUser instanceof Admin) {
            newUser = new Users(existingUser.getEmail(), existingUser.getPassword());
        } else if (existingUser instanceof Users) {
            newUser = new Admin(existingUser.getEmail(), existingUser.getPassword());
        } else {
            System.out.println("Unknown user type for " + email + ". Cannot toggle type.");
            return null;
        }
        
        allUsers.remove(existingUser);
        regularUsers.remove(existingUser);
        admins.remove(existingUser);

        allUsers.add(newUser);
        if (newUser instanceof Admin) {
            admins.add((Admin) newUser);
        } else if (newUser instanceof Users) {
            regularUsers.add((Users) newUser);
        }

        saveUsers();
        return newUser;
    }

}