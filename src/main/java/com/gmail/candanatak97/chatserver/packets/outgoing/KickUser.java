package com.gmail.candanatak97.chatserver.packets.outgoing;

import org.json.JSONObject;

import com.gmail.candanatak97.chatserver.packets.ServerPacket;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class KickUser implements ServerPacket {
	private JSONObject data;
	
	public KickUser(boolean status) {
		this.data = new JSONObject();
		
		this.data.put("header", 4);
		
		JSONObject arr = new JSONObject();
		
		arr.put("status", status);
		
		this.data.put("data", arr);
	}

	public JSONObject getData() {
		return this.data;
	}
	
}
