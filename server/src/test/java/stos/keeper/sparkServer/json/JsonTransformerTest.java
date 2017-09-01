package stos.keeper.sparkServer.json;

import org.junit.Test;
import stos.keeper.model.planned_matches.FootballMatch;
import stos.keeper.model.planned_matches.MatchType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class JsonTransformerTest {

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