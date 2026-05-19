package com.cinco;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 * This a helper class designed to make the GSON functions on ConversionUtils
 * work with the addition of local date in the subclasses of item It works to
 * add backwards comparability to the assignment 1 code Cody Sperling 4-24-26
 */

public class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * This function converts the local dates to a format that GSON will be able to
	 * read
	 */

	@Override
	public JsonElement serialize(final LocalDate date, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(date.format(formatter));
	}

	/**
	 * This function returns a local date from a JSON formatting
	 */

	@Override
	public LocalDate deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
			throws JsonParseException {
		return LocalDate.parse(json.getAsString(), formatter);
	}
}