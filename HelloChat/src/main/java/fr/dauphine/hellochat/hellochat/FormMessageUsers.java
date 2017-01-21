package fr.dauphine.hellochat.hellochat;


import java.util.Set;

public class FormMessageUsers implements FormMessage{
	private String message;
	private Set<String> Users;

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<String> getUsers() {
		return Users;
	}

	public void setUsers(Set<String> users) {
		Users = users;
	}

	
}
