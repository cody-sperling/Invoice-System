package com.cinco;

/**
 * Cody Sperling
 * 
 * 2-27-26
 * 
 * This class contains all the methods used in parsing the csv files into the appropriate objects
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.time.LocalDate;

public class DataLoaderCSV {

	/**
	 * This method takes in a file with Items data as a string, tokenizes it line by
	 * line, then adds it to a list of Items that is returned. Licenses, Services,
	 * and Equipment are included in this list
	 * 
	 * @param filePath
	 * @return
	 */

	public static List<Item> loadItemsData(String filePath) {

		List<Item> itemsList = new ArrayList<>();

		File f = new File(filePath);
		String line = null;

		try {
			Scanner s = new Scanner(f);

			line = s.nextLine();
			while (s.hasNextLine()) {
				line = s.nextLine();
				String tokens[] = line.split(",");

				UUID uuid = UUID.fromString(tokens[0]);

				String name = tokens[2];

				if (tokens[1].equals("E")) {

					double unitPrice = Double.parseDouble(tokens[3]);
					Equipment e = new Equipment(uuid, name, unitPrice);
					itemsList.add(e);

				} else if (tokens[1].equals("S")) {
					double hourlyCost = Double.parseDouble(tokens[3]);
					Service serv = new Service(uuid, name, hourlyCost);
					itemsList.add(serv);

				} else {

					double serviceFee = Double.parseDouble(tokens[3]);
					double annualFee = Double.parseDouble(tokens[4]);
					License l = new License(uuid, name, serviceFee, annualFee);
					itemsList.add(l);
				}

			}

			s.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		return itemsList;
	}

	/**
	 * This method takes in a file with Persons data as a string, tokenizes it line
	 * by line, then adds it to a list of Persons that is returned
	 * 
	 * @param filePath
	 * @return
	 */

	public static List<Person> loadPeopleData(String filePath) {

		List<Person> peopleList = new ArrayList<>();

		File f = new File(filePath);
		String line = null;

		try {
			Scanner s = new Scanner(f);

			line = s.nextLine();
			while (s.hasNextLine()) {

				line = s.nextLine();

				if (line.isEmpty()) {
					continue;
				}

				String tokens[] = line.split(",");

				int numOfEmails = (tokens.length - 4);

				UUID uuid = UUID.fromString(tokens[0]);

				String firstName = tokens[1];

				String lastName = tokens[2];

				String phoneNumber = tokens[3];

				Person p = new Person(uuid, firstName, lastName, phoneNumber);

				for (int i = 0; i < numOfEmails; i++) {

					String email = tokens[i + 4];

					p.addEmail(email);

				}

				peopleList.add(p);

			}

			s.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		return peopleList;
	}

	/**
	 * This method takes in a file with company data as a string and tokenizes it
	 * into a list of company objects
	 * 
	 * @param filePath
	 * @return
	 */

	public static List<Company> loadCompanyData(String filePath, List<Person> personList) {

		List<Company> companiesList = new ArrayList<>();

		File f = new File(filePath);
		String line = null;

		try {
			Scanner s = new Scanner(f);

			line = s.nextLine();
			while (s.hasNextLine()) {
				line = s.nextLine();
				String tokens[] = line.split(",");

				UUID companyUuid = UUID.fromString(tokens[0]);

				UUID contactUuid = UUID.fromString(tokens[1]);

				String name = tokens[2];

				String street = tokens[3];

				String city = tokens[4];

				String state = tokens[5];

				String zip = tokens[6];

				Address address = new Address(street, city, state, zip);

				Person companyContact = null;

				for (Person p : personList) {
					if (p.getUuid().equals(contactUuid)) {

						companyContact = p;
						break;

					} else {
						companyContact = new Person(contactUuid);

					}

				}

				Company c = new Company(companyUuid, companyContact, name, address);

				companiesList.add(c);

			}

			s.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		return companiesList;
	}

	/**
	 * This method takes in a list of people and companies and matches the companies
	 * to their respective invoices and people to sales persons and lastly it
	 * returns a list of invoices
	 * 
	 * @param filePath
	 * @param personList
	 * @param companyList
	 * @return
	 */

	public static List<Invoice> loadInvoicesData(String filePath, List<Person> personList, List<Company> companyList) {
		List<Invoice> invoiceList = new ArrayList<>();

		File f = new File(filePath);
		String line = null;

		try {
			Scanner s = new Scanner(f);

			line = s.nextLine();
			while (s.hasNextLine()) {

				line = s.nextLine();
				String tokens[] = line.split(",");

				UUID invoiceUUID = UUID.fromString(tokens[0]);

				UUID customerUUID = UUID.fromString(tokens[1]);

				Company customer = null;

				for (Company c : companyList) {
					if (c.getCompanyUuid().equals(customerUUID)) {

						customer = c;
						break;

					}

				}

				UUID salesPersonUuid = UUID.fromString(tokens[2]);

				Person salesPerson = null;

				for (Person p : personList) {
					if (p.getUuid().equals(salesPersonUuid)) {

						salesPerson = p;
						break;

					}

				}

				LocalDate dateCreated = LocalDate.parse(tokens[3]);

				Invoice i = new Invoice(invoiceUUID, customer, salesPerson, dateCreated);

				invoiceList.add(i);

			}

			s.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		return invoiceList;
	}

	/**
	 * This method tokenizes the lines in invoiceitems.csv and then creates a copy
	 * of each item with the new information to its respective invoice
	 * 
	 * @param filePath
	 * @param itemList
	 * @param invoiceList
	 * @param personList
	 */

	public static void loadInvoiceItems(String filePath, List<Item> itemList, List<Invoice> invoiceList,
			List<Person> personList) {

		File f = new File(filePath);
		String line = null;

		try {
			Scanner s = new Scanner(f);

			line = s.nextLine();
			while (s.hasNextLine()) {
				line = s.nextLine();
				String tokens[] = line.split(",");

				UUID invoiceUuid = UUID.fromString(tokens[0]);

				for (Invoice i : invoiceList) {
					if (i.getUuid().equals(invoiceUuid)) {

						UUID itemUuid = UUID.fromString(tokens[1]);

						for (Item item : itemList) {

							if (item.getUuid().equals(itemUuid)) {

								if (item instanceof License) {

									LocalDate beginningDate = LocalDate.parse(tokens[2]);
									LocalDate endingDate = LocalDate.parse(tokens[3]);

									License newLicense = new License((License) item, beginningDate, endingDate);
									i.addItem(newLicense);
								}

								else if (item instanceof Service) {

									UUID servicePersonUUID = UUID.fromString(tokens[2]);
									Double billableHours = Double.valueOf(tokens[3]);
									Person servicePerson = null;

									for (Person p : personList) {
										if (p.getUuid().equals(servicePersonUUID)) {

											servicePerson = p;
											break;

										}
									}

									Service newService = new Service((Service) item, servicePerson, billableHours);
									i.addItem(newService);
								}

								else if (item instanceof Equipment) {

									if (tokens[2].equals("P")) {

										int numOfUnits = Integer.parseInt(tokens[3]);

										SoldEquipment newSoldEquipment = new SoldEquipment((Equipment) item,
												numOfUnits);
										i.addItem(newSoldEquipment);

									}

									else if (tokens[2].equals("L")) {

										int numOfUnits = Integer.parseInt(tokens[3]);

										LeasedEquipment newLeasedEquipment = new LeasedEquipment((Equipment) item,
												numOfUnits);
										i.addItem(newLeasedEquipment);

									}

								}

								break;

							}

						}

						break;

					}

				}

			}
			s.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}

}
