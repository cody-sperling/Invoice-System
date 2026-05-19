package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * This class models a piece of equipment that has been sold from CCC Cody
 * Sperling 4-24-26
 */

public class SoldEquipment extends Equipment {

	public SoldEquipment(UUID uuid, String name, double perUnitCost, int numOfUnits) {
		super(uuid, name, perUnitCost, numOfUnits);
	}

	public SoldEquipment(Equipment newEquipment, int numOfUnits) {
		super(newEquipment.getUuid(), newEquipment.getName(), newEquipment.getPerUnitCost(), numOfUnits);
	}

	@Override
	public String toString() {
		return String.format("%s (Purchase)\t%s\n\t%d units @ $%.2f/unit\n%s%60s%30s", this.getUuid(), this.getName(),
				this.getNumOfUnits(), this.getPerUnitCost(), "          ", String.format("$%.2f", this.getTax()),
				String.format("$%.2f", this.getSubTotal()));
	}

	/**
	 * This method returns the grand total cost of a piece of equipment sold by CCC
	 */

	@Override
	public double getTotal() {
		return this.getSubTotal() + this.getTax();
	}

	/**
	 * This method returns the subtotal cost of a piece of equipment sold by CCC
	 */

	@Override
	public double getSubTotal() {

		BigDecimal bd = new BigDecimal(Double.toString(((double) this.getNumOfUnits()) * this.getPerUnitCost()));
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		double total = bd.doubleValue();
		return total;
	}

	/**
	 * This method returns the tax cost of an item purchased from CCC
	 */

	public double getTax() {

		BigDecimal bd = new BigDecimal(Double.toString((this.getSubTotal() * .0525)));
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		double tax = bd.doubleValue();
		return tax;
	}

}
