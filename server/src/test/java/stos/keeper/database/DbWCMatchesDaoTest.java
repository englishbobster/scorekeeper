package stos.keeper.database;

import org.junit.Test;
import stos.keeper.model.FootballMatch;
import stos.keeper.model.Group;
import stos.keeper.model.MatchType;

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

        assertThat(dao.countPlannedMatchEntries(), is(currentCount + 1));

        dao.deleteMatchById(1);
        assertThat(dao.countPlannedMatchEntries(), is(currentCount));
    }

    private DataSource getDataSource() {
        return new PostgresDataSourceImpl();
    }
}