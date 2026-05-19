package com.cinco;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * This class contains my implementation of a sorted array based data structure
 * Cody Sperling 4-28-26
 * 
 * @param <T>
 */

public class SortedList<T> implements Iterable<T> {

	private T[] array;
	private int size;
	private final Comparator<T> comparator;

	@SuppressWarnings("unchecked")
	public SortedList(Comparator<T> comparator) {
		super();
		this.array = (T[]) new Object[10];
		this.size = 0;
		this.comparator = comparator;
	}

	/**
	 * This is the only method that adds an element to the SortedList, this method
	 * checks to see if the list needs to expand, finds the correct point to insert
	 * and then shifts everything down to make room before insertion
	 * 
	 * @param element
	 */

	public void addElement(T element) {

		expand();

		int index = findInsertionIndex(element);

		for (int i = this.size - 1; i >= index; i--) {
			array[i + 1] = array[i];
		}
		array[index] = element;

		this.size++;

	}

	/**
	 * This helper method checks the SortedList and finds the correct index to
	 * insert an element, null elements throw an exception
	 * 
	 * @param element
	 * @return
	 */
	private int findInsertionIndex(T element) {

		if (element == null) {
			throw new RuntimeException("null cannot be added to list");
		}

		int result = 0;

		if (this.size == 0) {
			result = 0;

			return result;
		}

		for (int i = this.size - 1; i >= 0; i--) {
			if (this.comparator.compare(element, this.array[i]) > 0) {
				result = i + 1;
				return result;
			}

		}

		return result;
	}

	@Override
	public String toString() {

		if (this.size == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");

		for (int i = 0; i < this.size - 1; i++) {
			sb.append(this.array[i].toString() + ", ");
		}
		sb.append(this.array[this.size - 1]);

		sb.append("]");

		return sb.toString();
	}

	/**
	 * Deletes an element at a given index assuming it is valid. Throws an Exception
	 * for invalid indices
	 * 
	 * @param index
	 */
	public void deleteElement(int index) {
		if (validIndex(index)) {

			if (index == this.size - 1) {
				this.array[index] = null;
				this.size--;
				return;
			}

			for (int i = index; i < this.size; i++) {
				this.array[i] = this.array[i + 1];
			}

		} else {
			throw new RuntimeException("index at " + index + " doesn't exist");
		}

		this.size--;
	}

	/**
	 * Searches for and deletes an element, throws an exception if the element
	 * doesn't exist
	 * 
	 * @param element
	 */

	public void deleteElement(T element) {
		int index = getIndex(element);
		if (index == -1) {
			throw new RuntimeException(element + " does not exist within the SortedList");
		}
		deleteElement(index);

	}

	/**
	 * Returns element at the given index
	 * 
	 * @param index
	 * @return
	 */

	public T getElement(int index) {
		if (validIndex(index)) {
			return this.array[index];
		} else {
			throw new RuntimeException("index at " + index + " doesn't exist");
		}

	}

	/**
	 * Binary searches for and returns the index of a given element in the list,
	 * returns -1 if the element can't be found
	 * 
	 * @param key
	 * @return
	 */

	public int getIndex(T key) {
		int left = 0;
		int right = this.size - 1;

		while (left <= right) {
			int middle = (right + left) / 2;
			if (this.comparator.compare(this.array[middle], key) == 0) {
				return middle;
			} else if (this.comparator.compare(key, this.array[middle]) > 0) {
				left = middle + 1;

			} else {
				right = middle - 1;
			}
		}
		return -1;
	}

	/**
	 * Checks if the Sortedlist has reached the capacity of the internal array and
	 * updates the array to contain 20 new spots
	 */
	private void expand() {

		if (this.size == this.array.length) {
			this.array = Arrays.copyOf(array, this.array.length + 20);
		}
	}

	public int getSize() {
		return this.size;
	}

	/**
	 * checks and returns false if the index is less than 0 or bigger than the size
	 * 
	 * @param index
	 * @return
	 */
	private boolean validIndex(int index) {
		if (index >= this.size || index < 0) {
			return false;
		}
		return true;
	}

	@Override
	public Iterator<T> iterator() {

		return new SortedListIterator<T>(this);

	}

	/**
	 * converts a Java list to a SortedList
	 * 
	 * @param <T>
	 * @param list
	 * @param comp
	 * @return
	 */
	public static <T> SortedList<T> fromList(List<T> list, Comparator<T> comp) {

		SortedList<T> sortedList = new SortedList<T>(comp);
		for (T element : list) {
			sortedList.addElement(element);
		}
		return sortedList;
	}

	/**
	 * This private class contains the iterator for the SortedList data structure
	 * Cody Sperling 4-29-26
	 * 
	 * @param <T>
	 */

	@SuppressWarnings("hiding")
	private class SortedListIterator<T> implements Iterator<T> {

		private SortedList<T> list;
		private int index;

		SortedListIterator(SortedList<T> list) {
			this.list = list;
			this.index = 0;
		}

		/**
		 * checks if the SortedList has a next element
		 */
		@Override
		public boolean hasNext() {

			return (this.list.getSize() > index);
		}

		/**
		 * returns the current element and moves the pointer to the next element
		 */
		@Override
		public T next() {
			if (hasNext()) {
				T element = list.getElement(index);
				index++;
				return element;
			} else {
				throw new java.util.NoSuchElementException();
			}
		}

	}

}
