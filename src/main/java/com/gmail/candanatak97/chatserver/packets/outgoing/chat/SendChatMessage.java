package com.gmail.candanatak97.chatserver.packets.outgoing.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.gmail.candanatak97.chatserver.packets.ServerPacket;
import com.gmail.candanatak97.chatserver.chat.prefix.Prefix;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class SendChatMessage implements ServerPacket {
	private JSONObject data;
	
	public SendChatMessage(String username, String message, Prefix prefix, String timeFormat) {
		this.handle(username, message, prefix, timeFormat);
	}
	
	public SendChatMessage(String username, String message, Prefix prefix) {
		String time = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
		
		this.handle(username, message, prefix, time);
	}

	private void handle(String username, String message, Prefix prefix, String timeFormat) {
		this.data = new JSONObject();
		
		this.data.put("header", 1);
		
		JSONObject arr = new JSONObject();
		
		arr.put("username", username);
		arr.put("message", message);
		arr.put("time", timeFormat);
		
		if(prefix != null) {
			if(prefix.getPrefix() != null) {
				arr.put("prefix", prefix.getPrefix());
			}
			
			if(prefix.getName() != null) {
				arr.put("prefixName", prefix.getName());
			}
			
			if(prefix.getColor() != null) {
				arr.put("prefixColor", prefix.getColor());
			}
		}
		
		this.data.put("data", arr);	
	}
	
	public JSONObject getData() {
		return this.data;
	}
	
}
