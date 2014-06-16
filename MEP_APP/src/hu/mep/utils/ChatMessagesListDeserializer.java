package hu.mep.utils;

import hu.mep.datamodells.ChatMessage;
import hu.mep.datamodells.ChatMessagesList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ChatMessagesListDeserializer implements
		JsonDeserializer<ChatMessagesList> {

	@Override
	public ChatMessagesList deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = element.getAsJsonObject();
		List<ChatMessage> messages = new ArrayList<ChatMessage>();
		for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			ChatMessage message = context.deserialize(entry.getValue(),
					ChatMessage.class);
			messages.add(message);
		}
		return new ChatMessagesList(messages);

	}

}