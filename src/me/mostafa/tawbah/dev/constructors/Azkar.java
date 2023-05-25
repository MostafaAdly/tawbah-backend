package me.mostafa.tawbah.dev.constructors;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.Data;

@Data
public class Azkar {
	private String type, chance;

	private ArrayList<Integer> activeHours = new ArrayList<>();
	private ArrayList<Verse> verses = new ArrayList<>();

	public Azkar(String type, String chance, Verse... zkr) {
		this.type = type;
		this.chance = chance;
		verses.addAll(Arrays.asList(zkr));
	}

	@Data
	public class Verse {
		private String category = type, description, content;

		public Verse(String content, String description) {
			this.content = content;
			this.description = description;
		}
	}

	public Verse verse(String content, String description) {
		return new Verse(content, description);
	}

	public boolean hasChance(int ran) {
		return (chance != null && chance.contains("-") && isInt(chance.split("-")[0]) && isInt(chance.split("-")[1]))
				&& (ran > getInt(chance.split("-")[0]) && ran <= getInt(chance.split("-")[1]));
	}

	public Azkar addZkr(Verse msg) {
		if (!verses.contains(msg))
			verses.add(msg);
		return this;
	}

	public Azkar addPeriod(ArrayList<Integer> period) {
		for (int i : period)
			if (!activeHours.contains(i))
				activeHours.add(i);
		return this;
	}

	public void removeZkr(Verse msg) {
		verses.remove(msg);
	}

	public void removeZkr(String content) {
		for (Verse v : new ArrayList<>(verses))
			if (v.getContent() != null && (v.getContent().equals(content)
					|| (v.getDescription() != null && v.getDescription().equals(content))))
				verses.remove(v);
	}

	private int getInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return -1;
		}
	}

	private boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
