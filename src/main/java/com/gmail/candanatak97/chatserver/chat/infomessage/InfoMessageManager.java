package com.gmail.candanatak97.chatserver.chat.infomessage;

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
public class InfoMessageManager {
	private ArrayList<InfoMessage> messages;
	
	private Database database;
	
	public InfoMessageManager(Database database) {
		this.database = database;
	}
	
	public Connection getConnection() {
		return this.database.getConnection();
	}
	
	public ArrayList<InfoMessage> getEnabledMessages() {
		ArrayList<InfoMessage> messages = new ArrayList<InfoMessage>();
		
		for(InfoMessage infoMessage : getMessages()) {
			if(infoMessage.isEnabled()) {
				messages.add(infoMessage);
			}
		}
		
		return messages;
	}
	
	public ArrayList<InfoMessage> getMessages() {
		return this.messages;
	}
	
	public void initialize() {
		this.messages = new ArrayList<InfoMessage>();
		
		try {
			Statement stmt = this.getConnection().createStatement();
			stmt.executeQuery("SELECT * FROM `info_messages`");
			
			ResultSet resultSet = stmt.getResultSet();

			while(resultSet.next()) {
				this.getMessages().add(new InfoMessage(resultSet.getString("sender_name"), resultSet.getString("message"), Boolean.parseBoolean(resultSet.getString("enabled"))));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
