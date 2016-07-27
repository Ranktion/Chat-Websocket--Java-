package com.gmail.candanatak97.chatserver.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class Database {
	private Connection connection;

	public Database(String mHostname, String mUsername, String mPassword, String mDatabase, int mPort) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String dbString = "jdbc:mysql://" + mHostname + ":" + mPort + "/" + mDatabase + "?" + "user=" + mUsername + "&" + "password=" + mPassword;
			this.connection = DriverManager.getConnection(dbString, mUsername, mPassword);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public boolean isConnected() {
		try {
			return (this.connection != null && !this.connection.isClosed());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return false;
	}
}
