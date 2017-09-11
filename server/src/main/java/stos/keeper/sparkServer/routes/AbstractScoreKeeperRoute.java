package stos.keeper.sparkServer.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import stos.keeper.database.PlayerDAO;
import stos.keeper.sparkServer.json.JsonTransformer;

import java.util.Map;

public abstract class AbstractScoreKeeperRoute implements Route {

    protected final JsonTransformer transformer;
    protected final PlayerDAO playerDAO;

    public AbstractScoreKeeperRoute(PlayerDAO playerDAO, JsonTransformer transformer) {
        this.playerDAO = playerDAO;
        this.transformer = transformer;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ResponseData responseData = process(request.body(), request.params());
        response.status(responseData.getResponseStatus());
        return responseData.getResponseMessage();
    }

    protected abstract ResponseData process(String body, Map<String, String> params) throws Exception;
}
