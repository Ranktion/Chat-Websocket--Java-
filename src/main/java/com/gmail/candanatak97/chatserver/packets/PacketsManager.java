package com.gmail.candanatak97.chatserver.packets;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.gmail.candanatak97.chatserver.ChatServer;
import com.gmail.candanatak97.chatserver.user.User;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class PacketsManager {
	private Map<Integer, ClientPacket> packets;
	
	public PacketsManager() {
		this.packets = new HashMap<Integer, ClientPacket>();
	}
	
	public PacketsManager(Map<Integer, ClientPacket> packets) {
		this.packets = packets;
	}
	
	private Map<Integer, ClientPacket> getPackets() {
		return this.packets;
	}
	
	public void handlePacket(int header, User session, JSONObject data) {
		if(session != null && session.getConnection() != null) {
			if(this.getPackets().containsKey(header)) {
				ChatServer.getLogger().write("Handled packet: " + header);
				
				this.getPackets().get(header).onHandle(session, data);
			} else {
				ChatServer.getLogger().write("Packet not handled: " + header);
			}
		}
	}
}
