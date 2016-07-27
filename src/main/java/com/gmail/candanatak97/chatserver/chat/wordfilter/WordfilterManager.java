package com.gmail.candanatak97.chatserver.chat.wordfilter;

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
public class WordfilterManager {
	private ArrayList<Wordfilter> badwords;
	
	private Database database;
	
	public WordfilterManager(Database database) {
		this.database = database;
	}
	
	public Connection getConnection() {
		return this.database.getConnection();
	}
	
	public ArrayList<Wordfilter> getBadwords() {
		return this.badwords;
	}
	
	public void initialize() {
		this.badwords = new ArrayList<Wordfilter>();
		
		try {
			Statement stmt = this.getConnection().createStatement();
			stmt.executeQuery("SELECT * FROM `wordfilter`");
			
			ResultSet resultSet = stmt.getResultSet();

			while(resultSet.next()) {
				this.getBadwords().add(new Wordfilter(resultSet.getString("word"), resultSet.getString("replacement"), Boolean.parseBoolean(resultSet.getString("enabled"))));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
