package stos.keeper.database;

import org.junit.Before;
import org.junit.Test;
import stos.keeper.model.FootballMatch;
import stos.keeper.model.MatchType;
import stos.keeper.model.Score;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;


public class DbPlannedMatchesDAOTest {

    private static final int ID_1 = 1;
    private static final int ID_1000 = 1000;
    private static final int ID_1001 = 1001;
    private DbPlannedMatchesDAO dao;
    private FootballMatch expectedMatch_id_1;
    private FootballMatch expectedMatch_id_1000;
    private FootballMatch expectedMatch_id_1001;

    @Before
    public void setUp() {
        DataSource dataSource = getDataSource();
        dao = new DbPlannedMatchesDAO(dataSource);

        expectedMatch_id_1 = FootballMatch.builder().id(ID_1).time(ZonedDateTime.now())
                .teams("HOME", "AWAY").matchType(MatchType.F)
                .arena("ARENA").build();

        expectedMatch_id_1000 = FootballMatch.builder().id(ID_1000).time(ZonedDateTime.now())
                .teams("HOME", "AWAY").matchType(MatchType.F)
                .arena("ARENA").build();

        expectedMatch_id_1001 = FootballMatch.builder().id(ID_1001).time(ZonedDateTime.now().minusDays(15L))
                .teams("GOOD", "BAD").matchType(MatchType.FINAL)
                .arena("ARIANNA").build();
    }

    @Test
    public void insert_a_match_into_planned_matches() {
        dao.addMatch(expectedMatch_id_1);
        Optional<FootballMatch> fetchedMatch = dao.findMatchById(1);
        assertThat(fetchedMatch.get(), is(equalTo(expectedMatch_id_1)));

        dao.deleteMatchById(ID_1);
        Optional<FootballMatch> match = dao.findMatchById(1);
        assertThat(match, is(equalTo(Optional.empty())));
    }

    @Test
    public void count_matches_in_planned_matches_table() throws Exception {
        int currentCount = dao.countPlannedMatchEntries();

        dao.addMatch(expectedMatch_id_1);
        Optional<FootballMatch> fetchedMatch = dao.findMatchById(1);
        assertThat(fetchedMatch.get(), is(equalTo(expectedMatch_id_1)));

        int incremented_count = currentCount + 1;
        assertThat(dao.countPlannedMatchEntries(), is(incremented_count));

        dao.deleteMatchById(ID_1);
        assertThat(dao.countPlannedMatchEntries(), is(currentCount));
    }

    @Test
    public void get_all_the_planned_matches() {
        dao.addMatch(expectedMatch_id_1000);
        dao.addMatch(expectedMatch_id_1001);
        int count = dao.countPlannedMatchEntries();
        assertThat(count, is(equalTo(2)));

        List<FootballMatch> allPlannedMatches = dao.getAllPlannedMatches();
        assertThat(allPlannedMatches, contains(expectedMatch_id_1000, expectedMatch_id_1001));

        dao.deleteMatchById(ID_1001);
        dao.deleteMatchById(ID_1000);
        int actual = dao.countPlannedMatchEntries();
        assertThat(actual, is(equalTo(0)));
    }

    @Test
    public void update_a_match_by_id() throws Exception {
        dao.addMatch(expectedMatch_id_1000);
        int current_home_score = expectedMatch_id_1000.getScore().getHomeScore();
        int current_away_score = expectedMatch_id_1000.getScore().getAwayScore();

        Optional<FootballMatch> fetchedMatchOptional = dao.findMatchById(ID_1000);
        assertThat(fetchedMatchOptional, is(not(Optional.empty())));

        FootballMatch fetchedMatch = fetchedMatchOptional.get();
        assertThat(fetchedMatch.getScore().getHomeScore(), is(current_home_score));
        assertThat(fetchedMatch.getScore().getAwayScore(), is(current_away_score));

        dao.setFinalScoreForMatchById(ID_1000, new Score(7, 1));

        Optional<FootballMatch> updatedMatchOptional = dao.findMatchById(ID_1000);
        assertThat(updatedMatchOptional, is(not(Optional.empty())));

        FootballMatch updatedMatch = updatedMatchOptional.get();
        assertThat(updatedMatch.getScore().getHomeScore(), is(7));
        assertThat(updatedMatch.getScore().getAwayScore(), is(1));

        dao.deleteMatchById(ID_1000);
        int actual = dao.countPlannedMatchEntries();
        assertThat(actual, is(equalTo(0)));
    }

    private DataSource getDataSource() {
        return new PostgresDataSourceImpl();
    }
}