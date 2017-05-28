package stos.keeper.model;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class FootballMatchTest {

    private static final String HOME_TEAM = "Brazil";
    private static final String AWAY_TEAM = "France";
    private static final String SCORE_STRING = HOME_TEAM + " 0 - 0 " + AWAY_TEAM;
    private static final String SCORE_STRING_FINAL = HOME_TEAM + " 1 - 3 " + AWAY_TEAM;
    private static final ZonedDateTime now = ZonedDateTime.now();
    private static final String DATE_STRING = now.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    private static final String ARENA_NAME_RIO = "Rio";
    private static final String ARENA_NAME_WEMBLEY = "Wembley";
    private static final String HOME_TEAM_ENGLAND = "England";
    private static final String HOME_TEAM_CAMEROON = "Cameroon";
    private static final String EXPECTED_TO_STRING =
            "(" + DATE_STRING + ") " + ARENA_NAME_RIO + " [" + SCORE_STRING + "]\n";
    private FootballMatch match;
    private FootballMatch match_played;
    private FootballMatch another_match;
    private FootballMatch replayed_match;

    @Before
    public void setUp() {
        match = FootballMatch.builder()
                .time(now)
                .teams(HOME_TEAM, AWAY_TEAM)
                .matchType(MatchType.GROUPGAME)
                .group(Group.A)
                .arena(ARENA_NAME_RIO).build();

        match_played = FootballMatch.builder()
                .time(now)
                .teams(HOME_TEAM, AWAY_TEAM)
                .matchType(MatchType.GROUPGAME)
                .group(Group.A)
                .arena(ARENA_NAME_RIO).build();
        match_played.setFinalScore(4, 1);

        replayed_match = FootballMatch.builder()
                .time(now)
                .teams(HOME_TEAM, AWAY_TEAM)
                .matchType(MatchType.GROUPGAME)
                .group(Group.A)
                .arena(ARENA_NAME_RIO).build();
        replayed_match.setFinalScore(2, 2);

        another_match = FootballMatch.builder()
                .time(now)
                .teams(HOME_TEAM_ENGLAND, HOME_TEAM_CAMEROON)
                .matchType(MatchType.ROUND16)
                .arena(ARENA_NAME_WEMBLEY).build();
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
    public void match_is_played() {
        assertThat(match_played.isFullTime(), is(true));
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
    public void to_string_expected() {
        assertThat(match.toString(), is(EXPECTED_TO_STRING));
    }

    @Test
    public void same_match_with_differing_scores_are_equal() {
        assertThat(match, is(equalTo(match_played)));
    }

    @Test
    public void different_matches_are_not_equal() {
        assertThat(match, is(not(equalTo(another_match))));
    }

    @Test
    public void equals_symmetry() {
        assertTrue("equals is not symmetric", match.equals(match_played));
        assertTrue("equals is not symmetric", match_played.equals(match));
    }

    @Test
    public void equals_transitivity() {
        assertTrue(match.equals(match_played));
        assertTrue(match_played.equals(replayed_match));
        assertTrue(match.equals(replayed_match));
    }

    @Test
    public void hashcode_the_same_for_equal_objects() {
        assertThat(match.hashCode(), is(equalTo(match_played.hashCode())));
        assertThat(match.hashCode(), is(equalTo(replayed_match.hashCode())));
    }

    @Test
    public void hashcode_different_for_unequal_objects() {
        assertThat(match.hashCode(), is(not(equalTo(another_match.hashCode()))));
    }

    @Test
    public void match_has_match_type() {
        assertThat(match.getMatchType(), is(MatchType.GROUPGAME));
    }

    @Test
    public void when_group_game_group_has_value() {
        assertThat(match.getGroup(), is(Group.A));
    }

    @Test
    public void when_not_group_game_group_has_value_na() {
        assertThat(another_match.getGroup(), is(Group.NA));
    }

}