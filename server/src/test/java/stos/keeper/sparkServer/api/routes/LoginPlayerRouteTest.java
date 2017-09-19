package stos.keeper.sparkServer.api.routes;

import org.eclipse.jetty.server.Response;
import org.junit.Before;
import org.junit.Test;
import stos.keeper.database.dao.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.json.JsonTransformer;
import stos.keeper.sparkServer.security.DigestKeyChest;
import stos.keeper.sparkServer.security.PasswordService;
import stos.keeper.sparkServer.security.TokenGenerator;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginPlayerRouteTest {

    private PlayerDAO dao;
    private TokenGenerator tokenGenerator;

    @Before
    public void setUp() throws Exception {
        dao = mock(PlayerDAO.class);
        tokenGenerator = new TokenGenerator(DigestKeyChest.getInstance());
    }

    @Test
    public void player_json_message_is_returned_on_a_successful_request() throws Exception {
        ZonedDateTime time = ZonedDateTime.now();
        String salt = PasswordService.generateSalt();
        String encryptedPassword = PasswordService.encryptPassword("dallas", salt);
        Player player = Player.builder().id(7).username("bobby")
                .password(encryptedPassword).passwordSalt(salt)
                .email("another@email.address").hasPaid(true)
                .created(time).build();

        String requestBody = "{\"username\":\"bobby\"," + "\"password\":\"dallas\"" + "}";

        String expectedResponse = "{\"id\":7,\"username\":\"bobby\"," +
                "\"token\":";

        when(dao.getPlayerByName("bobby")).thenReturn(Optional.of(player));
        LoginPlayerRoute route = new LoginPlayerRoute(new JsonTransformer(), dao, tokenGenerator);
        ResponseData responseData = route.process(requestBody, Collections.emptyMap());

        assertThat(responseData.getResponseStatus(), is(Response.SC_OK));
        assertThat(responseData.getResponseMessage(), startsWith(expectedResponse));
    }

}