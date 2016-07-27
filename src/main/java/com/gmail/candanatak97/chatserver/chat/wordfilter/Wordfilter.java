package com.gmail.candanatak97.chatserver.chat.wordfilter;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class Wordfilter {
	private String badword;
	private String replacement;
	private boolean enabled;
	
	public Wordfilter(String badword, String replacement, boolean enabled) {
		this.badword = badword;
		this.replacement = replacement;
		this.enabled = enabled;
	}
	
	public String getWord() {
		return this.badword;
	}
	
	public String getReplacement() {
		return this.replacement;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
}
