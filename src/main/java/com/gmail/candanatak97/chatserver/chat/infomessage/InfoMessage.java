package com.gmail.candanatak97.chatserver.chat.infomessage;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class InfoMessage {
	private String senderName;
	private String message;
	private boolean enabled;
	
	public InfoMessage(String senderName, String message, boolean enabled) {
		this.senderName = senderName;
		this.message = message;
		this.enabled = enabled;
	}
	
	public String getSenderName() {
		return this.senderName;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
}
