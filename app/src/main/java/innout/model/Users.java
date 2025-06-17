package innout.model;

public class Users extends User {
	private String typeUser;

	public Users(String email, String password) {
		super(email, password);
		this.typeUser = "User";
	}

	@Override
	public String getTypeUser() {
		return typeUser;
	}
}
