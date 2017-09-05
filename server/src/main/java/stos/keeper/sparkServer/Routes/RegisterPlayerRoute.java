package stos.keeper.sparkServer.Routes;

import spark.Request;
import spark.Response;
import spark.Route;
import stos.keeper.database.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.json.JsonTransformer;

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
        Player player = transformer.playerFromJson(request.body());
        int result = playerDAO.addPlayer(player);
        if (result == 0) {
            response.status(org.eclipse.jetty.server.Response.SC_CONFLICT);
            return "Player already exists.";
        } else {
            response.status(org.eclipse.jetty.server.Response.SC_CREATED);

            return player.withId(result);
        }
    }
}
