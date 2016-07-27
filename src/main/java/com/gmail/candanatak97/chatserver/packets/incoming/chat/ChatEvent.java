package com.gmail.candanatak97.chatserver.packets.incoming.chat;

import org.json.JSONException;
import org.json.JSONObject;

import com.gmail.candanatak97.chatserver.ChatServer;
import com.gmail.candanatak97.chatserver.chat.wordfilter.Wordfilter;
import com.gmail.candanatak97.chatserver.chat.wordfilter.WordfilterManager;
import com.gmail.candanatak97.chatserver.packets.ClientPacket;
import com.gmail.candanatak97.chatserver.packets.outgoing.chat.SendChatMessage;
import com.gmail.candanatak97.chatserver.chat.commands.CommandParser;
import com.gmail.candanatak97.chatserver.chat.commands.CommandsManager;
import com.gmail.candanatak97.chatserver.chat.commands.ICommand;
import com.gmail.candanatak97.chatserver.chat.prefix.Prefix;
import com.gmail.candanatak97.chatserver.user.User;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class ChatEvent implements ClientPacket {
	private WordfilterManager wordfilterManager;
	private CommandsManager commandsManager;
	
	public ChatEvent(CommandsManager commandsManager, WordfilterManager wordfilterManager) {
		this.wordfilterManager = wordfilterManager;
		this.commandsManager = commandsManager;
	}
	
	public CommandsManager getCcommandsManager() {
		return this.commandsManager;
	}
	
	public WordfilterManager getWordfilterManager() {
		return this.wordfilterManager;
	}
	
	public void onHandle(User session, JSONObject data) {
		if(session == null || !session.isAuthentificated()) return;
		
		try {
			String message = data.getString("message");
			
			if(message.substring(0, 1).equalsIgnoreCase(ChatServer.COMMAND_CHAR)) {
				if(message.length() > 1) {
					CommandParser commandParser = new CommandParser(message);
					ICommand command = this.getCcommandsManager().getCommand(commandParser.getCommand());
					if(command != null) {
						command.handle(session, commandParser.getParameters());
					}
				}
			} else {
				for(Wordfilter wordfilter : this.getWordfilterManager().getBadwords()) {
					if(wordfilter.isEnabled()) {
						message = message.replaceAll("(?i)" + wordfilter.getWord(), wordfilter.getReplacement());
					}
				}
				
				Prefix prefix = session.getPrefix();
				
				if(message.length() > 0) {
					for(User user : ChatServer.getClients().values()) {
						if(user.isAuthentificated()) {
							user.send(new SendChatMessage(session.getName(), message, prefix));
						}
					}
				}
			}
		} catch(JSONException ex) {
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
