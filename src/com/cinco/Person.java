package com.cinco;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This data class models a person Cody Sperling 4-24-26
 */

public class Person {

	private UUID uuid;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private int personId;
	private List<String> emailList;

	public Person(UUID uuid, String firstName, String lastName, String phoneNumber) {
		super();
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emailList = new ArrayList<>();

	}

	public Person(UUID uuid, String firstName, String lastName, String phoneNumber, int personId) {
		super();
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.personId = personId;
		this.emailList = new ArrayList<>();

	}

	public Person(UUID uuid) {
		super();
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return String.format("%s, %s (%s)\n\t%s", this.lastName, this.firstName, this.uuid, this.emailList);
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public int getPersonId() {
		return personId;
	}

	public List<String> getEmailList() {
		return new ArrayList<>(emailList);
	}

	public void addEmail(String email) {

		this.emailList.add(email);

	}

}
