package innout.model;

public class Admin extends User {
	private String typeUser;

	public Admin(String email, String password) {
		super(email, password);
		this.typeUser = "Admin";
	}

	@Override
	public String getTypeUser() {
		return typeUser;
	}
}
