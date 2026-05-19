package com.cinco;

import java.io.PrintWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains all methods and utility methods that aid in generating
 * reports
 * 
 * Cody Sperling 4-28-26
 */

public class InvoiceReportsUtils {

	/**
	 * This function returns the summary table as a string. The summary table shows
	 * the total, tax, # of items and customer for each invoice
	 * 
	 * @param invoiceList
	 * @return
	 */

	public static String generateSummaryReport(List<Invoice> invoiceList) {

		StringBuilder sb = new StringBuilder();

		int sumOfNumItems = 0;
		double sumTaxes = 0.0;
		double sumTotals = 0.0;

		String headerStr = String.format(
				"+------------------------------------------------------------------------------------------------+\n");
		String headerStr2 = String.format(
				"|Summary Report - By Total                                                                       |\n");

		String headerStr3 = String.format(
				"+------------------------------------------------------------------------------------------------+\n");
		String headerStr4 = String.format("%-40s%-25s%5s%12s%12s\n", "Invoice#", "Customer", "#Items", "Tax", "Total");

		sb.append(headerStr).append(headerStr2).append(headerStr3).append(headerStr4);

		for (Invoice i : invoiceList) {

			sumOfNumItems += i.getListOfItems().size();
			sumTaxes += i.sumTaxes();
			sumTotals += i.sumTotals();

			String row = String.format("%-40s%-25s%5d%12s%12s\n", i.getUuid(), i.getCustomer(),
					i.getListOfItems().size(), String.format("$%.2f", i.sumTaxes()),
					String.format("$%.2f", i.sumTotals()));

			sb.append(row);

		}

		String footer = String.format(
				"+------------------------------------------------------------------------------------------------+\n");

		sb.append(footer);

		String totals = String.format("%70d%12s%12s", sumOfNumItems, String.format("$%.2f", sumTaxes),
				String.format("$%.2f", sumTotals));

		sb.append(totals);

		String result = sb.toString();

		return result;

	}

	/**
	 * This function returns the Company Invoice Summary Report as a string. This
	 * report contains the number of invoices of each company and the grand total of
	 * all of each companies invoices
	 * 
	 * @param companiesList
	 * @param invoiceList
	 * @return
	 */

	public static String generateCompanyInvoiceSummaryReport(List<Company> companiesList, List<Invoice> invoiceList) {

		StringBuilder sb = new StringBuilder();

		Map<Company, Double> companyToInvoiceTotal = new HashMap<>();

		Map<Company, Integer> companyToNumInvoice = new HashMap<>();

		int numInvoiceSum = 0;
		double invoiceTotalValue = 0.0;

		for (Company company : companiesList) {

			double companyInvoiceSum = calculateCompanyInvoiceTotal(company, invoiceList);
			int numInvoices = calculateNumInvoices(company, invoiceList);

			companyToNumInvoice.put(company, numInvoices);
			companyToInvoiceTotal.put(company, companyInvoiceSum);

		}

		for (Integer i : companyToNumInvoice.values()) {
			numInvoiceSum += i;
		}

		for (Double num : companyToInvoiceTotal.values()) {
			invoiceTotalValue += num;
		}

		String header = String.format(
				"+------------------------------------------------------------------------------------------------+\n");

		sb.append(header);

		String header2 = String.format(
				"|Company Invoice Summary Report                                                                  |\n");

		sb.append(header2);

		String header3 = String.format(
				"+------------------------------------------------------------------------------------------------+\n");
		String columns = String.format("%-40s%-25s%5s\n", "Company", "# Invoices", "Grand Total\n");

		sb.append(header3);

		sb.append(columns);

		for (Company c : companiesList) {

			String row = String.format("%-40s%-25d%5s\n", c.getName(), companyToNumInvoice.get(c),
					String.format("$%.2f", companyToInvoiceTotal.get(c)));

			sb.append(row);

		}

		String footer = String.format(
				"+------------------------------------------------------------------------------------------------+\n");

		sb.append(footer);

		String totals = String.format("%-40s%-25d%5s", "", numInvoiceSum, String.format("$%.2f", invoiceTotalValue));

		sb.append(totals);

		String result = sb.toString();

		return result;
	}

	/**
	 * This report shows a detailed view of each invoice and returns it as a string
	 * 
	 * 
	 * @param listOfInvoices
	 * @return
	 */

	public static String generateItemizedInvoiceReport(List<Invoice> listOfInvoices) {

		StringBuilder sb = new StringBuilder();

		for (Invoice invoice : listOfInvoices) {

			String row1 = String.format("\nInvoice# (%s)\nDate:\t%s\nCustomer:\n%s (%s)\n%s\n%s\nSales Person:\n%s\n",
					invoice.getUuid(), invoice.getDateCreated(), invoice.getCustomer().getName(),
					invoice.getCustomer().getCompanyUuid(), invoice.getCustomer().getCompanyContact(),
					invoice.getCustomer().getAddress(), invoice.getSalesPerson());

			String row2 = String.format("Items (%d)%60s%30s\n", invoice.getListOfItems().size(), "Tax", "Total");
			String row3 = String.format(
					"-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");

			sb.append(row1).append(row2).append(row3);

			for (Item item : invoice.getListOfItems()) {

				String itemRow = String.format("%s\n", item);
				sb.append(itemRow);

			}

			String row4 = String.format(
					"-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
			String row5 = String.format("SubTotals %60s%30s\n", String.format("$%.2f", invoice.sumTaxes()),
					String.format("$%.2f", invoice.sumSubTotals()));
			String row6 = String.format("Grand Total %88s", String.format("$%.2f", invoice.sumTotals()));

			sb.append(row4).append(row5).append(row6);

		}
		String result = sb.toString();

		return result;

	}

	/**
	 * This is a helper function for generating the Company Invoice Summary Report,
	 * it takes a company and computes the number of invoices they have in the
	 * system
	 * 
	 * @param c
	 * @param listOfInvoice
	 * @return
	 */

	public static int calculateNumInvoices(Company c, List<Invoice> listOfInvoice) {

		int counter = 0;
		for (Invoice i : listOfInvoice) {

			if (c.getCompanyUuid().equals(i.getCustomer().getCompanyUuid())) {
				counter++;
			}

		}

		return counter;

	}

	/**
	 * This is a helper function for generating the Company Invoice Summary Report,
	 * it takes a company and computes the Totals sum of all invoices they have in
	 * the system
	 * 
	 * @param c
	 * @param c
	 * @param listOfInvoice
	 * @return
	 */

	public static double calculateCompanyInvoiceTotal(Company c, List<Invoice> listOfInvoice) {

		double sum = 0;
		for (Invoice i : listOfInvoice) {

			if (c.getCompanyUuid().equals(i.getCustomer().getCompanyUuid())) {

				sum += i.sumTotals();
			}

		}

		return sum;

	}

	/**
	 * This function compiles the 3 reports above and outputs them as a single file
	 * called output.txt
	 * 
	 * @param invoiceList
	 * @param companiesList
	 */

	public static void compileSingleReport(List<Invoice> invoiceList, List<Company> companiesList) {

		try {
			PrintWriter pw = new PrintWriter("data/output.txt");

			pw.write(generateSummaryReport(invoiceList));

			pw.println();
			pw.write(generateCompanyInvoiceSummaryReport(companiesList, invoiceList));
			pw.println();

			pw.write(generateItemizedInvoiceReport(invoiceList));

			pw.close();
		} catch (Exception fnfe) {
			throw new RuntimeException(fnfe);
		}

	}

	/**
	 * This is a slightly different version of generateSummaryReport that takes a
	 * SortedList as input instead of a Java List This function returns the summary
	 * table as a string. The summary table shows the total, tax, # of items and
	 * customer for each invoice
	 * 
	 * @param invoiceList
	 * @param title
	 * @return
	 */

	public static String generateSortedSummaryReport(SortedList<Invoice> invoiceList, String title) {

		StringBuilder sb = new StringBuilder();

		int sumOfNumItems = 0;
		double sumTaxes = 0.0;
		double sumTotals = 0.0;

		String headerStr = String.format(
				"+------------------------------------------------------------------------------------------------+\n");
		String headerStr2 = String.format(" %s\n", title);

		String headerStr3 = String.format(
				"+------------------------------------------------------------------------------------------------+\n");
		String headerStr4 = String.format("%-40s%-25s%5s%12s%12s\n", "Invoice#", "Customer", "#Items", "Tax", "Total");

		sb.append(headerStr).append(headerStr2).append(headerStr3).append(headerStr4);

		for (Invoice i : invoiceList) {

			sumOfNumItems += i.getListOfItems().size();
			sumTaxes += i.sumTaxes();
			sumTotals += i.sumTotals();

			String row = String.format("%-40s%-25s%5d%12s%12s\n", i.getUuid(), i.getCustomer(),
					i.getListOfItems().size(), String.format("$%.2f", i.sumTaxes()),
					String.format("$%.2f", i.sumTotals()));

			sb.append(row);

		}

		String footer = String.format(
				"+------------------------------------------------------------------------------------------------+\n");

		sb.append(footer);

		String totals = String.format("%70d%12s%12s", sumOfNumItems, String.format("$%.2f", sumTaxes),
				String.format("$%.2f", sumTotals));

		sb.append(totals);

		String result = sb.toString();

		return result;

	}

	/**
	 * This is a slightly different version of generateCompanyInvoiceSummaryReport
	 * that takes a SortedList as input This function returns the Company Invoice
	 * Summary Report as a string. This report contains the number of invoices of
	 * each company and the grand total of all of each companies invoices
	 * 
	 * @param companiesList
	 * @param invoiceList
	 * @return
	 */

	public static String generateSortedCompanyInvoiceSummaryReport(SortedList<Company> companiesList,
			List<Invoice> invoiceList) {

		StringBuilder sb = new StringBuilder();

		Map<Company, Double> companyToInvoiceTotal = new HashMap<>();

		Map<Company, Integer> companyToNumInvoice = new HashMap<>();

		int numInvoiceSum = 0;
		double invoiceTotalValue = 0.0;

		for (Company company : companiesList) {

			double companyInvoiceSum = calculateCompanyInvoiceTotal(company, invoiceList);
			int numInvoices = calculateNumInvoices(company, invoiceList);

			companyToNumInvoice.put(company, numInvoices);
			companyToInvoiceTotal.put(company, companyInvoiceSum);

		}

		for (Integer i : companyToNumInvoice.values()) {
			numInvoiceSum += i;
		}

		for (Double num : companyToInvoiceTotal.values()) {
			invoiceTotalValue += num;
		}

		String header = String.format(
				"+------------------------------------------------------------------------------------------------+\n");

		sb.append(header);

		String header2 = String.format(
				"|Company Invoice Summary Report                                                                  |\n");

		sb.append(header2);

		String header3 = String.format(
				"+------------------------------------------------------------------------------------------------+\n");
		String columns = String.format("%-40s%-25s%5s\n", "Company", "# Invoices", "Grand Total\n");

		sb.append(header3);

		sb.append(columns);

		for (Company c : companiesList) {

			String row = String.format("%-40s%-25d%5s\n", c.getName(), companyToNumInvoice.get(c),
					String.format("$%.2f", companyToInvoiceTotal.get(c)));

			sb.append(row);

		}

		String footer = String.format(
				"+------------------------------------------------------------------------------------------------+\n");

		sb.append(footer);

		String totals = String.format("%-40s%-25d%5s", "", numInvoiceSum, String.format("$%.2f", invoiceTotalValue));

		sb.append(totals);

		String result = sb.toString();

		return result;
	}

}
