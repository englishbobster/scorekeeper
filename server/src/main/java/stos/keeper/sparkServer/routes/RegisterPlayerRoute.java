package stos.keeper.sparkServer.routes;

import stos.keeper.database.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.json.JsonTransformer;

import java.util.Map;

public class RegisterPlayerRoute extends AbstractScoreKeeperRoute {
    public static final String PATH = "/register";

    public RegisterPlayerRoute(JsonTransformer transformer, PlayerDAO playerDAO) {
        super(playerDAO, transformer);
    }

    @Override
    public ResponseData process(String requestBody, Map<String, String> requestParams) throws Exception {
        Player player = transformer.playerFromJson(requestBody);

        int result = playerDAO.addPlayer(player);
        if (result == 0) {
            return new ResponseData(org.eclipse.jetty.server.Response.SC_CONFLICT,
                    "Player with that name already exists.");
        } else {
            player = player.withId(result);
            String message = transformer.render(player);
            return new ResponseData(org.eclipse.jetty.server.Response.SC_CREATED, message);
        }
    }

}
