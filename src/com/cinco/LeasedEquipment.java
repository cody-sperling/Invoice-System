package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * This class models a piece of equipment that has been leased from CCC Cody
 * Sperling 4-24-26
 */

public class LeasedEquipment extends Equipment {

	public LeasedEquipment(UUID uuid, String name, double perUnitCost, int numOfUnits) {
		super(uuid, name, perUnitCost, numOfUnits);
	}

	public LeasedEquipment(Equipment newEquipment, int numOfUnits) {
		super(newEquipment.getUuid(), newEquipment.getName(), newEquipment.getPerUnitCost(), numOfUnits);
	}

	@Override
	public String toString() {

		return String.format("%s (Lease)\t%s\n\t%d units\n%s%60s%30s", this.getUuid(), this.getName(),
				this.getNumOfUnits(), "          ", String.format("$%.2f", this.getTax()),
				String.format("$%.2f", this.getTotal()));

	}

	/**
	 * This method is used to calculate the grand total cost of an equipment that
	 * has been leased from CCC
	 */

	@Override
	public double getTotal() {

		BigDecimal bd = new BigDecimal(Double.toString((this.getSubTotal() / 36.00) + getTax()));
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		double total = bd.doubleValue();
		return total;
	}

	/**
	 * This method is used to calculate the subtotal cost of an equipment that has
	 * been leased from CCC
	 */

	@Override
	public double getSubTotal() {

		BigDecimal bd = new BigDecimal(Double.toString(((double) this.getNumOfUnits() * this.getPerUnitCost()) * 1.5));
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		double total = bd.doubleValue();
		return total;
	}

	/**
	 * This method is used to calculate the tax on an equipment that has been leased
	 * from CCC
	 */

	public double getTax() {

		if (this.getSubTotal() < 2000.00) {
			return 0.0;
		} else if (this.getSubTotal() < 7000.00) {
			return 175.00;
		} else {
			return 350.00;
		}
	}
}
