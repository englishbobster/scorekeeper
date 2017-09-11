package stos.keeper.sparkServer.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import spark.ResponseTransformer;
import stos.keeper.model.planned_matches.MatchType;
import stos.keeper.model.planned_matches.Score;
import stos.keeper.model.player.Player;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

public class JsonTransformer implements ResponseTransformer {

    private Gson gson;

    public JsonTransformer() {
        gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZoneDateTimeSerializationAdapter())
                .registerTypeAdapter(ZonedDateTime.class, new ZoneDateTimeDeserializerAdapter())
                .registerTypeAdapter(MatchType.class, new MatchTypeAdapter())
                .create();
    }

    @Override
    public String render(Object model) throws Exception {
        return gson.toJson(model);
    }

    public Score scoreFromJson(String response) {
        return gson.fromJson(response, Score.class);
    }

    public Player playerFromJson(String response) {
        return gson.fromJson(response, Player.class);
    }

    private class ZoneDateTimeSerializationAdapter implements JsonSerializer<ZonedDateTime> {
        @Override
        public JsonElement serialize(ZonedDateTime time, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(time.toString());
        }
    }

    private class MatchTypeAdapter implements JsonSerializer<MatchType> {
        @Override
        public JsonElement serialize(MatchType matchType, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(matchType.toString());
        }
    }

    private class ZoneDateTimeDeserializerAdapter implements JsonDeserializer<ZonedDateTime> {
        @Override
        public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return ZonedDateTime.parse(json.getAsString());
        }
    }
}
