package stos.keeper.database;

import org.junit.Before;
import org.junit.Test;
import stos.keeper.model.planned_matches.FootballMatch;
import stos.keeper.model.planned_matches.MatchType;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class PlannedMatchStatementDataConstructorTest {

    private static final String EXPECTED_DELETE = "DELETE FROM planned_matches WHERE id= ?";
    private static final String EXPECTED_SELECT = "SELECT * FROM planned_matches WHERE id= ?";
    private static final String EXPECTED_INSERT = "INSERT INTO planned_matches VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String EXPECTED_COUNT = "SELECT COUNT(*) FROM planned_matches";
    public static final String EXPECTED_SELECT_ALL = "SELECT * FROM planned_matches";
    public static final String EXPECTED_UPDATE_SCORE ="UPDATE planned_matches SET (home_score, away_score, fulltime)=(?, ?, ?) WHERE ID= ? and fulltime = false";

    private FootballMatch match;

    @Before
    public void setUp() {
        match = FootballMatch.builder().id(1).arena("ARENA").teams("HOME", "AWAY")
                .time(ZonedDateTime.now()).matchType(MatchType.FINAL).build();
    }

    @Test
    public void get_statement_data_for_inserting_a_match() {
        StatementDataObject statementData = PlannedMatchStatementDataConstructor
                .getStatementDataFor("addMatch", Optional.of(match));

        List<Object> addMatchParams = statementData.getParameters();
        assertThat(addMatchParams.size(), is(9));
        assertThat(addMatchParams, contains(match.getId()
                , ConversionUtils.sqlTimeStampFrom(match.getMatchTime())
                , match.getArena()
                , match.getHomeTeam()
                , match.getAwayTeam()
                , match.getScore().getHomeScore()
                , match.getScore().getAwayScore()
                , match.isFullTime()
                , match.getMatchType().name()
                ));
        assertThat(statementData.getSqlStatement(), is(equalTo(EXPECTED_INSERT)));
    }

    @Test
    public void get_statement_data_for_deleting_a_match_by_id() throws Exception {
        StatementDataObject statementData = PlannedMatchStatementDataConstructor
                .getStatementDataFor("deleteMatchById", Optional.empty());
        List<Object> deleteMatchParams = statementData.getParameters();

        assertThat(deleteMatchParams.size(), is(0));
        assertThat(statementData.getSqlStatement(), is(equalTo(EXPECTED_DELETE)));
    }

    @Test
    public void get_statement_data_for_finding_a_match_by_id() throws Exception {
        StatementDataObject statementData = PlannedMatchStatementDataConstructor
                .getStatementDataFor("findMatchById", Optional.empty());
        List<Object> findMatchParams = statementData.getParameters();

        assertThat(findMatchParams.size(), is(0));
        assertThat(statementData.getSqlStatement(), is(equalTo(EXPECTED_SELECT)));
    }

    @Test
    public void get_statement_data_for_getting_planned_match_table_row_count() {
        StatementDataObject statementData = PlannedMatchStatementDataConstructor
                .getStatementDataFor("countPlannedMatches", Optional.empty());
        List<Object> countMatchesParams = statementData.getParameters();

        assertThat(countMatchesParams.size(), is(0));
        assertThat(statementData.getSqlStatement(), is(equalTo(EXPECTED_COUNT)));
    }

    @Test
    public void get_statement_data_for_fetching_all_planned_matches() {
        StatementDataObject statementData = PlannedMatchStatementDataConstructor
                .getStatementDataFor("fetchAllPlannedMatches", Optional.empty());
        List<Object> fetchAllMatchParams = statementData.getParameters();

        assertThat(fetchAllMatchParams.size(), is(0));
        assertThat(statementData.getSqlStatement(), is(equalTo(EXPECTED_SELECT_ALL)));
    }

    @Test
    public void get_statement_data_update_a_planned_match_score() throws Exception {
        StatementDataObject statementData = PlannedMatchStatementDataConstructor
                .getStatementDataFor("updatePlannedMatchScoreById", Optional.empty());
        List<Object> updateMatchParams = statementData.getParameters();
        assertThat(updateMatchParams.size(), is(0));
        assertThat(statementData.getSqlStatement(), is(equalTo(EXPECTED_UPDATE_SCORE)));
    }
}