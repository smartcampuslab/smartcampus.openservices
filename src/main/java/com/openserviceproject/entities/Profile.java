package com.openserviceproject.entities;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Class profile which will become Blob Object for Users table.
 * Having field:
 * imgAvatar
 * name
 * surname
 * birthday
 * address
 * country
 * hobby
 * interest
 * @author Giulia Canobbio
 *
 */
public class Profile implements Serializable{
	
	private String imgAvatar;//url image
	private String name;
	private String surname;
	private String birthday;
	private String address;
	private String country;
	private String hobby;
	private String interest;
	private String phone;
	private String mobile;
	
	public Profile(){
		
	}
	
	public Profile(String name, String surname, String hobby, String birthday){
		this.name=name;
		this.surname=surname;
		this.hobby=hobby;
		this.birthday=birthday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getImgAvatar() {
		return imgAvatar;
	}

	public void setImgAvatar(String imgAvatar) {
		this.imgAvatar = imgAvatar;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
