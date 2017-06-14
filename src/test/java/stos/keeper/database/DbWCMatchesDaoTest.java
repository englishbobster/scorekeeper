package stos.keeper.database;

import org.junit.Test;
import stos.keeper.model.FootballMatch;

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

    private DataSource getDataSource() {
        return new PostgresDataSourceImpl();
    }

}