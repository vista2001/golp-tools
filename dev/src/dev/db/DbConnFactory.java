package dev.db;


public class DbConnFactory {
	public static DbConnectImpl dbConnCreator(){
		return new DbConnectImpl();
	}
}
