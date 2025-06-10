package model;

public class Admin extends User {
    private String usertype = "Admin";

    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getUsertype() {
        return usertype;
    }
}