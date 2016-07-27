package com.gmail.candanatak97.chatserver.chat.commands.mod;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.webbitserver.WebSocketConnection;

import com.gmail.candanatak97.chatserver.ChatServer;
import com.gmail.candanatak97.chatserver.chat.commands.ICommand;
import com.gmail.candanatak97.chatserver.chat.permission.command.PermissionCommand;
import com.gmail.candanatak97.chatserver.chat.permission.command.PermissionCommandManager;
import com.gmail.candanatak97.chatserver.chat.prefix.PrefixManager;
import com.gmail.candanatak97.chatserver.packets.outgoing.KickUser;
import com.gmail.candanatak97.chatserver.packets.outgoing.chat.SendChatMessage;
import com.gmail.candanatak97.chatserver.user.User;

public class KickCommand implements ICommand {
	private String command;
	
	private PrefixManager prefixManager;
	private PermissionCommandManager permissionCommandManager;
	
	public KickCommand(String command, PrefixManager prefixManager, PermissionCommandManager permissionCommandManager) {
		this.command = command;
		this.prefixManager = prefixManager;
		this.permissionCommandManager = permissionCommandManager;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	private PrefixManager getPrefixManager() {
		return this.prefixManager;
	}
	
	private PermissionCommandManager getPermissionCommandManager() {
		return this.permissionCommandManager;
	}
	
	public void handle(User session, ArrayList<String> params) {
		PermissionCommand permissionCommand = getPermissionCommandManager().getPermission(getCommand());
		
		if(permissionCommand == null || session.getPrefix().getId() < permissionCommand.getMinPrefix()) {
			session.send(new SendChatMessage("Permissions denied", "You don't have permissions for this command.", getPrefixManager().getPrefixById(1)));
			return;
		}
		
		if(params.size() <= 0) {
			session.send(new SendChatMessage("Command usage", "Use " + ChatServer.COMMAND_CHAR + getCommand() + " <Username>", getPrefixManager().getPrefixById(1)));
			return;
		}
		
		User user = null;
		WebSocketConnection connection = null;
		
		for(Entry<WebSocketConnection, User> entry : ChatServer.getClients().entrySet()) {
			if(entry.getValue() != null && entry.getValue().getName().equalsIgnoreCase(params.get(0))) {
				user = entry.getValue();
				connection = entry.getKey();
			}
		}
		
		if(user != null && user.isAuthentificated()) {
			user.setAuthentificated(false);
			user.send(new SendChatMessage("Kick", "You was kicked by " + session.getName() + ".", getPrefixManager().getPrefixById(1)));
			user.send(new KickUser(false));
			
			session.send(new SendChatMessage("Kick", "You has kicked " + user.getName() + ".", getPrefixManager().getPrefixById(1)));
			
			if(connection != null) {
				connection.close();
			}
		} else {
			session.send(new SendChatMessage("Error", "User '" + params.get(0) + "' is not online.", getPrefixManager().getPrefixById(1)));
		}
	}
	
}
