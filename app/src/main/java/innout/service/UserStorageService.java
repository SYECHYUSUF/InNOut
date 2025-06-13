package innout.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import innout.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserStorageService {

    private static final String FILE_NAME = "users.json"; // Nama file JSON
    private static List<User> users = new ArrayList<>();
    private static final Gson gson = new Gson();

    // Memuat data pengguna dari file JSON
    static {
        loadUsers();
    }

    // Membaca data dari file JSON
    public static void loadUsers() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                users = gson.fromJson(reader, new TypeToken<List<User>>() {}.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Menyimpan data pengguna ke file JSON
    public static void saveUsers() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Menambahkan pengguna baru, jika email belum terdaftar
    public boolean addUser(User user) {
        // Cek apakah email sudah terdaftar
        if (getUserByEmail(user.getEmail()) != null) {
            return false; // Email sudah terdaftar, tidak bisa mendaftar lagi
        }
        users.add(user);
        saveUsers();
        return true; // Berhasil menambahkan user
    }

    // Mengambil pengguna berdasarkan email
    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user; // Jika ditemukan, return user
            }
        }
        return null; // Jika tidak ditemukan, return null
    }

    // Mengambil semua pengguna
    public List<User> getAllUsers() {
        return users;  // Mengembalikan daftar semua user
    }

    // Menghapus pengguna
    public boolean removeUser(User user) {
        boolean removed = users.remove(user);  // Menghapus user dari daftar
        if (removed) {
            saveUsers();  // Simpan perubahan setelah menghapus
        }
        return removed;
    }
}
