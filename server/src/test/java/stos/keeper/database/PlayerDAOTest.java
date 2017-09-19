package stos.keeper.database;

import org.junit.Before;
import org.junit.Test;
import stos.keeper.database.dao.PlayerDAO;
import stos.keeper.model.player.Player;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PlayerDAOTest {

    private PlayerDAO dao ;
    private Player player;
    private Player player_2;
    private Player player_3;

    @Before
    public void setUp() {
        player = Player.builder().username("bob").password("*****").passwordSalt("sfsdffsf")
                .email("maily.mc@mailface.com").hasPaid(false).created(ZonedDateTime.now()).build();
        player_2 = Player.builder().username("robert").password("hanzo").passwordSalt("aslaflajf")
                .email("robert@mailface.com").hasPaid(false).created(ZonedDateTime.now()).build();
        player_3 = Player.builder().username("gringots").password("bank").passwordSalt("eioufgk")
                .email("gringots@diagonally.com").hasPaid(false).created(ZonedDateTime.now()).build();

        DataSource dataSource = getDataSource();
        dao = new PlayerDAO(dataSource);
    }

    @Test
    public void add_get_delete_a_user_in_the_database() {
        assertThat(dao.addPlayer(player), is(1));
        Optional<Player> fetchedPlayerOptionl = dao.getPlayerByName(player.getUsername());
        assertThat(fetchedPlayerOptionl.get(), is(equalTo(player)));

        assertThat(dao.deletePlayerByName(player.getUsername()), is(1));
    }

    @Test
    public void delete_non_existant_player_returns_0() throws Exception {
        assertThat(dao.deletePlayerByName("IDONTEXISTS"), is(0));
    }


    @Test
    public void generated_keys_are_returned() throws Exception {
        int playerId = dao.addPlayer(player);
        assertThat(playerId, is(greaterThan(0)));
        int player2Id = dao.addPlayer(player_2);
        assertThat(player2Id, is(greaterThan(playerId)));
        assertThat(dao.deletePlayerByName("robert"), is(1));
        int player3Id = dao.addPlayer(player_3);
        assertThat(player3Id, is(greaterThan(player2Id)));
        assertThat(player3Id, is(greaterThan(2)));
        assertThat(dao.deletePlayerByName("bob"), is(1));
        assertThat(dao.deletePlayerByName("gringots"), is(1));
    }

    private DataSource getDataSource() {
        return new PostgresDataSourceImpl();
    }

}
