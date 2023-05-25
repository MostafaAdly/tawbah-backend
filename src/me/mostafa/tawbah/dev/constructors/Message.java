package me.mostafa.tawbah.dev.constructors;

import lombok.Data;
import me.mostafa.tawbah.dev.constructors.Azkar.Verse;

@Data
public class Message {

	private String type, content, description;

	public Message(Verse verse) {
		this(verse.getCategory(), verse.getContent(), verse.getDescription());
	}

	public Message(String type, String content, String description) {
		this.type = type;
		this.content = content;
		this.description = description;
	}
}
