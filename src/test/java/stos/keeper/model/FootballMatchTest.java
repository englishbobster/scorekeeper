package stos.keeper.model;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class FootballMatchTest {

    public static final String HOMETEAM = "Brazil";
    public static final String AWAYTEAM = "France";
    public static final String SCORE_STRING = HOMETEAM + " 0 - 0 " + AWAYTEAM;
    private FootballMatch match;

    @Before
    public void setUp() {
        match = FootballMatch.builder()
                .time(ZonedDateTime.now())
                .teams(HOMETEAM, AWAYTEAM)
                .arena("Rio").build();
    }

    @Test
    public void can_build_a_football_match() {
        assertThat(match, CoreMatchers.notNullValue());
    }

    @Test
    public void new_match_has_nil_nil_score() {
        assertThat(match.getHomeScore(), is(0));
        assertThat(match.getAwayScore(), is(0));
    }

    @Test
    public void formatted_string_for_score() {
        assertThat(match.getFinalScoreAsString(), is(SCORE_STRING));
    }

}