package com.gmail.candanatak97.chatserver.chat.commands;

import java.util.HashMap;
import java.util.Map;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class CommandsManager {
	Map<String, ICommand> commands;
	
	public CommandsManager() {
		this.commands = new HashMap<String, ICommand>();
	}
	
	public CommandsManager(Map<String, ICommand> commands) {
		this.commands = commands;
	}
	
	public ICommand getCommand(String command) {
		if(this.getCommands().containsKey(command)) {
			return this.getCommands().get(command);
		}
		
		return null;
	}
	
	public Map<String, ICommand> getCommands() {
		return this.commands;
	}
}
