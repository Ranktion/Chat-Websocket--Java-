package com.gmail.candanatak97.chatserver.chat.commands;

import java.util.ArrayList;

import com.gmail.candanatak97.chatserver.ChatServer;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class CommandParser {
	private String command;
	
	public CommandParser(String command) {
		this.command = command;
		
		if(command.substring(0, 1).equalsIgnoreCase(ChatServer.COMMAND_CHAR)) {
			this.command = command.substring(1, command.length());
		}
	}
	
	public String[] split() {
		return this.command.split(" ");
	}
	
	public String getCommand() {
		if(split().length > 0) {
			return split()[0];
		}
		
		return "";
	}
	
	public ArrayList<String> getParameters() {
		ArrayList<String> params = new ArrayList<String>();
		
		int index = 0;
		for(String param : split()) {
			index++;
			
			if(index > 1) {
				params.add(param);
			}
		}
		
		return params;
	}
}
