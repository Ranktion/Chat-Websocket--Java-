package com.gmail.candanatak97.chatserver.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import com.gmail.candanatak97.chatserver.ChatServer;
import com.gmail.candanatak97.chatserver.chat.commands.CommandsManager;
import com.gmail.candanatak97.chatserver.chat.commands.ICommand;
import com.gmail.candanatak97.chatserver.chat.commands.mod.KickCommand;
import com.gmail.candanatak97.chatserver.chat.infomessage.InfoMessage;
import com.gmail.candanatak97.chatserver.chat.infomessage.InfoMessageManager;
import com.gmail.candanatak97.chatserver.chat.permission.command.PermissionCommandManager;
import com.gmail.candanatak97.chatserver.chat.prefix.PrefixManager;
import com.gmail.candanatak97.chatserver.chat.wordfilter.WordfilterManager;
import com.gmail.candanatak97.chatserver.database.Database;
import com.gmail.candanatak97.chatserver.packets.ClientPacket;
import com.gmail.candanatak97.chatserver.packets.PacketsManager;
import com.gmail.candanatak97.chatserver.packets.incoming.chat.ChatEvent;
import com.gmail.candanatak97.chatserver.packets.incoming.user.LoginEvent;
import com.gmail.candanatak97.chatserver.packets.outgoing.chat.SendChatMessage;
import com.gmail.candanatak97.chatserver.packets.outgoing.chat.UpdateUsersCount;
import com.gmail.candanatak97.chatserver.user.User;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class ChatServerHandler extends BaseWebSocketHandler {
	private PacketsManager packetsManager;
	private Map<WebSocketConnection, User> clients;
	
	private int infoMessageTimer = -1;
	
	private Timer autoMessageTimer;
	private TimerTask autoMessageTask;
	
	private Database database;
	
	private WordfilterManager wordfilterManager;
	private InfoMessageManager infoMessageManager;
	private PrefixManager prefixManager;
	private CommandsManager commandsManager;
	private PermissionCommandManager permissionCommandManager;
	
	public ChatServerHandler() {
		this.clients = new HashMap<WebSocketConnection, User>();

		infoMessageTimer = Integer.parseInt(ChatServer.getProperties().getProperty("InfoMessageTimer", "-1"));
		
		String mHostname = ChatServer.getProperties().getProperty("MySQL.Hostname", "127.0.0.1");
		String mUsername = ChatServer.getProperties().getProperty("MySQL.Username");
		String mPassword = ChatServer.getProperties().getProperty("MySQL.Password");
		String mDatabase = ChatServer.getProperties().getProperty("MySQL.Database");
		int mPort = Integer.parseInt(ChatServer.getProperties().getProperty("MySQL.Port", "3306"));
		
		this.database = new Database(mHostname, mUsername, mPassword, mDatabase, mPort);
		
		registerWordfilter();
		registerInfoMessages();
		registerPrefixes();
		registerPermissionCommands();
		
		registerCommands();
		registerPackets();
		
		if(infoMessageTimer > 0) {
			this.autoMessageTask = new TimerTask() {
				@Override
				public void run() {
					autoMessage();
				}
			};
			
			autoMessageTimer = new Timer();
			autoMessageTimer.schedule(autoMessageTask, 1000, 1000 * infoMessageTimer);
		}
		
		ChatServer.getLogger().write("Server is started!");
	}
	
	private void autoMessage() {
		ArrayList<InfoMessage> messages = getInfoMessageManager().getEnabledMessages();
		
		Random random = new Random();
		int randomIndex = random.nextInt(messages.size());
		
		for(User user : this.getClients().values()) {
			if(user.isAuthentificated()) {
				user.send(new SendChatMessage(messages.get(randomIndex).getSenderName(), messages.get(randomIndex).getMessage(), getPrefixManager().getPrefixById(1)));
			}
		}
	}
	
	public Map<WebSocketConnection, User> getClients() {
		return this.clients;
	}
	
	public Database getDatabase() {
		return this.database;
	}
	
	public PacketsManager getPacketsManager() {
		return this.packetsManager;
	}
	
	public InfoMessageManager getInfoMessageManager() {
		return this.infoMessageManager;
	}

	public PrefixManager getPrefixManager() {
		return this.prefixManager;
	}

	public CommandsManager getCommandsManager() {
		return this.commandsManager;
	}
	
	public WordfilterManager getWordfilterManager() {
		return this.wordfilterManager;
	}
	
	public PermissionCommandManager getPermissionCommandManager() {
		return this.permissionCommandManager;
	}
	
	public void registerPermissionCommands() {
		this.permissionCommandManager = new PermissionCommandManager(getDatabase());
		this.getPermissionCommandManager().initialize();
	}
	
	public void registerCommands() {
		Map<String, ICommand> commands = new HashMap<String, ICommand>();
		
		commands.put("kick", new KickCommand("kick", getPrefixManager(), getPermissionCommandManager()));
		
		this.commandsManager = new CommandsManager(commands);
	}
	
	public void registerInfoMessages() {
		this.infoMessageManager = new InfoMessageManager(this.getDatabase());
		this.getInfoMessageManager().initialize();
	}
	
	public void registerPrefixes() {
		this.prefixManager = new PrefixManager(this.getDatabase());
		this.getPrefixManager().initialize();
	}
	
	public void registerWordfilter() {
		this.wordfilterManager = new WordfilterManager(this.getDatabase());
		this.getWordfilterManager().initialize();
	}
	
	public void registerPackets() {
		Map<Integer, ClientPacket> packets = new HashMap<Integer, ClientPacket>();
		
		packets.put(1, new ChatEvent(getCommandsManager(), getWordfilterManager()));
		packets.put(2, new LoginEvent(getPrefixManager()));
		
		packetsManager = new PacketsManager(packets);
	}
	
	public void updateUsers() {
		int count = 0;
		
		for(User user : this.getClients().values()) {
			if(user.isAuthentificated()) {
				count++;
			}
		}

		for(User user : this.getClients().values()) {
			user.send(new UpdateUsersCount(count));
		}
	}

	@Override
	public void onOpen(WebSocketConnection connection) {
		ChatServer.getLogger().write("Connection opened.");
		
		if (this.getClients().containsKey(connection)) {
			this.getClients().get(connection).getConnection().close();
			this.getClients().remove(connection);
		}
		
		User user = new User(connection);
		this.getClients().put(connection, user);
	}

	@Override
	public void onClose(WebSocketConnection connection) {
		ChatServer.getLogger().write("Connection closed.");
		
		if (this.getClients().containsKey(connection)) {
			this.getClients().remove(connection);
		}
		
		this.updateUsers();
	}

	@Override
	public void onMessage(WebSocketConnection connection, String message) {
		if (this.getClients().containsKey(connection)) {
			try {
				JSONObject jsonObject = new JSONObject(message);
				
				int header = jsonObject.getInt("header");
				JSONObject data = jsonObject.getJSONObject("data");
				
				this.getPacketsManager().handlePacket(header, this.getClients().get(connection), data);
			} catch(JSONException ex) {
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
