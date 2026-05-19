package com.cinco;

import java.util.List;

/**
 * Cody Sperling 2-27-26
 * 
 * This class calls the methods designed in the ConversionUtils and
 * LoadDataUtils classes to parse the companies,items and persons CSV files into
 * their appropriate objects and then call the ToJSON methods to output the JSON
 * files
 */

public class DataConverter {

	public static void main(String[] args) {

		List<Person> peopleList = DataLoaderCSV.loadPeopleData("data/Persons.csv");

		List<Company> companiesList = DataLoaderCSV.loadCompanyData("data/Companies.csv", peopleList);

		List<Item> itemsList = DataLoaderCSV.loadItemsData("data/Items.csv");

		ConversionUtils.convertToJSON(peopleList, "data/Persons.json");
		ConversionUtils.convertToJSON(companiesList, "data/Companies.json");
		ConversionUtils.convertToJSON(itemsList, "data/Items.json");

	}
}
