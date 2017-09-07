package stos.keeper.sparkServer.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import stos.keeper.database.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.json.JsonTransformer;

import java.util.Map;
import java.util.Optional;

public class LoginPlayerRoute implements Route {
    public static final String PATH = "/login/:name";

    private final PlayerDAO playerDAO;
    private final JsonTransformer transformer;

    public LoginPlayerRoute(JsonTransformer transformer, PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
        this.transformer = transformer;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ResponseData responseData = process(request.body(), request.params());
        response.status(responseData.getResponseStatus());
        return responseData.getResponseMessage();
    }

    public ResponseData process(String requestbody, Map<String, String> requestParams) throws Exception {
        Optional<Player> playerByName = playerDAO.getPlayerByName(requestParams.get(":name"));
        if (playerByName.isPresent()) {
            return new ResponseData(org.eclipse.jetty.server.Response.SC_OK, transformer.render(playerByName.get()));
        } else {
            return new ResponseData(org.eclipse.jetty.server.Response.SC_NOT_FOUND, "Player not found.");
        }
    }
}
