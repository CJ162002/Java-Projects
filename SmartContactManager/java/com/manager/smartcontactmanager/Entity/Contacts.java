package com.manager.smartcontactmanager.Entity;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="contacts")
public class Contacts {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long c_id;
	@NotBlank(message = "Please enter the name !!")
	@Pattern(regexp = "^[A-Za-z\s]+$", message = "Only alphabets are allowed !!")
	@Size(min = 5, max = 20, message = "Name should be of 5-20 characters")
	private String name;
	@NotBlank(message = "Please enter the nickname !!")
	@Pattern(regexp = "^[A-Za-z\s]+$", message = "Only alphabets are allowed !!")
	@Size(min = 3, max = 20, message = "Nick Name should be of 3-20 characters")
	private String nickName;
	@Column(unique = true)
	@NotBlank(message = "This field should not be empty !!")
	@Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Please match the email format !!")
	private String email;
	@Column(unique = true)
	@NotBlank(message = "This field should not be empty !!")
	@Pattern(regexp = "(\\+?\\d{1,4}[- ]?)?\\d{10}", message = "Phone number should match the format !!")
	private String phone;
	private String img;
	@Column(length=900)
	@NotBlank(message = "This field should not be empty !!")
	private String description;
	
	@ManyToOne
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getC_id() {
		return c_id;
	}
	public void setC_id(long c_id) {
		this.c_id = c_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Contacts [c_id=" + c_id + ", name=" + name + ", nickName=" + nickName + ", email=" + email + ", phone="
				+ phone + ", img=" + img + ", description=" + description + ", user=" + user + "]";
	}
	
}
