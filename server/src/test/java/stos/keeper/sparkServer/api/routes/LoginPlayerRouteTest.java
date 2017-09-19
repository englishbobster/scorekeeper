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

    private static final String USER_NAME = "bobby";
    private static final String PASSWORD = "dallas";
    private static final String WRONG_PASSWORD = "wrong_password";
    private static final String EMAIL = "another@email.address";
    private PlayerDAO dao;
    private TokenGenerator tokenGenerator;
    private static final String REQUEST_BODY = "{\"username\":\"" + USER_NAME + "\"," + "\"password\":\"" + PASSWORD + "\"" + "}";
    private static final String REQUEST_BODY_WRONG_PWD = "{\"username\":\"" + USER_NAME + "\"," + "\"password\":\"" + WRONG_PASSWORD + "\"" + "}";
    private static final String EXPECTED_RESPONSE = "{\"id\":7,\"username\":\"" + USER_NAME + "\"," +
            "\"token\":";

    @Before
    public void setUp() throws Exception {
        dao = mock(PlayerDAO.class);
        tokenGenerator = new TokenGenerator(DigestKeyChest.getInstance());
    }

    @Test
    public void player_json_message_is_returned_on_a_successful_request() throws Exception {
        ZonedDateTime time = ZonedDateTime.now();
        String salt = PasswordService.generateSalt();
        String encryptedPassword = PasswordService.encryptPassword(PASSWORD, salt);
        Player player = Player.builder().id(7).username(USER_NAME)
                .password(encryptedPassword).passwordSalt(salt)
                .email(EMAIL).hasPaid(true)
                .created(time).build();

        when(dao.getPlayerByName(USER_NAME)).thenReturn(Optional.of(player));
        LoginPlayerRoute route = new LoginPlayerRoute(new JsonTransformer(), dao, tokenGenerator);
        ResponseData responseData = route.process(REQUEST_BODY, Collections.emptyMap());

        assertThat(responseData.getResponseStatus(), is(Response.SC_OK));
        assertThat(responseData.getResponseMessage(), startsWith(EXPECTED_RESPONSE));
    }

    @Test
    public void unauthorized_is_returned_with_wrong_password() throws Exception {
        ZonedDateTime time = ZonedDateTime.now();
        String salt = PasswordService.generateSalt();
        String encryptedPassword = PasswordService.encryptPassword(PASSWORD, salt);
        Player player = Player.builder().id(7).username(USER_NAME)
                .password(encryptedPassword).passwordSalt(salt)
                .email(EMAIL).hasPaid(true)
                .created(time).build();

        when(dao.getPlayerByName(USER_NAME)).thenReturn(Optional.of(player));
        LoginPlayerRoute route = new LoginPlayerRoute(new JsonTransformer(), dao, tokenGenerator);
        ResponseData responseData = route.process(REQUEST_BODY_WRONG_PWD, Collections.emptyMap());

        assertThat(responseData.getResponseStatus(), is(Response.SC_UNAUTHORIZED));
        assertThat(responseData.getResponseMessage(), startsWith("Player not authorized."));
    }

    @Test
    public void not_found_is_returned_without_an_existing_player() throws Exception {

        when(dao.getPlayerByName(USER_NAME)).thenReturn(Optional.empty());
        LoginPlayerRoute route = new LoginPlayerRoute(new JsonTransformer(), dao, tokenGenerator);
        ResponseData responseData = route.process(REQUEST_BODY, Collections.emptyMap());

        assertThat(responseData.getResponseStatus(), is(Response.SC_NOT_FOUND));
        assertThat(responseData.getResponseMessage(), startsWith("Player not found."));

    }

    @Test
    public void invalid_is_returned_from_gobbledygook_request() throws Exception {
        LoginPlayerRoute route = new LoginPlayerRoute(new JsonTransformer(), dao, tokenGenerator);
        ResponseData responseData = route.process("", Collections.emptyMap());

        assertThat(responseData.getResponseStatus(), is(Response.SC_BAD_REQUEST));
        assertThat(responseData.getResponseMessage(), startsWith("Invalid Request."));
    }
}