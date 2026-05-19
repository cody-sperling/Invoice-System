package com.cinco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * JUnit test suite for the invoice system. Specifically the invoice class as a
 * whole
 * 
 * Cody Sperling 4-24-26
 */
public class InvoiceTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in the
	 * system.
	 */
	@Test
	public void testInvoice01() {

		UUID uuid = UUID.fromString("e7104a1b-4e97-497d-99b3-0e4c5f5ffb8a");
		String name = "Adobe Premium License";
		double serviceFee = 84.99;
		double annualFee = 200;
		LocalDate beginDate = LocalDate.of(2023, 3, 11);
		LocalDate endDate = LocalDate.of(2025, 3, 11);

		License testLicense = new License(uuid, name, serviceFee, annualFee, beginDate, endDate);

		UUID uuidService = UUID.fromString("e7f1f962-8648-439e-95dd-08d034273e28");
		String nameService = "Server Maintenance";
		double perHourCost = 25.00;
		double billableHour = 3;

		UUID personUuid = UUID.fromString("d607eebd-b731-4fce-85b5-1f35bdc138e7");

		Person servicePerson = new Person(personUuid, "Harry", "Kane", "610-784-6790");

		Service testService = new Service(uuidService, nameService, perHourCost, billableHour, servicePerson);

		UUID uuidSold = UUID.fromString("673a37b6-afc2-428b-a5ec-8a3d67f556c5");
		String nameSold = "RGB KeyBoard";
		double pricePerUnit = 149.99;
		int numberOfUnits = 25;

		SoldEquipment testSoldEquipment = new SoldEquipment(uuidSold, nameSold, pricePerUnit, numberOfUnits);

		UUID invoiceUuid = UUID.fromString("c110268d-dc1e-45df-8c53-2ad69396da5e");
		LocalDate dateCreated = LocalDate.of(2026, 2, 26);

		UUID salesmanUuid = UUID.fromString("e2bb88b1-749b-44c7-80b3-c9840bc8bafa");
		String salesmanName = "Bob";
		String salesmanLastName = "Smith";
		String phoneNumber = "531-500-7764";
		Person salesman = new Person(salesmanUuid, salesmanName, salesmanLastName, phoneNumber);

		UUID companycontactUuid = UUID.fromString("e2bb88b1-749b-44c7-80b3-c9840bc8bafa");
		String companycontactName = "Bob";
		String companycontactLastName = "Springer";
		String companycontactphoneNumber = "531-500-7764";
		Person companycontact = new Person(companycontactUuid, companycontactName, companycontactLastName,
				companycontactphoneNumber);

		UUID companyUuid = UUID.fromString("50ae6c63-9f32-4248-9e4d-17fe6a719bda");
		String companyTitle = "Springer and Sons";
		String street = "2688 Rd Lamar";
		String city = "Texarkana";
		String state = "Arkansas";
		String zip = "72846";
		Address companyAddress = new Address(street, city, state, zip);
		Company customer = new Company(companyUuid, companycontact, companyTitle, companyAddress);

		Invoice invoiceTest = new Invoice(invoiceUuid, customer, salesman, dateCreated);

		invoiceTest.addItem(testSoldEquipment);
		invoiceTest.addItem(testLicense);
		invoiceTest.addItem(testService);

		double expectedSubtotal = 4435.29;
		double expectedTaxTotal = 203.16;
		double expectedGrandTotal = 4638.45;

		double actualSubtotal = invoiceTest.sumSubTotals();
		double actualTaxTotal = invoiceTest.sumTaxes();
		double actualGrandTotal = invoiceTest.sumTotals();

		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);

	}

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in the VGB
	 * system.
	 */
	@Test
	public void testInvoice02() {

		UUID uuid = UUID.fromString("c4e239dd-ddfd-447a-9afa-bdefe08f18f3");
		String name = "I280 Razor Headset";
		double pricePerUnit = 84.99;
		int numberOfUnits = 200;

		LeasedEquipment testLease = new LeasedEquipment(uuid, name, pricePerUnit, numberOfUnits);

		UUID licenseUuid = UUID.fromString("64b7025e-df94-4cba-ad95-7bd921441f4d");
		String licenseName = "Microsoft Word Auto Save Subscription";
		double serviceFee = 10.99;
		double annualFee = 1300;
		LocalDate beginDate = LocalDate.of(2023, 3, 11);
		LocalDate endDate = LocalDate.of(2025, 3, 11);

		License testLicense = new License(licenseUuid, licenseName, serviceFee, annualFee, beginDate, endDate);

		UUID companycontactUuid = UUID.fromString("4ba5cb07-8e35-4f41-9aaf-4f9383dd7427");
		String companycontactName = "Janet";
		String companycontactLastName = "Applewhite";
		String companycontactphoneNumber = "531-500-5421";
		Person companycontact = new Person(companycontactUuid, companycontactName, companycontactLastName,
				companycontactphoneNumber);

		UUID invoiceUuid = UUID.fromString("002b2a8d-60ee-4df3-ad4e-82df3b4512c4");
		LocalDate dateCreated = LocalDate.of(2026, 2, 25);

		UUID salesmanUuid = UUID.fromString("22fbf231-ac9e-4ef2-ba08-fd467807d8fe");
		String salesmanName = "Lebron";
		String salesmanLastName = "Jack";
		String phoneNumber = "531-500-1132";
		Person salesman = new Person(salesmanUuid, salesmanName, salesmanLastName, phoneNumber);

		UUID companyUuid = UUID.fromString("0890a099-7934-4628-9d78-038ee4cb7d49");
		String companyTitle = "National Banking Association";
		String street = "2632 Rd Red";
		String city = "Sacremento";
		String state = "California";
		String zip = "73531";
		Address companyAddress = new Address(street, city, state, zip);
		Company customer = new Company(companyUuid, companycontact, companyTitle, companyAddress);

		Invoice invoiceTest = new Invoice(invoiceUuid, customer, salesman, dateCreated);

		invoiceTest.addItem(testLicense);
		invoiceTest.addItem(testLease);

		double expectedSubtotal = 28111.55;
		double expectedTaxTotal = 350;
		double expectedGrandTotal = 3672.80;

		double actualSubtotal = invoiceTest.sumSubTotals();
		double actualTaxTotal = invoiceTest.sumTaxes();
		double actualGrandTotal = invoiceTest.sumTotals();

		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);

	}

}
