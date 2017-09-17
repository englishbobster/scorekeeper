package stos.keeper.sparkServer.api.routes;

import org.eclipse.jetty.server.Response;
import org.junit.Before;
import org.junit.Test;
import stos.keeper.database.dao.PlayerDAO;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.json.JsonTransformer;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginPlayerRouteTest {

    private PlayerDAO dao;

    @Before
    public void setUp() throws Exception {
        dao = mock(PlayerDAO.class);
    }

    @Test
    public void player_json_message_is_returned_on_a_successful_request() throws Exception {
        ZonedDateTime time = ZonedDateTime.now();
        String timeString = time.toString();
        Player player = Player.builder().id(7).username("bobby").password("dallas").email("another@email.address")
                .hasPaid(true).created(time).build();

        String expectedResponse = "{\"id\":7,\"userName\":\"bobby\"," +
                "\"password\":\"dallas\"," +
                "\"passwordSalt\":[0,0,0,0]," +
                "\"email\":\"another@email.address\"," +
                "\"hasPaid\":true," +
                "\"created\":\"" +
                timeString +
                "\"}";

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put(":name", "bobby");
        when(dao.getPlayerByName(requestParams.get(":name"))).thenReturn(Optional.of(player));
        LoginPlayerRoute route = new LoginPlayerRoute(new JsonTransformer(), dao);
        ResponseData responseData = route.process("", requestParams);

        assertThat(responseData.getResponseStatus(), is(Response.SC_OK));
        assertThat(responseData.getResponseMessage(), is(expectedResponse));
    }

    @Test
    public void error_is_returned_when_login_fails() throws Exception {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put(":name", "torkel");
        when(dao.getPlayerByName(requestParams.get(":name"))).thenReturn(Optional.empty());
        LoginPlayerRoute route = new LoginPlayerRoute(new JsonTransformer(), dao);
        ResponseData responseData = route.process("", requestParams);

        assertThat(responseData.getResponseStatus(), is(Response.SC_NOT_FOUND));
        assertThat(responseData.getResponseMessage(), is("Player not found."));

    }
}