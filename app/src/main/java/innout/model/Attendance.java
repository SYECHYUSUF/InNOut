package innout.model;

import java.util.Map;

public class Attendance {
    private String user; // Email pengguna
    private Map<String, Boolean> events; // Map yang menyimpan nama event dan status kehadirannya

    // Konstruktor
    public Attendance(String user, Map<String, Boolean> events) {
        this.user = user;
        this.events = events;
    }

    // Getter dan Setter
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Map<String, Boolean> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Boolean> events) {
        this.events = events;
    }

    // Menambahkan status kehadiran untuk sebuah event
    public void addEventAttendance(String eventName, boolean isPresent) {
        this.events.put(eventName, isPresent);
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "user='" + user + '\'' +
                ", events=" + events +
                '}';
    }
}
