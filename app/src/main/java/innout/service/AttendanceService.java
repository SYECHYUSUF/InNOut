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

    public void saveAttendanceData(List<Attendance> attendanceList) {
        try (Writer writer = new FileWriter(ATTENDANCE_FILE)) {
            Gson gson = new Gson();
            gson.toJson(attendanceList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkAttendance(String userEmail, String eventName) {
        List<Attendance> attendanceList = loadAttendanceData();

        for (Attendance attendance : attendanceList) {
            if (attendance.getUser().equals(userEmail)) {
                return attendance.getEvents().getOrDefault(eventName, false);
            }
        }
        return false;
    }

    public void updateAttendance(String userEmail, String eventName, Boolean attend) {
        List<Attendance> attendanceList = loadAttendanceData();

        if (attendanceList == null) {
            attendanceList = new ArrayList<>();
        }

        boolean userFound = false;

        for (Attendance attendance : attendanceList) {
            if (attendance.getUser().equals(userEmail)) {
                Map<String, Boolean> events = attendance.getEvents();
                events.put(eventName, attend);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            Attendance newAttendance = new Attendance(userEmail, Map.of(eventName, true));
            attendanceList.add(newAttendance);
        }

        saveAttendanceData(attendanceList);
    }
}
