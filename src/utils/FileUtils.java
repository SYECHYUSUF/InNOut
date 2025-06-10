package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileUtils {
    private static final String USERS_FILE = "data/users.json";

    // Membaca data pengguna dari file JSON
    public static List<Map<String, Object>> loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(USERS_FILE)));
            JSONArray jsonArray = new JSONArray(content);
            List<Map<String, Object>> users = new ArrayList<>();

            // Mengonversi JSONArray ke dalam List<Map<String, Object>>
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> user = new HashMap<>();
                user.put("idUser", jsonObject.getInt("idUser"));
                user.put("username", jsonObject.getString("username"));
                user.put("password", jsonObject.getString("password"));
                user.put("type", jsonObject.getString("type")); // Menambahkan tipe user (admin/pengguna)
                users.add(user);
            }

            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Menyimpan data pengguna ke file JSON
    public static void saveUsers(List<Map<String, Object>> users) {
        File file = new File(USERS_FILE);
        file.getParentFile().mkdirs(); // Membuat folder jika belum ada

        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> user : users) {
            JSONObject jsonObject = new JSONObject(user);
            jsonArray.put(jsonObject);
        }

        try {
            Files.write(Paths.get(USERS_FILE), jsonArray.toString(4).getBytes()); // pretty print JSON
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Menyimpan pengguna baru ke dalam file
    public static void saveNewUser(Map<String, Object> newUser) {
        List<Map<String, Object>> users = loadUsers();
        users.add(newUser);
        saveUsers(users);
    }

    // Mengecek apakah username sudah ada di dalam file
    public static boolean isUsernameExists(String username) {
        List<Map<String, Object>> users = loadUsers();
        for (Map<String, Object> user : users) {
            if (user.get("username").equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Fungsi untuk mendapatkan idUser terakhir
    public static int getLastUserId() {
        List<Map<String, Object>> users = loadUsers();

        if (users.isEmpty()) {
            return 0; // Mengembalikan null jika tidak ada pengguna
        }

        // Menemukan idUser tertinggi
        int lastUserId = 0;
        for (Map<String, Object> user : users) {
            int idUser = (int) user.get("idUser");
            if (idUser > lastUserId) {
                lastUserId = idUser;
            }
        }

        return lastUserId;
    }
}
