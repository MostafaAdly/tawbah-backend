package me.mostafa.tawbah.dev;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import me.mostafa.tawbah.Tawbah;
import me.mostafa.tawbah.dev.constructors.*;

public class Global {

	// =========================================================== [ CONSTRUCTORS ]
	public Gson gson = new Gson();
	public Scheduler scheduler = new Scheduler();
	public PublicHandler handler = new PublicHandler();
	public MessageHandler messageHandler = new MessageHandler();
	// =========================================================== [ MAPS ]
	public HashMap<String, Channel> channels = new HashMap<>();
	public HashMap<String, Azkar> azkar = new HashMap<>();
	public ArrayList<Client> clients = new ArrayList<>();
	// =========================================================== [ -- GLOBAL -- ]

	public Global() {
		new Loader();
		handler.createServer();
	}

	public class Loader {

		public Loader() {
			Load_DataFromJSON();
		}

		public void Load_DataFromJSON() {
			try {
				JsonParser jsonParser = new JsonParser();
				Object obj = jsonParser.parse(new FileReader("Azkar.json"));
				((JsonArray) obj).forEach(zkr -> {
					((JsonArray) zkr).forEach(emp -> {
						String type = getString(emp, "category");
						Azkar zkr_ = new Azkar(type, getString(emp, "chance"));
						channels.computeIfAbsent(type, k -> new Channel(type));
						azkar.computeIfAbsent(type, k -> zkr_)
								.addZkr(zkr_.verse(getString(emp, "content"), getString(emp, "description")))
								.addPeriod(scheduler.getCalculater().Load_TimePeriods(getString(emp, "period")));
					});
				});
				int total = 0;
				for (Azkar zkr : azkar.values())
					total += zkr.getVerses().size();
				Tawbah.print("Loaded [" + azkar.size() + ", " + total + "] types of [Azkar, Total] respectivily.");
				Tawbah.print("Loaded [" + channels.size() + "] channels.");
			} catch (Exception e) {
				e.printStackTrace();
				Tawbah.print("--> Error while loading azkar.");
			}
		}

		private String getString(JsonElement ob, String parameter) {
			return ob.getAsJsonObject().get(parameter).getAsString();
		}
	}

	public class PublicHandler {

		public void disconnectClient(String clientId, boolean close) throws IOException {
			for (Client client : new ArrayList<>(clients))
				if (client.getID().equals(clientId)) {
					if (close)
						client.stop();
					clients.remove(client);
				}
			Tawbah.print("[" + clientId + "] was disconnected, There are (" + clients.size()
					+ ") connected client(s) at the moment.");
		}

		public void createServer() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Tawbah.server = new Server(Tawbah.port);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

	}

	public class MessageHandler {
		public void sendMessage(Client client, String... msgs) {
			client.sendMessage(msgs);
		}

	}

}
