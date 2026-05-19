package com.cinco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JUnit test suite for the invoice system. Individually tests
 * SoldEquipment,LeasedEquipment,Services, and Licenses
 * 
 * Cody Sperling 4-24-26
 */
public class EntityTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Creates an instance of purchased equipment and tests if its cost and tax
	 * calculations are correct as well as tests the toString method for accuracy.
	 *
	 * 
	 */
	@Test
	public void testEquipmentPurchase() {

		UUID uuid = UUID.fromString("673a37b6-afc2-428b-a5ec-8a3d67f556c5");
		String name = "RGB KeyBoard";
		double pricePerUnit = 149.99;
		int numberOfUnits = 25;

		SoldEquipment test = new SoldEquipment(uuid, name, pricePerUnit, numberOfUnits);

		double expectedCost = 3749.75;
		double expectedTax = 196.86;
		double expectedTotal = 3946.61;

		double actualCost = test.getSubTotal();
		double actualTax = test.getTax();
		double actualTotal = test.getTotal();

		String s = test.toString();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);

		assertTrue(s.contains("Purchase"));
		assertTrue(s.contains(test.getName()));
		assertTrue(s.contains(test.getUuid().toString()));
		assertTrue(s.contains(String.valueOf(test.getNumOfUnits())));
		assertTrue(s.contains(String.valueOf(test.getPerUnitCost())));
		assertTrue(s.contains(String.valueOf(test.getTax())));
		assertTrue(s.contains(String.valueOf(test.getTotal())));

	}

	/**
	 * Creates an instance of leased equipment and tests if its cost and tax
	 * calculations are correct as well as tests the toString method for accuracy.
	 *
	 * 
	 */
	@Test
	public void testEquipmentLease() {
		// data values
		UUID uuid = UUID.fromString("c4e239dd-ddfd-447a-9afa-bdefe08f18f3");
		String name = "I280 Razor Headset";
		double pricePerUnit = 84.99;
		int numberOfUnits = 200;

		LeasedEquipment test = new LeasedEquipment(uuid, name, pricePerUnit, numberOfUnits);

		double expectedCost = 25497.00;
		double expectedTax = 350.00;
		double expectedTotal = 1058.25;

		double actualCost = test.getSubTotal();
		double actualTax = test.getTax();
		double actualTotal = test.getTotal();
		String s = test.toString();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);

		assertTrue(s.contains("Lease"));
		assertTrue(s.contains(test.getName()));
		assertTrue(s.contains(test.getUuid().toString()));
		assertTrue(s.contains(String.valueOf(test.getNumOfUnits())));
		assertTrue(s.contains(String.valueOf(test.getPerUnitCost())));
		assertTrue(s.contains(String.valueOf(test.getTax())));
		assertTrue(s.contains(String.valueOf(test.getTotal())));

	}

	/**
	 * Creates an instance of service and tests if its cost and tax calculations are
	 * correct as well as tests the toString method for accuracy.
	 *
	 * 
	 */
	@Test
	public void testService() {

		UUID uuid = UUID.fromString("e7f1f962-8648-439e-95dd-08d034273e28");
		String name = "Server Maintenance";
		double perHourCost = 25.00;
		double billableHour = 3;

		UUID personUuid = UUID.fromString("d607eebd-b731-4fce-85b5-1f35bdc138e7");

		Person servicePerson = new Person(personUuid, "Harry", "Kane", "610-784-6790");

		Service test = new Service(uuid, name, perHourCost, billableHour, servicePerson);

		double expectedCost = 200.00;
		double expectedTax = 6.30;
		double expectedTotal = 206.30;

		double actualCost = test.getSubTotal();
		double actualTax = test.getTax();
		double actualTotal = test.getTotal();

		String s = test.toString();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);

		assertTrue(s.contains("Service"));
		assertTrue(s.contains(test.getName()));
		assertTrue(s.contains(test.getUuid().toString()));
		assertTrue(s.contains(String.valueOf(test.getBillableHours())));
		assertTrue(s.contains(String.valueOf(test.getPerHourCost())));
		assertTrue(s.contains(String.valueOf(test.getTax())));
		assertTrue(s.contains(String.valueOf(test.getTotal())));

	}

	/**
	 * Creates an instance of license and tests if its cost and tax calculations are
	 * correct as well as tests the toString method for accuracy.
	 *
	 * 
	 */
	@Test
	public void testLicense() {

		UUID uuid = UUID.fromString("e7104a1b-4e97-497d-99b3-0e4c5f5ffb8a");
		String name = "Adobe Premium License";
		double serviceFee = 84.99;
		double annualFee = 200;
		LocalDate beginDate = LocalDate.of(2023, 3, 11);
		LocalDate endDate = LocalDate.of(2025, 3, 11);

		License test = new License(uuid, name, serviceFee, annualFee, beginDate, endDate);

		double expectedCost = 485.54;
		double expectedTax = 0.00;
		double expectedTotal = 485.54;

		double actualCost = test.getSubTotal();
		double actualTax = test.getTax();
		double actualTotal = test.getTotal();

		String s = test.toString();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);

		assertTrue(s.contains("License"));
		assertTrue(s.contains(test.getName()));
		assertTrue(s.contains(test.getUuid().toString()));
		assertTrue(s.contains(String.valueOf(test.getServiceFee())));
		assertTrue(s.contains(String.valueOf(test.getDaysBetween())));
		assertTrue(s.contains(String.valueOf(test.getAnnualFee())));
		assertTrue(s.contains(String.valueOf(test.getTotal())));

	}

}
