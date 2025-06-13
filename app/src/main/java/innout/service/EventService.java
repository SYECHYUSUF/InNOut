package innout.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import innout.model.Event;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    private static final String EVENT_FILE = "events.json"; // Nama file tempat menyimpan event

    // Menyimpan event ke dalam file JSON
    public void simpanSemuaEvent(List<Event> eventList) {
        try (Writer writer = new FileWriter(EVENT_FILE)) {
            Gson gson = new Gson();
            gson.toJson(eventList, writer); // Serialize daftar event menjadi JSON
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Memuat semua event dari file JSON
    public List<Event> muatSemuaEvent() {
        try (Reader reader = new FileReader(EVENT_FILE)) {
            Gson gson = new Gson();
            Type eventListType = new TypeToken<List<Event>>() {}.getType(); // Tipe data untuk list event
            return gson.fromJson(reader, eventListType); // Deserialize JSON ke dalam list event
        } catch (FileNotFoundException e) {
            // Jika file belum ada, kembalikan list kosong
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Menambah event baru
    public void tambahEvent(Event event) {
        List<Event> eventList = muatSemuaEvent();
        eventList.add(event);
        simpanSemuaEvent(eventList);
    }

    // Menghapus event
    public void hapusEvent(Event event) {
        List<Event> eventList = muatSemuaEvent();
        eventList.remove(event);
        simpanSemuaEvent(eventList);
    }

    // Mengupdate event yang sudah ada
    public void updateEvent(Event updatedEvent) {
        List<Event> eventList = muatSemuaEvent();
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getNamaEvent().equals(updatedEvent.getNamaEvent())) {
                eventList.set(i, updatedEvent); // Ganti dengan event yang baru
                simpanSemuaEvent(eventList); // Simpan kembali ke file JSON
                return;
            }
        }
    }
}
