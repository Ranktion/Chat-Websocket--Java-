package com.gmail.candanatak97.chatserver.packets.incoming.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.gmail.candanatak97.chatserver.ChatServer;
import com.gmail.candanatak97.chatserver.chat.prefix.PrefixManager;
import com.gmail.candanatak97.chatserver.packets.ClientPacket;
import com.gmail.candanatak97.chatserver.packets.outgoing.Authentification;
import com.gmail.candanatak97.chatserver.packets.outgoing.chat.SendChatMessage;
import com.gmail.candanatak97.chatserver.user.User;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class LoginEvent implements ClientPacket {
	private PrefixManager prefixManager;
	
	public LoginEvent(PrefixManager prefixManager) {
		this.prefixManager = prefixManager;
	}
	
	public PrefixManager getPrefixManager() {
		return this.prefixManager;
	}
	
	public void onHandle(User session, JSONObject data) {
		if(session != null && session.isAuthentificated()) return;

		try {
			String username = data.getString("username");
			String password = data.getString("password");
			
			if(username.length() <= 0 || password.length() <= 0) return;
			
			boolean login = false;
			ResultSet resultSet = null;
			
			try {
				PreparedStatement stmt = ChatServer.getDatabaseConnection().prepareStatement("SELECT `username`, `prefix` FROM `users` WHERE `username` = ? AND `password` = ? LIMIT 1");
				stmt.setString(1, username);
				stmt.setString(2, password);
				resultSet = stmt.executeQuery();

				if(resultSet.last()) {
					login = true;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			if(login && resultSet != null) {
				session.setName(resultSet.getString("username"));
				session.setPrefix(this.getPrefixManager().getPrefixById(resultSet.getInt("prefix")));
				
				session.setAuthentificated(true);
				ChatServer.updateUsers();
				
				session.send(new Authentification(true));
				session.send(new SendChatMessage("Info", "Welcome!", getPrefixManager().getPrefixById(1)));
				return;
			} else {
				session.send(new Authentification(false));
			}
		} catch(JSONException ex) {
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
