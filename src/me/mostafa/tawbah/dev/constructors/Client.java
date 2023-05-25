package me.mostafa.tawbah.dev.constructors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Data;
import me.mostafa.tawbah.Tawbah;

@Data
public class Client {

	private String ID = "#" + UUID.randomUUID().toString().split("-")[0];
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;
	private ConnectionSettings settings = new ConnectionSettings();

	public Client() {
		this(Tawbah.ip, Tawbah.port);
	}

	public Client(String ip, int port) {
		try {
			client = new Socket(ip, port);
			connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect() throws IOException {
		create_IN_OUT();
		Tawbah.global.clients.add(this);
		Tawbah.print("=-> Current connected clients [" + Tawbah.global.clients.size() + "]");
		clientDisconnectEvent();

	}

	public Client(Socket socket) throws IOException {
		client = socket;
		connect();
	}

	public void create_IN_OUT() throws IOException {
		out = new PrintWriter(client.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}

	public void sendMessage(String... msgs) {
		for (String msg : msgs)
			out.println(msg);
		out.flush();
	}

	public void stop() throws IOException {
		out.close();
		in.close();
		client.close();
		Tawbah.global.handler.disconnectClient(ID, false);
	}

	public void onDisconnect() throws IOException {
		stop();
	}

	public void clientDisconnectEvent() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (!client.isClosed())
						if (in.read() == -1)
							onDisconnect();
				} catch (IOException e) {
					try {
						onDisconnect();
					} catch (Exception e2) {
					}
				}
			}
		}).start();
	}

	public void startListening() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					sendMessage(Tawbah.global.gson.toJson(getSettings()));
					String str;
					while (!client.isClosed() && ((str = in.readLine()) != null)) {
						Tawbah.print("['" + ID + "'] " + str);
						handleConnectionSettings(str);
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void handleConnectionSettings(String str) {
		try {
			JsonObject obj = ((JsonObject) new JsonParser().parse(str));
			if (obj.get("channels") != null && obj.get("channels").getAsBoolean())
				sendMessage(Tawbah.global.gson.toJson(Tawbah.global.channels.keySet()));
			if (obj.get("connection-settings") == null)
				return;
			JsonObject s = (JsonObject) obj.get("connection-settings");
			settings.setActiveHours(Tawbah.global.scheduler.calculater
					.Load_TimePeriods(s.get("active-hours") == null ? null : s.get("active-hours").getAsString()));
			settings.setSendingChance(s.get("sending-chance") == null ? null : s.get("sending-chance").getAsInt());
			settings.setSubscribedChannels(
					s.get("subscribed-channels") == null ? null : (JsonArray) s.get("subscribed-channels"));
		} catch (Exception e) {
		}
	}
}
