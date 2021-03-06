package hu.mep.utils.deserializers;

import hu.mep.datamodells.Place;
import hu.mep.datamodells.PlaceList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PlaceListDeserializer implements JsonDeserializer<PlaceList> {

	@Override
	public PlaceList deserialize(JsonElement element, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		List<Place> places = new ArrayList<Place>();
		
		if(element.isJsonObject()) {
			JsonObject jsonObject = element.getAsJsonObject();
			
			for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				Place place = context.deserialize(entry.getValue(), Place.class);
				places.add(place);
			}
		}
		return new PlaceList(places);
	}

}
