package edu.depaul.reservations.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GsonLocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    @Override
    public JsonElement serialize(LocalTime time, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(getNewDateTimeFormat().format(time));
    }

    @Override
    public LocalTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        return LocalTime.parse(jsonElement.getAsString());
    }

    private DateTimeFormatter getNewDateTimeFormat() {
        return DateTimeFormatter.ofPattern("HH:mm:ss");
    }
}