package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class contains methods to load all data from my Nuros database
 * 
 * Cody Sperling
 * 
 * 4-17-26
 */

public class DataLoaderDatabase extends DatabaseUtils {

	/**
	 * This method loads all emails in my database and assigns the addresses to
	 * their appropriate people
	 * 
	 * @param personList
	 */

	private static void getEmails(List<Person> personList) {

		Connection conn = DatabaseUtils.getConnection();

		String query = "select personId,address from Email;";

		try {

			PreparedStatement ps = conn.prepareStatement(query);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				int personId = rs.getInt("personId");
				String address = rs.getString("address");

				for (Person p : personList) {
					if (p.getPersonId() == personId) {

						p.addEmail(address);
					}

				}

			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);

	}

	/**
	 * This method loads and constructs all the person data in the database and
	 * returns a list of Person with everyone
	 * 
	 * @return
	 */

	public static List<Person> loadPersons() {

		List<Person> personList = new ArrayList<>();

		Connection conn = DatabaseUtils.getConnection();
		String query = "select uuid,firstName,lastName,phoneNumber,personId from Person;";

		try {

			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String uuidStr = rs.getString("uuid");
				UUID uuid = UUID.fromString(uuidStr);
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String phoneNumber = rs.getString("phoneNumber");
				int personId = rs.getInt("personId");

				Person p = new Person(uuid, firstName, lastName, phoneNumber, personId);
				personList.add(p);

			}
			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);

		getEmails(personList);
		return personList;

	}

	/**
	 * This method loads and constructs all the company data in the database and
	 * returns a list of Company with everything
	 * 
	 * @param personList
	 * @return
	 */

	public static List<Company> loadCompanies(List<Person> personList) {

		List<Company> companyList = new ArrayList<>();
		Connection conn = DatabaseUtils.getConnection();
		String query = "select companyId,uuid,name,street,city,state,zip,personId from Company;";

		try {

			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int companyId = rs.getInt("companyId");
				String uuidstr = rs.getString("uuid");
				UUID uuid = UUID.fromString(uuidstr);
				String name = rs.getString("name");
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				int personId = rs.getInt("personId");
				Person contact = null;

				for (Person p : personList) {

					if (p.getPersonId() == personId) {
						contact = p;
						break;
					}

				}

				Address add = new Address(street, city, state, zip);
				Company company = new Company(uuid, contact, name, add, companyId);
				companyList.add(company);

			}
			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
		return companyList;
	}

	/**
	 * This method loads and constructs all the invoice data in the database and
	 * returns a list of Invoice with every invoice
	 * 
	 * @param personList
	 * @param companyList
	 * @return
	 */

	public static List<Invoice> loadInvoices(List<Person> personList, List<Company> companyList) {

		List<Invoice> invoiceList = new ArrayList<>();

		Connection conn = DatabaseUtils.getConnection();
		String query = "select invoiceId,uuid,date,personId,companyId from Invoice;";

		try {

			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				String uuidstr = rs.getString("uuid");
				UUID uuid = UUID.fromString(uuidstr);
				String datestr = rs.getString("date");
				LocalDate date = LocalDate.parse(datestr);
				int personId = rs.getInt("personId");
				Person salesman = null;
				Company customer = null;
				int companyId = rs.getInt("companyId");

				for (Person p : personList) {

					if (p.getPersonId() == personId) {
						salesman = p;
						break;

					}

				}

				for (Company c : companyList) {

					if (c.getCompanyId() == companyId) {
						customer = c;
						break;
					}

				}

				Invoice inv = new Invoice(uuid, customer, salesman, date, invoiceId);
				invoiceList.add(inv);

			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
		return invoiceList;
	}

	/**
	 * This method loads and constructs all the items data in the database and
	 * returns a list of Item with every item on it
	 * 
	 * @return
	 */

	public static List<Item> loadItems() {

		List<Item> itemList = new ArrayList<>();

		Connection conn = DatabaseUtils.getConnection();
		String query = "select itemId,uuid,name,type,perUnitCost,hourlyCost,serviceFee,annualFee from Item;";

		try {

			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Item item;
				int itemId = rs.getInt("itemId");
				String uuidstr = rs.getString("uuid");
				UUID uuid = UUID.fromString(uuidstr);
				String name = rs.getString("name");

				if (rs.getString("type").equals("E")) {

					double perUnitCost = rs.getDouble("perUnitCost");
					item = new Equipment(itemId, uuid, name, perUnitCost);

				} else if (rs.getString("type").equals("L")) {
					double serviceFee = rs.getDouble("serviceFee");
					double annualFee = rs.getDouble("annualFee");
					item = new License(itemId, uuid, name, serviceFee, annualFee);

				} else if (rs.getString("type").equals("S")) {
					double hourlyCost = rs.getDouble("hourlyCost");
					item = new Service(itemId, uuid, name, hourlyCost);

				} else {
					System.err.println("Data Error");
					item = null;
				}
				itemList.add(item);

			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
		return itemList;

	}

	/**
	 * This method adds each item in itemList to their appropriate invoice in
	 * invoiceList with the extra information contained in the InvoiceItem table on
	 * my database
	 * 
	 * @param itemList
	 * @param invoiceList
	 * @param personList
	 */

	public static void loadInvoiceItems(List<Item> itemList, List<Invoice> invoiceList, List<Person> personList) {
		Connection conn = DatabaseUtils.getConnection();
		String query = "select itemId,invoiceId,numOfUnits,type,billableHours,personId,beginDate,endDate from InvoiceItem;";

		try {

			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Invoice targetInvoice = null;
				Item targetItem = null;
				int itemId = rs.getInt("itemId");
				int invoiceId = rs.getInt("invoiceId");

				for (Item i : itemList) {

					if (i.getitemId() == itemId) {
						targetItem = i;
						break;
					}
				}

				for (Invoice inv : invoiceList) {

					if (inv.getInvoiceId() == invoiceId) {
						targetInvoice = inv;
						break;
					}
				}

				if (targetItem instanceof License) {
					String beginDatestr = rs.getString("beginDate");
					LocalDate beginDate = LocalDate.parse(beginDatestr);
					String endDatestr = rs.getString("endDate");
					LocalDate endDate = LocalDate.parse(endDatestr);

					License newLicense = new License((License) targetItem, beginDate, endDate);
					targetInvoice.addItem(newLicense);

				} else if (targetItem instanceof Equipment) {

					int numOfUnits = rs.getInt("numOfUnits");
					Equipment newEquipment;

					if (rs.getString("type").equals("P")) {
						newEquipment = new SoldEquipment((Equipment) targetItem, numOfUnits);

					} else {
						newEquipment = new LeasedEquipment((Equipment) targetItem, numOfUnits);

					}
					targetInvoice.addItem(newEquipment);

				} else if (targetItem instanceof Service) {

					Person serviceMan = null;
					int personId = rs.getInt("personId");
					double billableHours = rs.getDouble("billableHours");

					for (Person p : personList) {

						if (p.getPersonId() == personId) {
							serviceMan = p;
							break;
						}
					}
					Service newService = new Service((Service) targetItem, serviceMan, billableHours);
					targetInvoice.addItem(newService);
				}

			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(getConnection());
	}

}
