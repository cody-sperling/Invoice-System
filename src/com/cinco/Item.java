package com.cinco;

import java.util.UUID;

/**
 * This is an abstract class that represents an item with child classes:
 * License, Service, and Equipment
 * 
 * Cody Sperling 4-24-26
 */

public abstract class Item {

	private UUID uuid;
	private String name;
	private int itemId;

	public Item(UUID uuid, String name) {
		super();
		this.uuid = uuid;
		this.name = name;
	}

	public Item(UUID uuid, String name, int itemId) {
		super();
		this.uuid = uuid;
		this.name = name;
		this.itemId = itemId;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public int getitemId() {
		return itemId;
	}

	/**
	 * This method is an abstract method created so that all items need a way to
	 * calculate a total
	 * 
	 * @return
	 */

	public abstract double getTotal();

	/**
	 * This method is an abstract method created so that all items need a way to
	 * calculate a subtotal
	 * 
	 * @return
	 */

	public abstract double getSubTotal();

	/**
	 * This method is an abstract method created so that all items need a way to
	 * calculate a tax total
	 * 
	 * @return
	 */
	public abstract double getTax();

}
