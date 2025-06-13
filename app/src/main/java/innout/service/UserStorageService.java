package innout.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import innout.model.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserStorageService {

    private static final String FILE_NAME = "users.json"; // Nama file JSON
    private static List<User> users = new ArrayList<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Memuat data pengguna dari file JSON saat class dijalankan
    static {
        loadUsers();
    }

    // Membaca data dari file JSON
    public static void loadUsers() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                List<User> loadedUsers = gson.fromJson(reader, new TypeToken<List<User>>() {}.getType());
                if (loadedUsers != null) {
                    users = loadedUsers;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Menyimpan data pengguna ke file JSON
    public static void saveUsers() {
        try (Writer writer = new FileWriter(FILE_NAME, StandardCharsets.UTF_8)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * [FIX] Diubah menjadi metode static untuk konsistensi.
     * Menambahkan pengguna baru, jika email belum terdaftar.
     */
    public static boolean addUser(User user) {
        if (getUserByEmail(user.getEmail()) != null) {
            return false; // Email sudah terdaftar
        }
        users.add(user);
        saveUsers(); // Langsung simpan perubahan
        return true;
    }

    /**
     * [FIX] Diubah menjadi metode static untuk konsistensi.
     * Mengambil pengguna berdasarkan email.
     */
    public static User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * [BARU] Method untuk menghapus user dan menyimpan perubahan ke file JSON.
     * @param userToDelete User yang akan dihapus.
     * @return true jika user ditemukan dan berhasil dihapus.
     */
    public static boolean deleteUser(User userToDelete) {
        boolean removed = users.removeIf(user -> user.getEmail().equals(userToDelete.getEmail()));
        if (removed) {
            saveUsers(); // Jika berhasil dihapus dari list, simpan perubahannya ke file
        }
        return removed;
    }

    /**
     * [BARU] Method untuk mendapatkan semua user, berguna untuk controller.
     * @return List semua user.
     */
    public static List<User> getAllUsers() {
        return users;
    }
}