package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * This data class models a License sold by CCC Cody Sperling 4-24-26
 */

public class License extends Item {

	private double serviceFee;
	private double annualFee;
	private LocalDate beginningDate;
	private LocalDate endingDate;

	public License(License newLicense, LocalDate beginningDate, LocalDate endingDate) {
		super(newLicense.getUuid(), newLicense.getName());
		this.serviceFee = newLicense.serviceFee;
		this.annualFee = newLicense.annualFee;
		this.beginningDate = LocalDate.from(beginningDate);
		this.endingDate = LocalDate.from(endingDate);

	}

	public License(UUID uuid, String name, double serviceFee, double annualFee) {
		super(uuid, name);
		this.serviceFee = serviceFee;
		this.annualFee = annualFee;
	}

	public License(int itemId, UUID uuid, String name, double serviceFee, double annualFee) {
		super(uuid, name, itemId);
		this.serviceFee = serviceFee;
		this.annualFee = annualFee;
	}

	public License(UUID uuid, String name, double serviceFee, double annualFee, LocalDate beginningDate,
			LocalDate endingDate) {
		super(uuid, name);
		this.serviceFee = serviceFee;
		this.annualFee = annualFee;
		this.beginningDate = beginningDate;
		this.endingDate = endingDate;
	}

	public double getServiceFee() {
		return serviceFee;
	}

	public double getAnnualFee() {
		return annualFee;
	}

	@Override
	public String toString() {
		return String.format(
				"%s (License)\t%s\n\t%d days (%s ---> %s) @ $%.2f / year\n\tService Fee: $%.2f\n%s%60s%30s",
				this.getUuid(), this.getName(), this.getDaysBetween(), this.beginningDate, this.endingDate,
				this.annualFee, this.serviceFee, "          ", String.format("$%.2f", this.getTax()),
				String.format("$%.2f", this.getSubTotal()));
	}

	public LocalDate getBeginningDate() {
		return beginningDate;
	}

	public LocalDate getEndingDate() {
		return endingDate;
	}

	/**
	 * This method returns the grand total cost of a license that has been acquired
	 * with the help of CCC
	 * 
	 * @return
	 */

	@Override
	public double getTotal() {
		return this.getSubTotal();
	}

	/**
	 * This method returns the subtotal cost of a license that has been acquired
	 * with the help of CCC
	 * 
	 * @return
	 */

	@Override
	public double getSubTotal() {

		BigDecimal bd = new BigDecimal(
				Double.toString(((this.annualFee / 365.00) * getDaysBetween()) + this.serviceFee));
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		double total = bd.doubleValue();

		return total;
	}

	/**
	 * This method returns the tax on a license that has been acquired with the help
	 * of CCC
	 * 
	 * @return
	 */

	@Override
	public double getTax() {
		return 0.00;
	}

	/**
	 * 
	 * This method is an intermediate helper function used assist in calculating the
	 * subtotal and totals by returning the number of days between 2 dates
	 * 
	 * @return
	 */

	public long getDaysBetween() {

		long daysBetween = ChronoUnit.DAYS.between(this.beginningDate, this.endingDate);

		return daysBetween + 1;
	}

}
