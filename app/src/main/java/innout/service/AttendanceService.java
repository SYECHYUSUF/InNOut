package innout.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import innout.model.Attendance;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class AttendanceService {
    private static final String ATTENDANCE_FILE = "attendance.json";  // Nama file data kehadiran

    // Memuat data kehadiran dari file attendance
    public List<Attendance> loadAttendanceData() {
        try (Reader reader = new FileReader(ATTENDANCE_FILE)) {
            Gson gson = new Gson();
            Type attendanceListType = new TypeToken<List<Attendance>>() {}.getType();
            return gson.fromJson(reader, attendanceListType);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Menyimpan data kehadiran ke file attendance
    public void saveAttendanceData(List<Attendance> attendanceList) {
        try (Writer writer = new FileWriter(ATTENDANCE_FILE)) {
            Gson gson = new Gson();
            gson.toJson(attendanceList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        // Fungsi untuk memeriksa apakah pengguna sudah hadir pada event tertentu
    public boolean checkAttendance(String userEmail, String eventName) {
        // Muat data kehadiran dari file
        List<Attendance> attendanceList = loadAttendanceData();

        // Mencari user berdasarkan email
        for (Attendance attendance : attendanceList) {
            if (attendance.getUser().equals(userEmail)) {
                // Cek jika event tersebut ada dalam daftar event yang dibeli oleh pengguna
                return attendance.getEvents().getOrDefault(eventName, false);
            }
        }
        return false; // Jika pengguna tidak ditemukan atau event tidak ada, return false (belum hadir)
    }

    // Memperbarui status kehadiran event untuk pengguna berdasarkan email
    public void updateAttendance(String userEmail, String eventName) {
        List<Attendance> attendanceList = loadAttendanceData();

        // Jika data attendance kosong atau tidak ada, buat data baru
        if (attendanceList == null) {
            attendanceList = new ArrayList<>();
        }

        boolean userFound = false;

        for (Attendance attendance : attendanceList) {
            if (attendance.getUser().equals(userEmail)) {
                // Update status kehadiran event
                Map<String, Boolean> events = attendance.getEvents();
                events.put(eventName, true);  // Set event attendance to true
                userFound = true;
                break;
            }
        }

        // Jika user tidak ditemukan, buat data baru untuk user
        if (!userFound) {
            Attendance newAttendance = new Attendance(userEmail, Map.of(eventName, true));
            attendanceList.add(newAttendance);
        }

        // Simpan data yang sudah diperbarui
        saveAttendanceData(attendanceList);
    }
}
