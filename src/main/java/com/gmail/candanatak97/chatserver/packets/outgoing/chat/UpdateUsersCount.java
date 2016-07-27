package com.gmail.candanatak97.chatserver.packets.outgoing.chat;

import org.json.JSONObject;

import com.gmail.candanatak97.chatserver.packets.ServerPacket;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class UpdateUsersCount implements ServerPacket {
	private JSONObject data;
	
	public UpdateUsersCount(int count) {
		this.data = new JSONObject();
		
		this.data.put("header", 2);
		
		JSONObject arr = new JSONObject();
		
		arr.put("count", count);
		
		this.data.put("data", arr);
	}

	public JSONObject getData() {
		return this.data;
	}
	
}
