package me.mostafa.tawbah.dev.constructors;

import java.util.ArrayList;

import com.google.gson.JsonArray;

import lombok.Data;
import me.mostafa.tawbah.Tawbah;

@Data
public class ConnectionSettings {
	// chance=5 for fast(test) & 35 for slow (production)
	private int defaultChance = 35, sendingChance = defaultChance; // 1/90
	private ArrayList<Integer> activeHours = new ArrayList<>();
	private ArrayList<String> subscribedChannels = new ArrayList<>();

	public ConnectionSettings() {
		this.activeHours = Tawbah.global.scheduler.calculater.Load_TimePeriods("0-24");
		subscribedChannels = new ArrayList<>(Tawbah.global.channels.keySet());
	}

	public ConnectionSettings(int sendingChance, ArrayList<Integer> activeHours, ArrayList<String> subscribedChannels) {
		this.sendingChance = sendingChance;
		this.activeHours = activeHours;
		this.subscribedChannels = subscribedChannels;
	}

	public void setSendingChance(int chance) {
		if (chance <= 0)
			this.sendingChance = defaultChance;
		else
			this.sendingChance = chance;
	}

	public void setActiveHours(ArrayList<Integer> hours) {
		if (hours == null)
			this.activeHours = Tawbah.global.scheduler.calculater.Load_TimePeriods("0-24");
		else
			this.activeHours = hours;
	}

	public void setSubscribedChannels(JsonArray array) {
		if (array != null)
			array.forEach(e -> {
				subscribedChannels.add(e.getAsString());
			});
		else
			subscribedChannels = new ArrayList<>(Tawbah.global.channels.keySet());
	}
}
