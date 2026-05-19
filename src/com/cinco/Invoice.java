package com.cinco;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * This class models an invoice from a customer of CCC Cody Sperling 4-24-26
 */

public class Invoice {

	private UUID uuid;
	private Company customer;
	private Person salesPerson;
	private LocalDate dateCreated;
	private int invoiceId;
	private List<Item> listOfItems;

	public Invoice(UUID uuid, Company customer, Person salesPerson, LocalDate dateCreated, List<Item> listOfItems) {
		super();
		this.uuid = uuid;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.dateCreated = dateCreated;
		this.listOfItems = new ArrayList<>();
	}

	public Invoice(UUID uuid, Company customer, Person salesPerson, LocalDate dateCreated) {
		super();
		this.uuid = uuid;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.dateCreated = dateCreated;
		this.listOfItems = new ArrayList<>();

	}

	public Invoice(UUID uuid, Company customer, Person salesPerson, LocalDate dateCreated, int invoiceId) {
		super();
		this.uuid = uuid;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.dateCreated = dateCreated;
		this.invoiceId = invoiceId;
		this.listOfItems = new ArrayList<>();

	}

	public Company getCustomer() {
		return customer;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Person getSalesPerson() {
		return salesPerson;
	}

	public List<Item> getListOfItems() {
		return new ArrayList<>(listOfItems);
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void addItem(Item item) {

		this.listOfItems.add(item);

	}

	@Override
	public String toString() {
		return "Invoice [uuid=" + uuid + ", customer=" + customer + ", salesPerson=" + salesPerson + ", dateCreated="
				+ dateCreated + ", listOfItems=" + listOfItems + "]";
	}

	/**
	 * This method returns the sum of the grand totals from all the items in
	 * listOfItems
	 * 
	 * @return
	 */
	public double sumTotals() {

		double sum = 0.00;

		for (Item i : listOfItems) {

			sum += i.getTotal();

		}

		return sum;
	}

	/**
	 * This method returns the sum of the subtotals of all items in listOfItems
	 * 
	 * If an item is a LeasedEquipment the get total is called because the get total
	 * provides the amortized version of the cost
	 * 
	 * @return
	 */

	public double sumSubTotals() {

		double sum = 0.00;

		for (Item i : listOfItems) {

			if (!(i instanceof LeasedEquipment)) {
				sum += i.getSubTotal();
			} else {

				sum += i.getTotal();

			}

		}

		return sum;
	}

	/**
	 * This method returns the sum of all the taxes that need to be paid on the
	 * items of listOfItems
	 * 
	 * @return
	 */
	public double sumTaxes() {

		double sum = 0.00;

		for (Item i : listOfItems) {

			sum += i.getTax();

		}

		return sum;
	}

	/**
	 * This comparator orders invoices by moving the invoice with the largest total
	 * first, the secondary sorting condition is by the Invoice UUIDs
	 */

	static Comparator<Invoice> byLargestTotal = new Comparator<Invoice>() {
		@Override
		public int compare(Invoice inv1, Invoice inv2) {

			double sum1 = inv1.sumTotals();
			double sum2 = inv2.sumTotals();

			int comparision = Double.compare(sum1, sum2);

			if (comparision != 0) {
				return -(comparision);
			}

			return (inv1.getUuid().compareTo(inv2.getUuid()));

		}
	};

	/**
	 * This comparator sorts invoices lexicographically by the customer(Company)'s
	 * name first, the secondary sorting condition is by the Invoice UUIDs
	 */

	static Comparator<Invoice> byCustomerName = new Comparator<Invoice>() {
		@Override
		public int compare(Invoice inv1, Invoice inv2) {

			int comparision = inv1.getCustomer().getName().compareToIgnoreCase(inv2.getCustomer().getName());

			if (comparision != 0) {
				return comparision;

			}

			return (inv1.getUuid().compareTo(inv2.getUuid()));

		}
	};

}
