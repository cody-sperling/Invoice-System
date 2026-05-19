package com.cinco;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Cody Sperling
 * 
 * 2-27-26
 * 
 * This class contains the method used to take lists of Persons, Items and
 * Companies to JSON files of those lists
 */

public class ConversionUtils {

	/**
	 * This method outputs a JSON file of the desired list of objects with the
	 * desired name/location
	 * 
	 * @param <T>
	 * @param listOfObj
	 * @param outputName
	 */

	public static <T> void convertToJSON(List<T> listOfObj, String outputName) {

		try {
			PrintWriter pw = new PrintWriter(outputName);

			Gson gson = new GsonBuilder().setPrettyPrinting()
					.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

			String jsonOutput = gson.toJson(listOfObj);

			pw.write(jsonOutput);

			pw.close();
		} catch (Exception fnfe) {
			throw new RuntimeException(fnfe);
		}

	}

}
