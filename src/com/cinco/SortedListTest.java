package com.cinco;

import java.util.Comparator;
import java.util.Random;

/**
 * This class contains some adhoc tests for my implementation of SortedList Cody
 * Sperling 4-28-26
 */
public class SortedListTest {

	public static void main(String[] args) {
		SortedList<Integer> list = new SortedList<Integer>(Comparator.naturalOrder());

		Random random = new Random();

		for (int i = 0; i < 1000; i++) {
			int randnum = random.nextInt();
			list.addElement(randnum);

		}

		for (int i = 1; i < list.getSize() - 1; i++) {
			if (list.getElement(i) > list.getElement(i + 1) || list.getElement(i) < list.getElement(i - 1)) {
				throw new RuntimeException("Out of Order");
			}
		}
		System.out.println(list);

		for (int i = 999; i >= 0; i--) {
			list.deleteElement(i);
		}
		System.out.println(list);

		SortedList<Integer> list2 = new SortedList<Integer>(Comparator.naturalOrder());
		for (int i = 0; i < 10; i++) {
			list2.addElement(i * 10);

		}

		System.out.println(list2);
		list2.deleteElement((Integer) 90);
		System.out.println(list2);
		list2.deleteElement((Integer) 50);
		System.out.println(list2);
		list2.deleteElement((Integer) 0);
		System.out.println(list2);
		list2.deleteElement((Integer) 10);
		list2.deleteElement((Integer) 20);
		list2.deleteElement((Integer) 30);
		list2.deleteElement((Integer) 40);
		list2.deleteElement((Integer) 60);
		list2.deleteElement((Integer) 70);
		list2.deleteElement((Integer) 80);
		System.out.println(list2);

		SortedList<Integer> list3 = new SortedList<Integer>(Comparator.naturalOrder());
		for (int i = 0; i < 10; i++) {
			list3.addElement(i * 10);

		}

		for (int num : list3) {
			System.out.println(num);

		}
	}

}
