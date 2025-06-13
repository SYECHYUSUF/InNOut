package innout.model;

public class User {
    private String email;
    private String password;

    // Konstruktor
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {}
    // Getter dan Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
