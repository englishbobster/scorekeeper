package stos.keeper.sparkServer.routes;

import org.eclipse.jetty.server.Response;
import stos.keeper.database.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.model.player.PlayerIdAndToken;
import stos.keeper.sparkServer.json.JsonTransformer;
import stos.keeper.sparkServer.security.DigestKeyChest;
import stos.keeper.sparkServer.security.PasswordService;
import stos.keeper.sparkServer.security.TokenGenerator;

import java.util.Map;

public class RegisterPlayerRoute extends AbstractScoreKeeperRoute {
    public static final String PATH = "/register";

    public RegisterPlayerRoute(JsonTransformer transformer, PlayerDAO playerDAO) {
        super(playerDAO, transformer);
    }

    @Override
    public ResponseData process(String requestBody, Map<String, String> requestParams) throws Exception {
        Player player_incoming = transformer.playerFromJson(requestBody);
        byte[] passwordSalt = PasswordService.generateSalt();
        byte[] hashedPassword = PasswordService.encryptPassword(player_incoming.getPassword(), passwordSalt);
        Player player = player_incoming.withHashedPasswordAndSalt(new String(hashedPassword), passwordSalt);

        int result = playerDAO.addPlayer(player);
        if (result == 0) {
            return new ResponseData(Response.SC_CONFLICT,
                    "Player with that name already exists.");
        } else {
            String generatedToken = new TokenGenerator(player.getUserName(),
                    player.getPassword(), player.getEmail(), DigestKeyChest.getInstance()).generateToken();
            String message = transformer.render(new PlayerIdAndToken(result, player.getUserName(), generatedToken));
            return new ResponseData(Response.SC_CREATED, message);
        }
    }

}
