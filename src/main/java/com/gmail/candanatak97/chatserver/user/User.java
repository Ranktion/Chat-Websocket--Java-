package com.gmail.candanatak97.chatserver.user;

import org.webbitserver.WebSocketConnection;

import com.gmail.candanatak97.chatserver.ChatServer;
import com.gmail.candanatak97.chatserver.packets.ServerPacket;
import com.gmail.candanatak97.chatserver.chat.prefix.Prefix;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class User {
	private WebSocketConnection connection;
	private Prefix prefix;
	private boolean isAuthentificated = false;
	
	private String username = "";
	
	public User(WebSocketConnection connection) {
		this.connection = connection;
		this.isAuthentificated = false;
	}
	
	public WebSocketConnection getConnection() {
		return this.connection;
	}
	
	public Prefix getPrefix() {
		return this.prefix;
	}
	
	public void setPrefix(Prefix prefix) {
		this.prefix = prefix;
	}
	
	public String getName() {
		return this.username;
	}
	
	public void setName(String username) {
		this.username = username;
	}
	
	public void setAuthentificated(boolean isAuthentificated) {
		this.isAuthentificated = isAuthentificated;
	}
	
	public boolean isAuthentificated() {
		return this.isAuthentificated;
	}
	
	public void send(ServerPacket packet) {
		if(getConnection() != null) {
			if(packet.getData() != null) {
				String message = packet.getData().toString();
				getConnection().send(message);
				
				ChatServer.getLogger().write("Send message: " + message);
			}
		}
	}
}
