package com.gmail.candanatak97.chatserver.chat.permission.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.gmail.candanatak97.chatserver.database.Database;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class PermissionCommandManager {
	private Map<String, PermissionCommand> permissions;
	
	private Database database;
	
	public PermissionCommandManager(Database database) {
		this.database = database;
	}
	
	public Connection getConnection() {
		return this.database.getConnection();
	}
	
	public Map<String, PermissionCommand> getPermissions() {
		return this.permissions;
	}
	
	public PermissionCommand getPermission(String command) {
		if(getPermissions().containsKey(command)) {
			return getPermissions().get(command);
		}
		
		return null;
	}
	
	public void initialize() {
		this.permissions = new HashMap<String, PermissionCommand>();
		
		try {
			Statement stmt = this.getConnection().createStatement();
			stmt.executeQuery("SELECT * FROM `permissions_commands`");
			
			ResultSet resultSet = stmt.getResultSet();

			while(resultSet.next()) {
				this.getPermissions().put(resultSet.getString("command"), new PermissionCommand(resultSet.getString("command"), resultSet.getInt("min_prefix")));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
