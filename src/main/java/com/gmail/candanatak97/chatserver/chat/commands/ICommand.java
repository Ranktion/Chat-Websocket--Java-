package com.gmail.candanatak97.chatserver.chat.commands;

import java.util.ArrayList;

import com.gmail.candanatak97.chatserver.user.User;

/**
 * Chat Server
 * 
 * @author C3O
 */
public interface ICommand {
	public String getCommand();
	public void handle(User session, ArrayList<String> params);
}
