package me.mostafa.tawbah;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import me.mostafa.tawbah.dev.Global;
import me.mostafa.tawbah.dev.constructors.Server;

public class Tawbah {

	public static Global global;
	public static Server server;
	public static String ip = "localhost";
	public static int port = 3000;

	public static void main(String[] args) {
		port = getInt(args.length > 0 ? args[0] : "3000", 3000);
		global = new Global();
	}

	private static int getInt(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return def;
		}
	}

	public static void print(String msg) {
		System.out.println(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": " + msg);
	}
}
