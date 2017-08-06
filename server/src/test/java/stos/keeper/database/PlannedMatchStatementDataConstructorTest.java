package stos.keeper.database;

import org.junit.Before;
import org.junit.Test;
import stos.keeper.model.FootballMatch;
import stos.keeper.model.Group;
import stos.keeper.model.MatchType;

import java.sql.Timestamp;
import java.time.ZoneId;
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
    private static final String EXPECTED_INSERT = "INSERT INTO planned_matches VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String EXPECTED_COUNT = "SELECT COUNT(*) FROM planned_matches";

    private FootballMatch match;

    @Before
    public void setUp() {
        match = FootballMatch.builder().id(1).arena("ARENA").teams("HOME", "AWAY")
                .time(ZonedDateTime.now()).matchType(MatchType.FINAL).group(Group.NA).build();
    }

    @Test
    public void convert_a_zoned_datetime_to_sql_time_stamp_and_back() throws Exception {
        ZonedDateTime givenTime = ZonedDateTime.of(2004, 10, 19, 10, 23, 54, 0, ZoneId.systemDefault());
        Timestamp expected = Timestamp.from(givenTime.toInstant());
        assertThat(PlannedMatchStatementDataConstructor.sqlTimeStampFrom(givenTime), is(equalTo(expected)));

        ZonedDateTime convertedBack = ZonedDateTime.ofInstant(expected.toInstant(), ZoneId.systemDefault());
        assertThat(convertedBack, is(equalTo(givenTime)));
    }

    @Test
    public void get_statement_data_for_inserting_a_match() {
        StatementDataObject statementData = PlannedMatchStatementDataConstructor
                .getStatementDataFor("addMatch", Optional.of(match));

        List<Object> addMatchParams = statementData.getParameters();
        assertThat(addMatchParams.size(), is(10));
        assertThat(addMatchParams, contains(match.getId()
                , PlannedMatchStatementDataConstructor.sqlTimeStampFrom(match.getMatchTime())
                , match.getArena()
                , match.getHomeTeam()
                , match.getAwayTeam()
                , match.getHomeScore()
                , match.getAwayScore()
                , match.isFullTime()
                , match.getMatchType().name()
                , match.getGroup().name()
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
}