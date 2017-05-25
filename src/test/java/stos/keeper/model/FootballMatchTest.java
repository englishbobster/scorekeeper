package stos.keeper.model;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class FootballMatchTest {

    private static final String HOME_TEAM = "Brazil";
    private static final String AWAY_TEAM = "France";
    private static final String SCORE_STRING = HOME_TEAM + " 0 - 0 " + AWAY_TEAM;
    private static final String SCORE_STRING_FINAL = HOME_TEAM + " 1 - 3 " + AWAY_TEAM;
    public static final String ARENA_NAME = "Rio";
    private FootballMatch match;
    private FootballMatch match_played;

    @Before
    public void setUp() {
        match = FootballMatch.builder()
                .time(ZonedDateTime.now())
                .teams(HOME_TEAM, AWAY_TEAM)
                .arena(ARENA_NAME).build();
        match_played = FootballMatch.builder()
                .time(ZonedDateTime.now())
                .teams(HOME_TEAM, AWAY_TEAM)
                .arena("Rio").build();
        match_played.setFinalScore(4, 1);
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
    public void new_match_is_not_played() {
        assertThat(match.isFullTime(), is(false));
    }

    @Test
    public void set_the_final_score() {
        match.setFinalScore(1, 3);
        assertThat(match.getHomeScore(), is(1));
        assertThat(match.getAwayScore(), is(3));
        assertThat(match.isFullTime(), is(true));
        assertThat(match.getFinalScoreAsString(), is(SCORE_STRING_FINAL));
    }

    @Test
    public void formatted_string_for_score() {
        assertThat(match.getFinalScoreAsString(), is(SCORE_STRING));
    }

    @Test
    public void same_match_with_differing_scores_are_equal() {
        assertThat(match, is(equalTo(match_played)));
    }
}