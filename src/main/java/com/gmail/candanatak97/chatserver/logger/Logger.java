package com.gmail.candanatak97.chatserver.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Chat Server
 * 
 * @author C3O
 */
public class Logger {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public void write(String message) {
		System.out.println(getTimeString() + ANSI_WHITE + message + ANSI_RESET);
	}
	
	public void warn(String message) {
		System.out.println(getTimeString() + ANSI_YELLOW + message + ANSI_RESET);
	}
	
	public void info(String message) {
		System.out.println(getTimeString() + ANSI_BLUE + message + ANSI_RESET);
	}
	
	public void error(String message) {
		System.out.println(getTimeString() + ANSI_RED + message + ANSI_RESET);
	}
	
	public String getTimeString(String string, String format) {
		String dateString = new SimpleDateFormat(format).format(new Date());
		return ANSI_PURPLE + "[" + dateString + "]" + string + ANSI_RESET;
	}
	
	public String getTimeString() {
		return getTimeString(" ", "dd-MM-yyyy HH:mm");
	}
}
