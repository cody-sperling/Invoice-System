package com.cinco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection factory that includes methods and variables used to connect to my
 * specific database on the Nuros Server
 * 
 * Cody Sperling 4-24-26
 */
public class DatabaseUtils {

	private static final String USERNAME = "redacted";

	private static final String PASSWORD = "redacted";

	private static final String URL = "redacted";

	/**
	 * Creates and returns a connection to Nuros under my information
	 */
	protected static Connection getConnection() {

		try {
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
	}

	/**
	 * Closes a provided connection, used to avoid so many try/catches in
	 * DataLoaderDatabase
	 * 
	 * @param c
	 */
	protected static void close(Connection c) {

		try {
			c.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
	}
}
