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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class StatementConstructorTest {

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
        assertThat(StatementConstructor.sqlTimeStampFrom(givenTime), is(equalTo(expected)));

        ZonedDateTime convertedBack = ZonedDateTime.ofInstant(expected.toInstant(), ZoneId.systemDefault());
        assertThat(convertedBack, is(equalTo(givenTime)));
    }

    @Test
    public void get_parameter_list_for_inserting_a_match() {
        StatementConstructor constructor = new StatementConstructor();
        List<Object> addMatchParams = constructor.getParametersFor("addMatch", match);

        assertThat(addMatchParams.size(), is(10));
        assertThat(addMatchParams, contains(match.getId()
                , StatementConstructor.sqlTimeStampFrom(match.getMatchTime())
                , match.getArena()
                , match.getHomeTeam()
                , match.getAwayTeam()
                , match.getHomeScore()
                , match.getAwayScore()
                , match.isFullTime()
                , match.getMatchType().name()
                , match.getGroup().name()
                ));
    }

    @Test
    public void get_parameter_list_for_deleting_a_match() throws Exception {
        StatementConstructor constructor = new StatementConstructor();
        List<Object> deleteMatchParams = constructor.getParametersFor("deleteMatch", match);

        assertThat(deleteMatchParams.size(), is(1));
        assertThat(deleteMatchParams, contains(match.getId()));
    }
}