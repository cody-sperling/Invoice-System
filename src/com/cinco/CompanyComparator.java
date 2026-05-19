package com.cinco;

import java.util.Comparator;
import java.util.List;

/**
 * This class contains the method used to sort companies by the grandtotal of
 * all their invoices Cody Sperling 4-28-26
 */

public class CompanyComparator implements Comparator<Company> {

	private static List<Invoice> invoices;

	public CompanyComparator(List<Invoice> invoices) {
		super();
		CompanyComparator.invoices = invoices;
	}

	@Override
	public int compare(Company comp1, Company comp2) {

		double compSum1 = InvoiceReportsUtils.calculateCompanyInvoiceTotal(comp1, invoices);
		double compSum2 = InvoiceReportsUtils.calculateCompanyInvoiceTotal(comp2, invoices);

		int comparision = Double.compare(compSum1, compSum2);

		if (comparision != 0) {
			return comparision;
		}

		return (comp1.getCompanyUuid().compareTo(comp2.getCompanyUuid()));

	}

	/**
	 * This method returns a comparator of companies based on a provided list of
	 * Invoices
	 * 
	 * @param list
	 * @return
	 */

	public static Comparator<Company> byCompanyTotal(List<Invoice> list) {
		Comparator<Company> comp = new CompanyComparator(list);
		return comp;
	}
}
