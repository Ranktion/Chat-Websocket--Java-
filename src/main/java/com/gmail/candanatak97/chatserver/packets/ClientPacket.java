package com.gmail.candanatak97.chatserver.packets;

import org.json.JSONObject;

import com.gmail.candanatak97.chatserver.user.User;

/**
 * Chat Server
 * 
 * @author C3O
 */
public interface ClientPacket {
	public void onHandle(User connection, JSONObject data);
}
