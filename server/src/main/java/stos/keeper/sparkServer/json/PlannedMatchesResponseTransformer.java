package stos.keeper.sparkServer.json;

import com.google.gson.*;
import spark.ResponseTransformer;
import stos.keeper.model.MatchType;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PlannedMatchesResponseTransformer implements ResponseTransformer {

    private Gson gson;

    public PlannedMatchesResponseTransformer() {
        gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZoneDateTimeSerializationAdapter())
                .registerTypeAdapter(MatchType.class, new MatchTypeAdapter())
                .create();
    }

    @Override
    public String render(Object model) throws Exception {
        return gson.toJson(model);
    }

    private class ZoneDateTimeSerializationAdapter implements JsonSerializer<ZonedDateTime> {
        @Override
        public JsonElement serialize(ZonedDateTime time, Type typeOfSrc, JsonSerializationContext context) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy H:mm")
                    .withZone(ZoneId.systemDefault());
            return new JsonPrimitive(time.format(dateTimeFormatter));
        }
    }

    private class MatchTypeAdapter implements JsonSerializer<MatchType> {
        @Override
        public JsonElement serialize(MatchType matchType, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(matchType.toString());
        }
    }

}
