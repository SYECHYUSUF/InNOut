package innout.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event {

    private String namaEvent;
    private String tanggal;
    private String lokasi;
    private String deskripsi;
    private int jumlahTiket;  // Menambahkan jumlah tiket
    private List<String> pembeli;  // Menyimpan list pembeli yang telah membeli tiket

    // Konstruktor
    public Event(String namaEvent, String tanggal, String lokasi, String deskripsi, int jumlahTiket) {
        this.namaEvent = namaEvent;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.jumlahTiket = jumlahTiket;
        this.pembeli = new ArrayList<>();
    }

    // Getter dan Setter
    public String getNamaEvent() {
        return namaEvent;
    }

    public void setNamaEvent(String namaEvent) {
        this.namaEvent = namaEvent;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getJumlahTiket() {
        return jumlahTiket;
    }

    public void setJumlahTiket(int jumlahTiket) {
        this.jumlahTiket = jumlahTiket;
    }

    public List<String> getPembeli() {
        return pembeli;
    }

    public void tambahPembeli(String namaPembeli) {
        this.pembeli.add(namaPembeli);
    }

    public boolean isTiketAvailable() {
        return this.jumlahTiket > this.pembeli.size();  // Cek apakah masih ada tiket
    }

    @Override
    public String toString() {
        return "Event{" +
                "namaEvent='" + namaEvent + '\'' +
                ", tanggal='" + tanggal + '\'' +
                ", lokasi='" + lokasi + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", jumlahTiket=" + jumlahTiket +
                ", pembeli=" + pembeli +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(namaEvent, event.namaEvent) &&
               Objects.equals(tanggal, event.tanggal) &&
               Objects.equals(lokasi, event.lokasi) &&
               Objects.equals(deskripsi, event.deskripsi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namaEvent, tanggal, lokasi, deskripsi);
    }
}
