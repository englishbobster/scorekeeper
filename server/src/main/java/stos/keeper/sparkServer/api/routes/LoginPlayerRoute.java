package stos.keeper.sparkServer.api.routes;

import org.eclipse.jetty.server.Response;
import stos.keeper.database.dao.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.json.JsonTransformer;

import java.util.Map;
import java.util.Optional;

public class LoginPlayerRoute extends AbstractScoreKeeperRoute {
    public static final String PATH = "/login";

    public LoginPlayerRoute(JsonTransformer transformer, PlayerDAO playerDAO) {
        super(playerDAO, transformer);
    }

    @Override
    public ResponseData process(String requestbody, Map<String, String> requestParams) throws Exception {
        Optional<Player> playerByName = playerDAO.getPlayerByName(requestParams.get(":name"));
        if (playerByName.isPresent()) {
            return new ResponseData(Response.SC_OK, transformer.render(playerByName.get()));
        } else {
            return new ResponseData(Response.SC_NOT_FOUND, "Player not found.");
        }
    }
}
