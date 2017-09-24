package stos.keeper.sparkServer.api.routes;

import org.eclipse.jetty.server.Response;
import stos.keeper.database.dao.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.api.messages.LoginPlayerRequest;
import stos.keeper.sparkServer.api.messages.PlayerIdAndTokenReply;
import stos.keeper.sparkServer.json.JsonTransformer;
import stos.keeper.sparkServer.security.PasswordService;
import stos.keeper.sparkServer.security.TokenGenerator;

import java.util.Map;
import java.util.Optional;

public class LoginPlayerRoute extends AbstractScoreKeeperRoute {
    public static final String PATH = "/login";
    private final TokenGenerator tokenGenerator;

    public LoginPlayerRoute(JsonTransformer transformer, PlayerDAO playerDAO, TokenGenerator tokenGenerator) {
        super(playerDAO, transformer);
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public ResponseData process(String requestBody, Map<String, String> requestParams) throws Exception {
        LoginPlayerRequest loginRequest = transformer.loginPlayerFromJson(requestBody);
        if (loginRequest != null) {
            Optional<Player> playerOptional = playerDAO.getPlayerByName(loginRequest.getUsername());
            if (playerOptional.isPresent()) {
                Player playerByName = playerOptional.get();
                boolean authentic = PasswordService.authenticate(loginRequest.getPassword(),
                        playerByName.getPassword(), playerByName.getSalt());
                if (authentic) {
                    String token = tokenGenerator.generateToken(playerByName.getUsername(),
                            loginRequest.getPassword(), playerByName.getEmail());
                    PlayerIdAndTokenReply reply = new PlayerIdAndTokenReply(playerByName.getId(),
                            playerByName.getUsername(), token);
                    return new ResponseData(Response.SC_OK, transformer.render(reply));
                } else {
                    return new ResponseData(Response.SC_UNAUTHORIZED, "Player not authorized.");
                }
            } else {
                return new ResponseData(Response.SC_NOT_FOUND, "Player not found.");
            }
        }
        return new ResponseData(Response.SC_BAD_REQUEST, "Invalid Request.");
    }
}
