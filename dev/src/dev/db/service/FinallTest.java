package dev.db.service;

import java.sql.SQLException;

import dev.util.DevLogger;

public class FinallTest {
	public static void test() throws SQLException {
		try {
			SQLException se = new SQLException("hhh");
			throw se;
		} finally {
			SQLException se = new SQLException("hhh1");
			throw se;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			test();
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
	}

}
