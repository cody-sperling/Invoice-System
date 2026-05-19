package com.cinco;

import java.util.UUID;

/**
 * This data class models a company Cody Sperling 4-24-26
 */

public class Company {

	private UUID companyUuid;
	private Person companyContact;
	private String name;
	private Address address;
	private int companyId;

	public Company(UUID companyUuid, Person companyContact, String name, Address address) {
		super();
		this.companyUuid = companyUuid;
		this.companyContact = companyContact;
		this.name = name;
		this.address = address;
	}

	public Company(UUID companyUuid, Person companyContact, String name, Address address, int companyId) {
		super();
		this.companyUuid = companyUuid;
		this.companyContact = companyContact;
		this.name = name;
		this.address = address;
		this.companyId = companyId;
	}

	public UUID getCompanyUuid() {
		return companyUuid;
	}

	public Person getCompanyContact() {
		return companyContact;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	public int getCompanyId() {
		return companyId;
	}

	@Override
	public String toString() {
		return name;
	}

}
