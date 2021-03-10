package com.scma.anilg.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="USER")
public class User {
	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "name shuld not be blank")
	@Size(min = 3, max = 20, message = "name must be bitween 2 to 20 characters")
	private String name;
	
	@Column(unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "invalid email address")
	private String email;
	
	@Size(min = 6, message = "password must be minimum 6 character")
	private String password;
	
	private String imageUrl;
	
	@Column(length = 500)
	
	@NotBlank(message = "about field must not be blank")
	private String about;
	
	private String role;
	
	private boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>(); 
	
	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	

	@Override
	public String toString() {
		return "User [about=" + about + ", contacts=" + contacts + ", email=" + email + ", enabled=" + enabled + ", id="
				+ id + ", imageUrl=" + imageUrl + ", name=" + name + ", password=" + password + ", role=" + role + "]";
	}
	
}
