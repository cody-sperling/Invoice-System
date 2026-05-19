package com.cinco;

import java.util.List;

/**
 * Cody Sperling
 * 
 * 4-28-26
 * 
 * This main class calls methods to load all the lists of objects, output the 3
 * reports to standard output and lastly compile the report in output.txt
 */

public class InvoiceReport {

	public static void main(String[] args) {

		List<Person> peopleList = DataLoaderDatabase.loadPersons();

		List<Company> companiesList = DataLoaderDatabase.loadCompanies(peopleList);

		List<Item> itemsList = DataLoaderDatabase.loadItems();

		List<Invoice> invoiceList = DataLoaderDatabase.loadInvoices(peopleList, companiesList);

		DataLoaderDatabase.loadInvoiceItems(itemsList, invoiceList, peopleList);

		SortedList<Invoice> invoiceListByTotal = SortedList.fromList(invoiceList, Invoice.byLargestTotal);
		String byTotalSummaryReport = InvoiceReportsUtils.generateSortedSummaryReport(invoiceListByTotal,
				"Invoices By Total");
		System.out.println(byTotalSummaryReport);

		SortedList<Invoice> invoiceListByName = SortedList.fromList(invoiceList, Invoice.byCustomerName);
		String byNameSummaryReport = InvoiceReportsUtils.generateSortedSummaryReport(invoiceListByName,
				"Invoices By Cusomer");
		System.out.println(byNameSummaryReport);

		SortedList<Company> sortedCompanyList = SortedList.fromList(companiesList,
				CompanyComparator.byCompanyTotal(invoiceList));
		String byCompanyTotal = InvoiceReportsUtils.generateSortedCompanyInvoiceSummaryReport(sortedCompanyList,
				invoiceList);
		System.out.println(byCompanyTotal);

	}

}
