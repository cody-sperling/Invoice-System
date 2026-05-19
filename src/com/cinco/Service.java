package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * This data class models a service sold by CCC Cody Sperling 4-24-26
 */

public class Service extends Item {

	private double perHourCost;
	private double billableHours;
	private Person servicePerson;

	public Service(UUID uuid, String name, double perHourCost) {
		super(uuid, name);
		this.perHourCost = perHourCost;
	}

	public Service(int itemId, UUID uuid, String name, double perHourCost) {
		super(uuid, name, itemId);
		this.perHourCost = perHourCost;
	}

	public Service(UUID uuid, String name, double perHourCost, double billableHours, Person servicePerson) {
		super(uuid, name);
		this.perHourCost = perHourCost;
		this.billableHours = billableHours;
		this.servicePerson = servicePerson;
	}

	public Service(Service newService, Person servicePerson, Double billableHours) {
		super(newService.getUuid(), newService.getName());
		this.perHourCost = newService.perHourCost;
		this.billableHours = billableHours;
		this.servicePerson = servicePerson;
	}

	public double getPerHourCost() {
		return perHourCost;
	}

	public double getBillableHours() {
		return billableHours;
	}

	public Person getServicePerson() {
		return servicePerson;
	}

	@Override
	public String toString() {
		return String.format("%s (Service)\t%s\n\t%.2f hours @ $%.2f/hour\nServiced By:\n\t%s\n%s%60s%30s",
				this.getUuid(), this.getName(), this.getBillableHours(), this.getPerHourCost(), this.servicePerson,
				"          ", String.format("$%.2f", this.getTax()), String.format("$%.2f", this.getSubTotal()));

	}

	/**
	 * This method returns grand total cost of a Service performed by CCC
	 */

	@Override
	public double getTotal() {
		double total = this.getSubTotal() + this.getTax();
		return total;
	}

	/**
	 * This method returns subtotal cost of a Service performed by CCC
	 * 
	 */

	@Override
	public double getSubTotal() {

		BigDecimal bd = new BigDecimal(Double.toString((this.billableHours * this.perHourCost) + 125.00));
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		double total = bd.doubleValue();

		return total;
	}

	/**
	 * This method returns a tax cost of a Service performed by CCC
	 */

	public double getTax() {

		BigDecimal bd = new BigDecimal(Double.toString((this.getSubTotal()) * .0315));
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		double total = bd.doubleValue();
		return total;
	}

}
