package me.mostafa.tawbah.dev.constructors;

import java.util.Random;

import lombok.Data;
import me.mostafa.tawbah.Tawbah;

@Data
public class Channel {

	private String name;
	private Random random = new Random();

	public Channel(String name) {
		this.name = name;
	}

	public void sendMessage(Message msg) {
		for (Client client : Tawbah.global.clients)
			if (client.getSettings().getSubscribedChannels().contains(name)
					&& Tawbah.global.scheduler.calculater.canSendRightNow(client.getSettings().getActiveHours())
					&& random.nextInt(
							client.getSettings().getSendingChance()) == client.getSettings().getSendingChance() - 1)
				client.sendMessage(Tawbah.global.gson.toJson(msg));
	}
}
