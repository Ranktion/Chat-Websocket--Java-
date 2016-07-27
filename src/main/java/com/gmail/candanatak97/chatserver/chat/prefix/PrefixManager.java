package com.gmail.candanatak97.chatserver.chat.prefix;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.gmail.candanatak97.chatserver.database.Database;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class PrefixManager {
	private ArrayList<Prefix> prefixes;
	
	private Database database;
	
	public PrefixManager(Database database) {
		this.database = database;
	}
	
	public Connection getConnection() {
		return this.database.getConnection();
	}
	
	public ArrayList<Prefix> getPrefixes() {
		return this.prefixes;
	}
	
	public Prefix getPrefixById(int id) {
		for(Prefix prefix : this.getPrefixes()) {
			if(id == prefix.getId()) {
				return prefix;
			}
		}
		
		return null;
	}
	
	public void initialize() {
		this.prefixes = new ArrayList<Prefix>();
		
		try {
			Statement stmt = this.getConnection().createStatement();
			stmt.executeQuery("SELECT * FROM `prefixes`");
			
			ResultSet resultSet = stmt.getResultSet();

			while(resultSet.next()) {
				this.getPrefixes().add(new Prefix(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("prefix"), resultSet.getString("color")));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
