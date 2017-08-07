package stos.keeper.database;

import org.junit.Test;
import stos.keeper.model.FootballMatch;
import stos.keeper.model.Group;
import stos.keeper.model.MatchType;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;


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
    public void count_matches_in_planned_matches_table() throws Exception {
        DataSource dataSource = getDataSource();
        DbWCMatchesDao dao = new DbWCMatchesDao(dataSource);

        FootballMatch expectedMatch = FootballMatch.builder().id(1).time(ZonedDateTime.now())
                .teams("HOME", "AWAY").group(Group.F).matchType(MatchType.GROUPGAME)
                .arena("ARENA").build();

        int currentCount = dao.countPlannedMatchEntries();

        dao.addMatch(expectedMatch);
        Optional<FootballMatch> fetchedMatch = dao.findMatchById(1);
        assertThat(fetchedMatch.get(), is(equalTo(expectedMatch)));

        int incremented_count = currentCount + 1;
        assertThat(dao.countPlannedMatchEntries(), is(incremented_count));

        dao.deleteMatchById(1);
        assertThat(dao.countPlannedMatchEntries(), is(currentCount));
    }

    @Test
    public void get_all_the_planned_matches() {
        DataSource dataSource = getDataSource();
        DbWCMatchesDao dao = new DbWCMatchesDao(dataSource);

        FootballMatch expectedMatch1 = FootballMatch.builder().id(1000).time(ZonedDateTime.now())
                .teams("HOME", "AWAY").matchType(MatchType.GROUPGAME).group(Group.F)
                .arena("ARENA").build();

        FootballMatch expectedMatch2 = FootballMatch.builder().id(1001).time(ZonedDateTime.now().minusDays(15L))
                .teams("GOOD", "BAD").matchType(MatchType.FINAL).group(Group.NA)
                .arena("ARIANNA").build();

        dao.addMatch(expectedMatch1);
        dao.addMatch(expectedMatch2);
        int count = dao.countPlannedMatchEntries();
        assertThat(count, is(equalTo(2)));

        List<FootballMatch> allPlannedMatches = dao.getAllPlannedMatches();
        assertThat(allPlannedMatches, contains(expectedMatch1, expectedMatch2));

        dao.deleteMatchById(1001);
        dao.deleteMatchById(1000);
        int actual = dao.countPlannedMatchEntries();
        assertThat(actual, is(equalTo(0)));
    }

    private DataSource getDataSource() {
        return new PostgresDataSourceImpl();
    }
}