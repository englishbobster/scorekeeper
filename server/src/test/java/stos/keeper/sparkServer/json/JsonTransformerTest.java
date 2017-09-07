package stos.keeper.sparkServer.json;

import org.junit.Before;
import org.junit.Test;
import stos.keeper.model.planned_matches.FootballMatch;
import stos.keeper.model.planned_matches.MatchType;
import stos.keeper.model.player.Player;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class JsonTransformerTest {

    private String addPlayerRequestJson;
    private String scoreJson;

    @Before
    public void setUp() throws Exception {
        addPlayerRequestJson = "{\"userName\": \"minger\",\n" +
                " \"password\" : \"twinger\",\n" +
                " \"email\" : \"abcde@ghijklmn.opq\",\n" +
                " \"created\" : \"2007-12-03T10:15:30.00Z\"\n" +
                "}";
        scoreJson = "{\"score\":{" +
                "\"homeScore\":5," +
                "\"awayScore\":4" +
                "}" +
                "}";
    }

    @Test
    public void should_deserialize_time_correctly() throws Exception {
        JsonTransformer transformer = new JsonTransformer();


        Player expectedPlayer = Player.builder().username("minger")
                .password("twinger").email("abcde@ghijklmn.opq")
                .created(ZonedDateTime.parse("2007-12-03T10:15:30.00Z")).build();

        Player player = transformer.playerFromJson(addPlayerRequestJson);
        assertThat(player, is(equalTo(expectedPlayer)));
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
                "\"matchTime\":\"Feb 20, 1970 8:00\"," +
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