package model;

public class Presensi {
    private int userId;
	private String tanggal;

	public String getTanggal() {
		return tanggal;
	}

	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}


	private String waktuMasuk;
	public String getWaktuMasuk() {
		return waktuMasuk;
	}

	public void setWaktuMasuk(String waktuMasuk) {
		this.waktuMasuk = waktuMasuk;
	}


	private String waktuKeluar;
    public String getWaktuKeluar() {
		return waktuKeluar;
	}

	public void setWaktuKeluar(String waktuKeluar) {
		this.waktuKeluar = waktuKeluar;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	public Presensi(int userId, String tanggal, String waktuMasuk, String waktuKeluar) {
		this.userId = userId;
		this.tanggal = tanggal;
		this.waktuMasuk = waktuMasuk;
		this.waktuKeluar = waktuKeluar;
	}
}

