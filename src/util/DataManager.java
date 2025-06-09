import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<User> loadUsers() throws IOException {
        return objectMapper.readValue(new File("data/users.json"), List.class);
    }

    public static List<Presensi> loadPresensi() throws IOException {
        return objectMapper.readValue(new File("data/presensi.json"), List.class);
    }

    public static void saveUsers(List<User> users) throws IOException {
        objectMapper.writeValue(new File("data/users.json"), users);
    }

    public static void savePresensi(List<Presensi> presensi) throws IOException {
        objectMapper.writeValue(new File("data/presensi.json"), presensi);
    }
}