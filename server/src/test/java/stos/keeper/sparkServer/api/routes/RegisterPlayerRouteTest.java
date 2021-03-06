package stos.keeper.sparkServer.api.routes;

import org.eclipse.jetty.server.Response;
import org.junit.Test;
import stos.keeper.database.dao.PlayerDAO;
import stos.keeper.sparkServer.json.JsonTransformer;
import stos.keeper.sparkServer.security.DigestKeyChest;
import stos.keeper.sparkServer.security.TokenGenerator;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterPlayerRouteTest {

    PlayerDAO dao = mock(PlayerDAO.class);
    public static final String REQUEST_BODY = "{\"username\": \"minger\"," +
            "\"password\" : \"twinger\"," +
            "\"email\" : \"abcde@ghijklmn.opq\", " +
            "\"created\" :\"2007-12-03T10:15:30Z\"}";
    private TokenGenerator tokenGenerator = new TokenGenerator(DigestKeyChest.getInstance());

    @Test
    public void returns_created_response_and_player_with_an_id() throws Exception {
        String expectedResponseMessage = "{\"id\":1," +
                "\"username\":\"minger\"," +
                "\"token\":";

        when(dao.addPlayer(anyObject())).thenReturn(1);

        RegisterPlayerRoute route = new RegisterPlayerRoute(new JsonTransformer(), dao, tokenGenerator);
        ResponseData data = route.process(REQUEST_BODY, Collections.EMPTY_MAP);
        assertThat(data.getResponseStatus(), is(Response.SC_CREATED));
        assertThat(data.getResponseMessage(), startsWith(expectedResponseMessage));
    }

    @Test
    public void exception_when_rubbish_is_sent_in() throws Exception {
        RegisterPlayerRoute route = new RegisterPlayerRoute(new JsonTransformer(), dao, tokenGenerator);

        ResponseData data = route.process("", Collections.EMPTY_MAP);
        when(dao.addPlayer(anyObject())).thenReturn(0);
        assertThat(data.getResponseStatus(), is(Response.SC_BAD_REQUEST));
        assertThat(data.getResponseMessage(), is("Invalid Request."));

    }

    @Test
    public void returns_failed_response() throws Exception {
        tokenGenerator = new TokenGenerator(DigestKeyChest.getInstance());
        RegisterPlayerRoute route = new RegisterPlayerRoute(new JsonTransformer(), dao, tokenGenerator);

        ResponseData data = route.process(REQUEST_BODY, Collections.EMPTY_MAP);
        when(dao.addPlayer(anyObject())).thenReturn(0);
        assertThat(data.getResponseStatus(), is(Response.SC_CONFLICT));
        assertThat(data.getResponseMessage(), is("Player with that name already exists."));
    }
}