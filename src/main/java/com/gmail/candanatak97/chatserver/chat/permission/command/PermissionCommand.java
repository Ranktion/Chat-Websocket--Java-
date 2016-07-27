package com.gmail.candanatak97.chatserver.chat.permission.command;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class PermissionCommand {
	private String command;
	private int minPrefix;
	
	public PermissionCommand(String command, int minPrefix) {
		this.command = command;
		this.minPrefix = minPrefix;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public int getMinPrefix() {
		return this.minPrefix;
	}
	
	public void setMinPrefix(int minPrefix) {
		this.minPrefix = minPrefix;
	}
}
