package stos.keeper.sparkServer.routes;

import org.eclipse.jetty.server.Response;
import org.junit.Test;
import stos.keeper.database.PlayerDAO;
import stos.keeper.sparkServer.json.JsonTransformer;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterPlayerRouteTest {

    PlayerDAO dao = mock(PlayerDAO.class);

    @Test
    public void returns_created_response_and_player_with_an_id() throws Exception {
        String requestBody = "{\"userName\": \"minger\"," +
                "\"password\" : \"twinger\"," +
                "\"email\" : \"abcde@ghijklmn.opq\", " +
                "\"created\" :\"2007-12-03T10:15:30.00Z\"}";
        String expectedResponseMessage = "{\"id\":1," +
                "\"userName\":\"minger\"," +
                "\"password\":\"twinger\"," +
                "\"email\":\"abcde@ghijklmn.opq\"," +
                "\"hasPaid\":false," +
                "\"created\":\"Dec 3, 2007 11:15\"}";

        when(dao.addPlayer(anyObject())).thenReturn(1);

        RegisterPlayerRoute route = new RegisterPlayerRoute(new JsonTransformer(), dao);
        ResponseData data = route.process(requestBody, Collections.EMPTY_MAP);
        assertThat(data.getResponseStatus(), is(Response.SC_CREATED));
        assertThat(data.getResponseMessage(), is(expectedResponseMessage));

    }

    @Test
    public void returns_failed_response() throws Exception {
        RegisterPlayerRoute route = new RegisterPlayerRoute(new JsonTransformer(), dao);
        ResponseData data = route.process("", Collections.EMPTY_MAP);
        assertThat(data.getResponseStatus(), is(Response.SC_CONFLICT));
        assertThat(data.getResponseMessage(), is(""));
    }
}