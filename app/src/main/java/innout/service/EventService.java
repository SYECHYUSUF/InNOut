package innout.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import innout.model.Event;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    private static final String EVENT_FILE = "events.json";

    public List<Event> muatSemuaEvent() {
        try (Reader reader = new FileReader(EVENT_FILE)) {
            Gson gson = new Gson();
            Type eventListType = new TypeToken<List<Event>>() {}.getType();
            return gson.fromJson(reader, eventListType);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void simpanSemuaEvent(List<Event> eventList) {
        try (Writer writer = new FileWriter(EVENT_FILE)) {
            Gson gson = new Gson();
            gson.toJson(eventList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tambahEvent(Event event) {
        List<Event> eventList = muatSemuaEvent();
        eventList.add(event);
        simpanSemuaEvent(eventList);
    }

    public void hapusEvent(Event event) {
        List<Event> eventList = muatSemuaEvent();
        boolean isRemoved = eventList.removeIf(e -> e.equals(event));
        if (isRemoved) {
            simpanSemuaEvent(eventList);
        } else {
            System.out.println("Event tidak ditemukan untuk dihapus.");
        }
    }

    public void updateEvent(Event updatedEvent) {
        List<Event> eventList = muatSemuaEvent();
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getNamaEvent().equals(updatedEvent.getNamaEvent())) {
                eventList.set(i, updatedEvent);
                simpanSemuaEvent(eventList);
                return;
            }
        }
    }

    public boolean beliTiket(Event event, String pembeli) {
        if (event.isTiketAvailable()) {
            event.tambahPembeli(pembeli);
            updateEvent(event);
            return true;
        } else {
            return false;
        }
    }
}
