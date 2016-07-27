package com.gmail.candanatak97.chatserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.WebSocketConnection;

import com.gmail.candanatak97.chatserver.logger.Logger;
import com.gmail.candanatak97.chatserver.network.ChatServerHandler;
import com.gmail.candanatak97.chatserver.user.User;

/**
 * Chat Server
 * 
 * @author C3O
 * @version 0.0.1
 */
public class ChatServer {
	private static Properties properties;
	private static Logger logger;
	private static ChatServerHandler chatServerHandler;
	
	public static final String COMMAND_CHAR = "/";

	public static void main(String[] args) {
		new ChatServer();
	}

	public static Logger getLogger() {
		return logger;
	}

	private static ChatServerHandler getChatServerHandler() {
		return chatServerHandler;
	}

	public static Map<WebSocketConnection, User> getClients() {
		return getChatServerHandler().getClients();
	}

	public static void updateUsers() {
		getChatServerHandler().updateUsers();
	}
	
	public static Connection getDatabaseConnection() {
		return getChatServerHandler().getDatabase().getConnection();
	}

	public ChatServer() {
		logger = new Logger();

		getLogger().write("Server is starting...");

		properties = new Properties();

		try {
			getProperties().load(new FileInputStream(new File("Configuration.ini")));

			int port = Integer.parseInt(getProperties().getProperty("Port", "8080"));
			String host = getProperties().getProperty("Host", "/");

			WebServer webServer = WebServers.createWebServer(port);
			chatServerHandler = new ChatServerHandler();
			webServer.add(host, getChatServerHandler());

			webServer.start();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return;
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}

	public static Properties getProperties() {
		return properties;
	}
}
