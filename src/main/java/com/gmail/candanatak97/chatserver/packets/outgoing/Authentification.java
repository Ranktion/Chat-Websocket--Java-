package com.gmail.candanatak97.chatserver.packets.outgoing;

import org.json.JSONObject;

import com.gmail.candanatak97.chatserver.packets.ServerPacket;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class Authentification implements ServerPacket {
	private JSONObject data;
	
	public Authentification(boolean authentificated) {
		this.data = new JSONObject();
		
		this.data.put("header", 3);
		
		JSONObject arr = new JSONObject();
		
		arr.put("authentificated", authentificated);
		
		this.data.put("data", arr);
	}

	public JSONObject getData() {
		return this.data;
	}
	
}
