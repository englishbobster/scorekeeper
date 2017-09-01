package stos.keeper.database;

import org.junit.Before;
import org.junit.Test;
import stos.keeper.model.player.Player;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PlayerDAOTest {

    private PlayerDAO dao ;

    @Before
    public void setUp() {
        DataSource dataSource = getDataSource();
        dao = new PlayerDAO(dataSource);
    }

    @Test
    public void add_get_delete_a_user_in_the_database() {
        Player player = Player.builder().username("bob").password("*****")
                .email("maily.mc@mailface.com").hasPaid(false).created(ZonedDateTime.now()).build();

        assertThat(dao.addUser(player), is(1));
        Optional<Player> fetchedPlayerOptionl = dao.getUserByName(player.getUserName());
        assertThat(fetchedPlayerOptionl.get(), is(equalTo(player)));

        assertThat(dao.deleteUserByName(player.getUserName()), is(1));
    }

    @Test
    public void delete_non_existant_player_returns_0() throws Exception {
        assertThat(dao.deleteUserByName("IDONTEXISTS"), is(0));
    }

    private DataSource getDataSource() {
        return new PostgresDataSourceImpl();
    }

}