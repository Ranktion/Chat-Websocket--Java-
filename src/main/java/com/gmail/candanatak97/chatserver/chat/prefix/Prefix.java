package com.gmail.candanatak97.chatserver.chat.prefix;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class Prefix {

	private int id;
	private String name;
	private String prefix;
	private String color;
	
	public Prefix(int id, String name, String prefix, String color) {
		this.id = id;
		this.name = name;
		this.prefix = prefix;
		this.color = color;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setColor(String color) {
		this.color = color;
	}

}
