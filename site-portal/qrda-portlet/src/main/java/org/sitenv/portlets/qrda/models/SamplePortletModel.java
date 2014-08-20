package org.sitenv.portlets.qrda.models;

import java.io.Serializable;

public class SamplePortletModel implements Serializable {
	private String FirstName;
	private String LastName;
	private String Username;
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String userName) {
		Username = userName;
	}
}
