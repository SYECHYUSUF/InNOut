package model;

public class Pengguna extends User {
    private String usertype = "Pengguna";

    public Pengguna(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getUsertype() {
        return usertype;
    }
}