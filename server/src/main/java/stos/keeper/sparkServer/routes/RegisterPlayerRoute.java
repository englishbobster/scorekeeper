package stos.keeper.sparkServer.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import stos.keeper.database.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.json.JsonTransformer;

import java.util.Collections;
import java.util.Map;

public class RegisterPlayerRoute implements Route {
    public static final String PATH = "/register";

    private final JsonTransformer transformer;
    private final PlayerDAO playerDAO;

    public RegisterPlayerRoute(JsonTransformer transformer, PlayerDAO playerDAO) {
        this.transformer = transformer;
        this.playerDAO = playerDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ResponseData responseData = process(request.body(), Collections.EMPTY_MAP);
        response.status(responseData.getResponseStatus());
        return responseData.getResponseMessage();
    }

    public ResponseData process(String requestBody, Map<String, String> requestParams) throws Exception {
        int status = org.eclipse.jetty.server.Response.SC_CREATED;
        String message;
        Player player = transformer.playerFromJson(requestBody);
        int result = playerDAO.addPlayer(player);
        if (result == 0) {
            status = org.eclipse.jetty.server.Response.SC_CONFLICT;
            message = "Player with that name already exists.";
        } else {
            player = player.withId(result);
            message = transformer.render(player);
        }
        return new ResponseData(status, message);
    }

}
