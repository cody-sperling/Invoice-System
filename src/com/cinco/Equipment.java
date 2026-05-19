package com.cinco;

import java.util.UUID;

/**
 * This data class models a piece of equipment sold by CCC Cody Sperling 4-24-26
 */

public class Equipment extends Item {

	private double perUnitCost;
	private int numOfUnits;

	public Equipment(UUID uuid, String name, double perUnitCost) {
		super(uuid, name);
		this.perUnitCost = perUnitCost;
	}

	public Equipment(int itemId, UUID uuid, String name, double perUnitCost) {
		super(uuid, name, itemId);
		this.perUnitCost = perUnitCost;
	}

	public Equipment(UUID uuid, String name, double perUnitCost, int numOfUnits) {
		super(uuid, name);
		this.perUnitCost = perUnitCost;
		this.numOfUnits = numOfUnits;
	}

	public int getNumOfUnits() {
		return numOfUnits;
	}

	public double getPerUnitCost() {
		return perUnitCost;
	}

	@Override
	public String toString() {
		return "Equipment [perUnitCost=" + perUnitCost + ", numOfUnits=" + numOfUnits + "]";
	}

	/**
	 * This method is overridden in both the child classes of equipment so it is
	 * never used
	 */
	@Override
	public double getTotal() {
		return 0;
	}

	/**
	 * This method is overridden in both the child classes of equipment so it is
	 * never used
	 */
	@Override
	public double getSubTotal() {
		return 0;
	}

	/**
	 * This method is overridden in both the child classes of equipment so it is
	 * never used
	 */

	@Override
	public double getTax() {
		return 0;
	}

}
