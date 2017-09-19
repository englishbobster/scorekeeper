package stos.keeper.sparkServer.json;

import org.junit.Before;
import org.junit.Test;
import stos.keeper.model.planned_matches.FootballMatch;
import stos.keeper.model.planned_matches.MatchType;
import stos.keeper.sparkServer.api.messages.LoginPlayerRequest;
import stos.keeper.sparkServer.api.messages.PlayerIdAndTokenReply;
import stos.keeper.sparkServer.api.messages.RegisterPlayerRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class JsonTransformerTest {

    private String registerPlayerRequestJson;
    private String scoreJson;
    private String loginRequest;
    private String renderedPlayerIdAndToken;

    @Before
    public void setUp() throws Exception {
        registerPlayerRequestJson = "{\"username\": \"minger\",\n" +
                " \"password\" : \"twinger\",\n" +
                " \"email\" : \"abcde@ghijklmn.opq\",\n" +
                " \"created\" : \"2007-12-03T10:15:30.00Z\"\n" +
                "}";
        scoreJson = "{\"score\":{" +
                "\"homeScore\":5," +
                "\"awayScore\":4" +
                "}" +
                "}";
        loginRequest = "{\"username\": \"minger\",\n" +
                " \"password\" : \"twinger\"" + "}";
        renderedPlayerIdAndToken = "{\"id\":7," +
                "\"username\":\"billybob\"," +
                "\"token\":\"a_long_long_long.tokenkindofthing.asdfnnsf\"" +
                "}";
    }

    @Test
    public void should_deserialize_time_correctly() throws Exception {
        JsonTransformer transformer = new JsonTransformer();

        RegisterPlayerRequest expectedPlayer = new RegisterPlayerRequest("minger","twinger","abcde@ghijklmn.opq",
                ZonedDateTime.parse("2007-12-03T10:15:30.00Z"));

        RegisterPlayerRequest player = transformer.registerPlayerFromJson(registerPlayerRequestJson);
        assertThat(player, is(equalTo(expectedPlayer)));
    }

    @Test
    public void should_deserialize_LoginPlayerRequest() throws Exception {
        JsonTransformer transformer = new JsonTransformer();
        LoginPlayerRequest playerRequest = transformer.loginPlayerFromJson(loginRequest);
        assertThat(playerRequest.getUsername(), is("minger"));
        assertThat(playerRequest.getPassword(), is("twinger"));
    }

    @Test
    public void should_render_PlayerIdAndToken_correctly() throws Exception {
        JsonTransformer transformer = new JsonTransformer();
        PlayerIdAndTokenReply reply = new PlayerIdAndTokenReply(7, "billybob", "a_long_long_long.tokenkindofthing.asdfnnsf");
        assertThat(transformer.render(reply), is(renderedPlayerIdAndToken));
    }

    @Test
    public void should_render_correct_json_serialization_of_planned_matches() throws Exception {
        JsonTransformer transformer = new JsonTransformer();
        ZonedDateTime time = ZonedDateTime
                .of(1970, 2, 20, 8, 0, 0, 0, ZoneId.systemDefault());

        FootballMatch match = FootballMatch.builder().id(1).arena("ARENA").time(time)
                .teams("HOME", "AWAY").matchType(MatchType.QUARTERS)
                .build();

        String expectedSerialization = "[{\"id\":1," +
                "\"matchTime\":\"1970-02-20T08:00+01:00[Europe/Stockholm]\"," +
                "\"arena\":\"ARENA\"," +
                "\"homeTeam\":\"HOME\"," +
                "\"awayTeam\":\"AWAY\"," +
                "\"score\":{" +
                    "\"homeScore\":0," +
                    "\"awayScore\":0" +
                    "}," +
                "\"fullTime\":false," +
                "\"matchType\":\"Quarter-Finals\"" +
                "}]";

        List<FootballMatch> listOfPlannedMatches = new ArrayList<>();
        listOfPlannedMatches.add(match);
        String formattedJson = transformer.render(listOfPlannedMatches);

        assertThat(formattedJson, is(equalTo(expectedSerialization)));
    }
}