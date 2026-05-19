package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 * Cody Sperling 4-24-26
 */

public class InvoiceData extends DatabaseUtils {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		Connection conn = DatabaseUtils.getConnection();
		String deleteInvItem = "delete from InvoiceItem;";

		try {
			PreparedStatement ps = conn.prepareStatement(deleteInvItem);
			ps.executeUpdate();
			ps.close();

			String deleteItem = "delete from Item;";

			PreparedStatement psItem = conn.prepareStatement(deleteItem);
			psItem.executeUpdate();
			psItem.close();

			String deleteInv = "delete from Invoice;";

			PreparedStatement psInv = conn.prepareStatement(deleteInv);
			psInv.executeUpdate();
			psInv.close();

			String deleteComp = "delete from Company;";

			PreparedStatement psComp = conn.prepareStatement(deleteComp);
			psComp.executeUpdate();
			psComp.close();

			String deleteEmail = "delete from Email;";

			PreparedStatement psEmail = conn.prepareStatement(deleteEmail);
			psEmail.executeUpdate();
			psEmail.close();

			String deletePers = "delete from Person;";

			PreparedStatement psPers = conn.prepareStatement(deletePers);
			psPers.executeUpdate();
			psPers.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addPerson(UUID personUuid, String firstName, String lastName, String phone) {
		List<UUID> uuidList = new ArrayList<>();
		Connection conn = DatabaseUtils.getConnection();
		String query = "select uuid from Person;";

		try {

			PreparedStatement ps = conn.prepareStatement(query);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String uuidStr = rs.getString("uuid");
				UUID uuid = UUID.fromString(uuidStr);
				uuidList.add(uuid);

			}
			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		if (inDatabase(personUuid, uuidList)) {
			System.err.println(
					"Person with UUID " + personUuid + " already exists within the database and cannot be entered");
			return;
		} else {
			String insert = "INSERT INTO Person(uuid,firstName,lastName,phoneNumber) VALUES (?,?,?,?);";
			try {
				PreparedStatement ps = conn.prepareStatement(insert);
				String personUuidStr = personUuid.toString();
				ps.setString(1, personUuidStr);
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setString(4, phone);
				ps.executeUpdate();

				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}

		DatabaseUtils.close(conn);
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(UUID personUuid, String email) {

		List<String> emailList = new ArrayList<>();

		Connection conn = DatabaseUtils.getConnection();
		try {
			int personId = findDatabaseKey(personUuid, "personId", "Person");

			String addresses = "select address from Email;";
			PreparedStatement addressPS = conn.prepareStatement(addresses);
			ResultSet addressRS = addressPS.executeQuery();
			while (addressRS.next()) {
				String address = addressRS.getString("address");
				emailList.add(address);
			}
			addressRS.close();
			addressPS.close();
			if (inDatabase(email, emailList)) {
				System.err.println("Person with email " + email + " already exists within the database and " + email
						+ " cannot be entered");
				return;
			}
			if (personId == -1) {
				System.err.println("Person with UUID " + personUuid
						+ " does not exist within the database and therefore an email cannot be entered");
				return;
			}

			String insert = "INSERT INTO Email(address,personId) VALUES (?,?);";
			PreparedStatement insertPS = conn.prepareStatement(insert);
			insertPS.setString(1, email);
			insertPS.setInt(2, personId);
			insertPS.executeUpdate();
			insertPS.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);

	}

	/**
	 * Adds a company record to the database with the primary contact person
	 * identified by the given code.
	 *
	 * @param companyUuid
	 * @param name
	 * @param contactUuid
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addCompany(UUID companyUuid, UUID contactUuid, String name, String street, String city,
			String state, String zip) {
		int personId = findDatabaseKey(contactUuid, "personId", "Person");
		List<UUID> uuidList = new ArrayList<>();
		Connection conn = DatabaseUtils.getConnection();
		String query = "select uuid from Company;";

		if (name.length() > 100) {
			System.err.println("Company name is too long for the database");
			return;
		}

		if (street.length() > 50) {
			System.err.println("Street name is too long for the database");
			return;
		}

		if (city.length() > 100) {
			System.err.println("City name is too long for the database");
			return;
		}

		if (state.length() > 10) {
			System.err.println("State name is too long for the database");
			return;
		}

		if (zip.length() > 10) {
			System.err.println("Zip code is too long for the database");
			return;
		}

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String uuidStr = rs.getString("uuid");
				UUID uuid = UUID.fromString(uuidStr);
				uuidList.add(uuid);
			}
			rs.close();
			ps.close();

			if (inDatabase(companyUuid, uuidList)) {
				System.err.println("The company with UUID " + companyUuid
						+ " already exists within the database and cannot be entered again");
				return;
			}
			if (personId < 0) {
				System.err.println("The company contact with UUID " + contactUuid
						+ " does not exist in the database and will have to be entered before " + name
						+ " can be entered");
				return;
			}

			String insert = "INSERT INTO Company(uuid,name,street,city,state,zip,personId) VALUES (?,?,?,?,?,?,?);";
			PreparedStatement insertPS = conn.prepareStatement(insert);
			String companyUuidStr = companyUuid.toString();
			insertPS.setString(1, companyUuidStr);
			insertPS.setString(2, name);
			insertPS.setString(3, street);
			insertPS.setString(4, city);
			insertPS.setString(5, state);
			insertPS.setString(6, zip);
			insertPS.setInt(7, personId);
			insertPS.executeUpdate();
			insertPS.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
	}

	/**
	 * Adds an equipment record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param modelNumber
	 * @param retailPrice
	 */
	public static void addEquipment(UUID equipmentUuid, String name, double pricePerUnit) {

		List<UUID> uuidList = new ArrayList<>();

		if (pricePerUnit < 0) {
			System.err.println("An equipment cannot have a negative price and therefore will not be added");
			return;
		}

		if (name.length() > 255) {
			System.err.println(name + " is too long. The maximim name length is 255 characters");
			return;
		}
		Connection conn = DatabaseUtils.getConnection();
		String query = "select uuid from Item;";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String uuidStr = rs.getString("uuid");
				UUID uuid = UUID.fromString(uuidStr);
				uuidList.add(uuid);
			}
			rs.close();
			ps.close();

			if (inDatabase(equipmentUuid, uuidList)) {
				System.err
						.println("Item with UUID " + equipmentUuid + " exists within the database and cannot be added");
				return;

			}

			String insert = "INSERT INTO Item(uuid,type,name,perUnitCost) VALUES (?,'E',?,?);";
			PreparedStatement insertPS = conn.prepareStatement(insert);
			insertPS.setString(1, equipmentUuid.toString());
			insertPS.setString(2, name);
			insertPS.setDouble(3, pricePerUnit);
			insertPS.executeUpdate();
			insertPS.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
	}

	/**
	 * Adds a service record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param costPerHour
	 */
	public static void addService(UUID equipmentUuid, String name, double costPerHour) {
		List<UUID> uuidList = new ArrayList<>();

		if (costPerHour < 0) {
			System.err.println("A service cannot have a negative hourly rate and therefore will not be added");
			return;
		}

		if (name.length() > 255) {
			System.err.println(name + " is too long. The maximim name length is 255 characters");
			return;
		}
		Connection conn = DatabaseUtils.getConnection();
		String query = "select uuid from Item;";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String uuidStr = rs.getString("uuid");
				UUID uuid = UUID.fromString(uuidStr);
				uuidList.add(uuid);
			}
			rs.close();
			ps.close();

			if (inDatabase(equipmentUuid, uuidList)) {
				System.err
						.println("Item with UUID " + equipmentUuid + " exists within the database and cannot be added");
				return;

			}

			String insert = "INSERT INTO Item(uuid,type,name,hourlyCost) VALUES (?,'S',?,?);";
			PreparedStatement insertPS = conn.prepareStatement(insert);
			insertPS.setString(1, equipmentUuid.toString());
			insertPS.setString(2, name);
			insertPS.setDouble(3, costPerHour);
			insertPS.executeUpdate();
			insertPS.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
	}

	/**
	 * Adds a license record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param serviceFee
	 * @param annualFee
	 */
	public static void addLicense(UUID equipmentUuid, String name, double serviceFee, double annualFee) {
		List<UUID> uuidList = new ArrayList<>();

		if (serviceFee < 0) {
			System.err.println("A license cannot have a negative service fee and therefore will not be added");
			return;
		}
		if (annualFee < 0) {
			System.err.println("A license cannot have a negative annual fee and therefore will not be added");
			return;
		}

		if (name.length() > 255) {
			System.err.println(name + " is too long. The maximim name length is 255 characters");
			return;
		}
		Connection conn = DatabaseUtils.getConnection();
		String query = "select uuid from Item;";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String uuidStr = rs.getString("uuid");
				UUID uuid = UUID.fromString(uuidStr);
				uuidList.add(uuid);
			}
			rs.close();
			ps.close();

			if (inDatabase(equipmentUuid, uuidList)) {
				System.err
						.println("Item with UUID " + equipmentUuid + " exists within the database and cannot be added");
				return;

			}

			String insert = "INSERT INTO Item(uuid,type,name,serviceFee,annualFee) VALUES (?,'L',?,?,?);";
			PreparedStatement insertPS = conn.prepareStatement(insert);
			insertPS.setString(1, equipmentUuid.toString());
			insertPS.setString(2, name);
			insertPS.setDouble(3, serviceFee);
			insertPS.setDouble(4, annualFee);
			insertPS.executeUpdate();
			insertPS.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
	}

	/**
	 * Adds an Invoice record to the database with the given data.
	 *
	 * @param invoiceUuid
	 * @param customerUuid
	 * @param salesPersonUuid
	 * @param date
	 */
	public static void addInvoice(UUID invoiceUuid, UUID customerUuid, UUID salesPersonUuid, LocalDate date) {
		List<UUID> uuidList = new ArrayList<>();
		int companyId = findDatabaseKey(customerUuid, "companyId", "Company");
		int personId = findDatabaseKey(salesPersonUuid, "personId", "Person");
		String dateStr = date.toString();

		Connection conn = DatabaseUtils.getConnection();
		String query = "select uuid from Invoice;";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				String uuid = rs.getString("uuid");
				uuidList.add(UUID.fromString(uuid));
			}

			rs.close();
			ps.close();

			if (inDatabase(invoiceUuid, uuidList)) {
				System.err.println(
						"Invoice with UUID " + invoiceUuid + " exists within the database and cannot be added");
				return;

			}
			if (companyId < 0) {
				System.err.println("Company with UUID " + customerUuid
						+ " does not exist within the database, please add the company first");
				return;
			}
			if (personId < 0) {
				System.err.println("Salesperson with UUID " + salesPersonUuid
						+ " does not exist within the database, please add the person first");
				return;
			}

			String insert = "INSERT INTO Invoice(uuid,date,personId,companyId) VALUES (?,?,?,?);";
			PreparedStatement insertPS = conn.prepareStatement(insert);
			insertPS.setString(1, invoiceUuid.toString());
			insertPS.setString(2, dateStr);
			insertPS.setDouble(3, personId);
			insertPS.setDouble(4, companyId);
			insertPS.executeUpdate();
			insertPS.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
	}

	/**
	 * Adds an Equipment purchase record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 */
	public static void addEquipmentPurchaseToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		addEquipmentToInvoice(invoiceUuid, itemUuid, numberOfUnits, "P");
	}

	/**
	 * Adds an Equipment lease record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param start
	 * @param end
	 */
	public static void addEquipmentLeaseToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		addEquipmentToInvoice(invoiceUuid, itemUuid, numberOfUnits, "L");
	}

	/**
	 * Adds a service record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param servicePersonUuid
	 * @param numberOfHours
	 */
	public static void addServiceToInvoice(UUID invoiceUuid, UUID itemUuid, UUID servicePersonUuid,
			double numberOfHours) {
		Connection conn = DatabaseUtils.getConnection();

		int personId = findDatabaseKey(servicePersonUuid, "personId", "Person");
		int itemId = findDatabaseKey(itemUuid, "itemId", "Item");
		int invoiceId = findDatabaseKey(invoiceUuid, "invoiceId", "Invoice");

		if (!pairIsUnique(itemId, invoiceId)) {
			System.err.println("The combination of the itemId associated with " + itemUuid
					+ " and the invoiceId associated with " + invoiceUuid + " must be unique");
			return;
		}

		if (itemId < 0) {
			System.err.println("The item with UUID " + itemUuid + " does not exist within the Item table");
			return;
		}

		if (invoiceId < 0) {
			System.err.println("The invoice with UUID " + invoiceUuid + " does not exist within the Invoice table");
			return;
		}

		if (personId < 0) {
			System.err.println(
					"The service person with UUID " + servicePersonUuid + " does not exist within the Person table");
			return;
		}

		if (numberOfHours < 0) {
			System.err.println("A service cannot take a negative number of hours");
			return;
		}

		String query = "INSERT INTO InvoiceItem(invoiceId,itemId,personId,billableHours) VALUES (?,?,?,?);";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setInt(3, personId);
			ps.setDouble(4, numberOfHours);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
	}

	/**
	 * Adds a license record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param beginDate
	 * @param endDate
	 */
	public static void addLicenseToInvoice(UUID invoiceUuid, UUID itemUuid, LocalDate beginDate, LocalDate endDate) {
		Connection conn = DatabaseUtils.getConnection();

		int itemId = findDatabaseKey(itemUuid, "itemId", "Item");
		int invoiceId = findDatabaseKey(invoiceUuid, "invoiceId", "Invoice");

		if (!pairIsUnique(itemId, invoiceId)) {
			System.err.println("The combination of the itemId associated with " + itemUuid
					+ " and the invoiceId associated with " + invoiceUuid + " must be unique");
			return;
		}

		if (itemId < 0) {
			System.err.println("The item with UUID " + itemUuid + " does not exist within the Item table");
			return;
		}

		if (invoiceId < 0) {
			System.err.println("The invoice with UUID " + invoiceUuid + " does not exist within the Invoice table");
			return;
		}

		String query = "INSERT INTO InvoiceItem(invoiceId,itemId,beginDate,endDate) VALUES (?,?,?,?);";
		try {

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setString(3, beginDate.toString());
			ps.setString(4, endDate.toString());
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn);
	}

	/**
	 * This method was written to check if <code> element </code> exists in
	 * <code> list </code>
	 * 
	 * @param <T>
	 * @param element
	 * @param list
	 * @return
	 */

	private static <T extends Comparable<? super T>> boolean inDatabase(T element, List<T> list) {
		Collections.sort(list);
		return (Collections.binarySearch(list, element)) >= 0;

	}

	/**
	 * This method queries the database for the primary key associated with a given
	 * UUID, if no such uuid exists in the specified table, -1 will be returned
	 * 
	 * @param uuid
	 * @return
	 */

	private static int findDatabaseKey(UUID uuid, String primaryKey, String tableName) {
		int personId = -1;
		List<String> acceptablePKs = new ArrayList<>();
		List<String> acceptableTableNames = new ArrayList<>();
		Collections.addAll(acceptablePKs, "personId", "companyId", "emailId", "invoiceId", "itemId", "invoiceItemId");
		Collections.addAll(acceptableTableNames, "Person", "Company", "Email", "Invoice", "Item", "InvoiceItem");

		if (!((acceptablePKs.contains(primaryKey)) || (acceptableTableNames.contains(tableName)))) {
			throw new RuntimeException(primaryKey + " or " + tableName + " is not valid");
		}

		Connection conn = DatabaseUtils.getConnection();
		String query = "select " + primaryKey + " from " + tableName + " where uuid = ?;";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			String uuidStr = uuid.toString();
			ps.setString(1, uuidStr);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				personId = rs.getInt(primaryKey);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn);

		return personId;
	}

	/**
	 * This method is used when adding to the invoice item table in order to make
	 * sure that the combination of the invoiceId and itemId is unique within the
	 * InvoiceItem table
	 * 
	 * @param itemId
	 * @param invoiceId
	 * @return
	 */

	private static boolean pairIsUnique(int itemId, int invoiceId) {

		Boolean isUnique;
		Comparator<int[]> pairComp = new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				if (a[0] != b[0]) {
					return Integer.compare(a[0], b[0]);
				}
				return Integer.compare(a[1], b[1]);
			}
		};

		int[] pair = { itemId, invoiceId };

		List<int[]> unqPairs = new ArrayList<>();

		String query = "select itemId,invoiceId from InvoiceItem;";
		Connection conn = DatabaseUtils.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int itemIdDB = rs.getInt("itemId");
				int invoiceIdDB = rs.getInt("invoiceId");
				int[] pairDB = { itemIdDB, invoiceIdDB };
				unqPairs.add(pairDB);

			}
			rs.close();
			ps.close();

			Collections.sort(unqPairs, pairComp);
			isUnique = (Collections.binarySearch(unqPairs, pair, pairComp) < 0);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn);
		return isUnique;

	}

	/**
	 * This method contains the code to add either a purchased or leased equipment
	 * to an invoice, depending on <code>type</code>
	 * 
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfUnits
	 * @param type
	 */

	private static void addEquipmentToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits, String type) {
		Connection conn = DatabaseUtils.getConnection();
		int itemId = findDatabaseKey(itemUuid, "itemId", "Item");
		int invoiceId = findDatabaseKey(invoiceUuid, "invoiceId", "Invoice");

		if (!(pairIsUnique(itemId, invoiceId))) {
			System.err.println("The combination of the itemId associated with " + itemUuid
					+ " and the invoiceId associated with " + invoiceUuid + " must be unique");
			return;
		}
		if (itemId < 0) {
			System.err.println("The item with UUID " + itemUuid + " does not exist within the Item table");
			return;
		}

		if (invoiceId < 0) {
			System.err.println("The invoice with UUID " + invoiceUuid + " does not exist within the Invoice table");
			return;
		}

		if (numberOfUnits < 0) {
			System.err.println("A negative number of units cannot be added to an invoice");
			return;
		}

		String query = "INSERT INTO InvoiceItem(invoiceId,itemId,type,numOfUnits) VALUES (?,?,?,?);";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setString(3, type);
			ps.setInt(4, numberOfUnits);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}

		DatabaseUtils.close(conn);
	}

}
