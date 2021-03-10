package com.scma.anilg.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="CONTACT")
public class Contact {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cId;
	
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@NotBlank(message="Nickname not be blank")
	private String secondName;
	
	@NotBlank(message="Work field not be blank")
	private String work;
	
	@NotBlank(message="Email is mandatory ")
	private String email;
	
	//@NotBlank(message="Please select one image")
	private String image;
	
	@NotBlank(message="Phone number is mandatory")
	//@Pattern(regexp="((0/91)?[7-9][0-9]{9})", message = "Enter valid mobile number" )
	private String phone;
	
	@NotBlank(message = "Description must not be blank")
	@Column(length = 5000)
	//@Size(min = 3, message = "description must be min 3 letters")
	private String description;
	
	
	@ManyToOne
	private User user;
	
	public Contact() {
		
	}

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * If we remove this comment, we will get StackOverflow error at contact adding time
	 * */
	/*
	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", name=" + name + ", secondName=" + secondName + ", work=" + work + ", email="
				+ email + ", image=" + image + ", description=" + description + ", phone=" + phone + ", user=" + user
				+ "]";
	}	
*/
	
	@Override
	public boolean equals(Object obj) {
		return this.cId == ((Contact)obj).getcId();
	}
	
}
