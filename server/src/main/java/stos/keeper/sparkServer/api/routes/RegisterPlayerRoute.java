package stos.keeper.sparkServer.api.routes;

import org.eclipse.jetty.server.Response;
import stos.keeper.database.dao.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.api.messages.PlayerIdAndTokenReply;
import stos.keeper.sparkServer.api.messages.RegisterPlayerRequest;
import stos.keeper.sparkServer.json.JsonTransformer;
import stos.keeper.sparkServer.security.PasswordService;
import stos.keeper.sparkServer.security.TokenGenerator;

import java.util.Map;

public class RegisterPlayerRoute extends AbstractScoreKeeperRoute {
    public static final String PATH = "/register";
    private TokenGenerator tokenGenerator;

    public RegisterPlayerRoute(JsonTransformer transformer, PlayerDAO playerDAO, TokenGenerator tokenGenerator) {
        super(playerDAO, transformer);
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public ResponseData process(String requestBody, Map<String, String> requestParams) throws Exception {
        RegisterPlayerRequest registerPlayerRequest = transformer.registerPlayerFromJson(requestBody);

        if (registerPlayerRequest != null) {
            String passwordSalt = PasswordService.generateSalt();
            String hashedPassword = PasswordService.encryptPassword(registerPlayerRequest.getPassword(), passwordSalt);
            Player player = Player.builder().username(registerPlayerRequest.getUsername())
                    .password(hashedPassword)
                    .email(registerPlayerRequest.getEmail())
                    .passwordSalt(passwordSalt)
                    .created(registerPlayerRequest.getCreated()).build();

            int result = playerDAO.addPlayer(player);

            if (result == 0) {
                return new ResponseData(Response.SC_CONFLICT,
                        "Player with that name already exists.");
            } else {
                String generatedToken = tokenGenerator.generateToken(registerPlayerRequest.getUsername(),
                        registerPlayerRequest.getPassword(), registerPlayerRequest.getEmail());
                PlayerIdAndTokenReply reply = new PlayerIdAndTokenReply(result, player.getUsername(), generatedToken);
                return new ResponseData(Response.SC_CREATED, transformer.render(reply));
            }
        }
        return new ResponseData(Response.SC_BAD_REQUEST, "Invalid Request.");
    }

}
