/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.openservices.entities;

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
 * 
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
	
	/**
	 * new {@Profile} instance
	 */
	public Profile(){
		
	}
	
	/**
	 * New {@Profile} instance with params
	 * @param name
	 * @param surname
	 * @param hobby
	 * @param birthday
	 */
	public Profile(String name, String surname, String hobby, String birthday){
		this.name=name;
		this.surname=surname;
		this.hobby=hobby;
		this.birthday=birthday;
	}

	/**
	 * Get name of {@Profile} instance
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name in {@Profile} instance
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get surname of {@Profile} instance
	 * @return String surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Set surname in {@Profile} instance
	 * @param surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * 
	 * @return String hobby of {@Profile} instance
	 */
	public String getHobby() {
		return hobby;
	}

	/**
	 * 
	 * @param hobby in {@Profile} instance
	 */
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	/**
	 * 
	 * @return String birthday of {@Profile} instance
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * 
	 * @param birthday in {@Profile} instance
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * 
	 * @return String url avatar of {@Profile} instance
	 */
	public String getImgAvatar() {
		return imgAvatar;
	}

	/**
	 * 
	 * @param imgAvatar, which is url avatar in {@Profile} instance
	 */
	public void setImgAvatar(String imgAvatar) {
		this.imgAvatar = imgAvatar;
	}

	/**
	 * 
	 * @return String address of {@Profile} instance
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * @param address in {@Profile} instance
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * @return String country of {@Profile} instance
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * 
	 * @param country in {@Profile} instance
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 
	 * @return String interest of {@Profile} instance
	 */
	public String getInterest() {
		return interest;
	}

	/**
	 * 
	 * @param interest in {@Profile} instance
	 */
	public void setInterest(String interest) {
		this.interest = interest;
	}

	/**
	 * 
	 * @return String phone of {@Profile} instance
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 
	 * @param phone in {@Profile} instance
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 
	 * @return String mobile phone of {@Profile} instance
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 
	 * @param mobile in {@Profile} instance
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
