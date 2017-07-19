package stos.keeper.database;

import org.junit.Test;
import stos.keeper.model.FootballMatch;
import stos.keeper.model.Group;
import stos.keeper.model.MatchType;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class DbWCMatchesDaoTest {

    @Test
    public void delete_a_match_from_planned_matches() {
        DataSource dataSource = getDataSource();
        DbWCMatchesDao dao = new DbWCMatchesDao(dataSource);

        dao.deleteMatchById(1);
        Optional<FootballMatch> match = dao.findMatchById(1);

        assertThat(match, is(equalTo(Optional.empty())));
    }


    @Test
    public void insert_a_match_into_planned_matches() {
        DataSource dataSource = getDataSource();
        DbWCMatchesDao dao = new DbWCMatchesDao(dataSource);

        FootballMatch expectedMatch = FootballMatch.builder().id(1).time(ZonedDateTime.now())
                .teams("HOME", "AWAY").group(Group.F).matchType(MatchType.GROUPGAME)
                .arena("ARENA").build();

        dao.addMatch(expectedMatch);
        Optional<FootballMatch> fetchedMatch = dao.findMatchById(1);
        assertThat(fetchedMatch.get(), is(equalTo(expectedMatch)));

        dao.deleteMatchById(1);
        Optional<FootballMatch> match = dao.findMatchById(1);
        assertThat(match, is(equalTo(Optional.empty())));
    }

    @Test
    public void convert_a_zoned_datetime_to_sql_time_stamp_and_back() throws Exception {
        ZonedDateTime givenTime = ZonedDateTime.of(2004, 10, 19, 10, 23, 54, 0, ZoneId.systemDefault());
        Timestamp expected = Timestamp.from(givenTime.toInstant());
        assertThat(DbWCMatchesDao.sqlTimeStampFrom(givenTime), is(equalTo(expected)));

        ZonedDateTime convertedBack = ZonedDateTime.ofInstant(expected.toInstant(), ZoneId.systemDefault());
        assertThat(convertedBack, is(equalTo(givenTime)));
    }

    private DataSource getDataSource() {
        return new PostgresDataSourceImpl();
    }
}