package stos.keeper.sparkServer.json;

import com.google.gson.*;
import spark.ResponseTransformer;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PlannedMatchesResponseTransformer implements ResponseTransformer {

    private Gson gson;

    public PlannedMatchesResponseTransformer() {
        gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZoneDateTimeSerializationAdapter())
                .create();
    }

    @Override
    public String render(Object model) throws Exception {
        return gson.toJson(model);
    }

    private class ZoneDateTimeSerializationAdapter implements JsonSerializer<ZonedDateTime> {
        @Override
        public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy H:mm")
                    .withZone(ZoneId.systemDefault());
            return new JsonPrimitive(src.format(dateTimeFormatter));
        }
    }
}
