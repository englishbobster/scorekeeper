package stos.keeper.database;

import org.junit.Test;
import stos.keeper.database.helpers.ConversionUtils;
import stos.keeper.database.helpers.PlayerStatementDataConstructor;
import stos.keeper.database.helpers.StatementDataObject;
import stos.keeper.model.player.Player;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PlayerStatementDataConstructorTest {

    private static final String EXPECTED_ADD_USER = "INSERT INTO players (username, password, salt, email, paid, created) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String EXPECTED_GET_USER = "SELECT * FROM players WHERE username= ?";
    private static final String EXPECTED_DELETE_USER = "DELETE FROM players WHERE username= ?";

    @Test
    public void construct_correct_parameters_for_adding_a_user() throws Exception {
        ZonedDateTime now = ZonedDateTime.now();
        Player player = Player.builder().username("user").password("mntgomery").email("a@b.c").hasPaid(true)
                .created(now).build();
        StatementDataObject addUserStatementData = PlayerStatementDataConstructor
                .getStatementDataFor("addPlayer", Optional.of(player));

        assertThat(addUserStatementData.getSqlStatement(), is(EXPECTED_ADD_USER));
        assertThat(addUserStatementData.getParameters(), contains(
                player.getUserName(),
                player.getPassword(),
                player.getSalt(),
                player.getEmail(),
                player.hasPaid(),
                ConversionUtils.sqlTimeStampFrom(player.getCreated())
        ));
    }

    @Test
    public void construct_correct_parameters_for_getting_user() throws Exception {
        StatementDataObject getUserStatement = PlayerStatementDataConstructor
                .getStatementDataFor("getPlayer", Optional.empty());
        assertThat(getUserStatement.getSqlStatement(), is(EXPECTED_GET_USER));
        assertThat(getUserStatement.getParameters(), is(Collections.emptyList()));
    }

    @Test
    public void construct_correct_parameters_for_deleting_user() throws Exception {
        StatementDataObject getUserStatement = PlayerStatementDataConstructor
                .getStatementDataFor("deletePlayer", Optional.empty());
        assertThat(getUserStatement.getSqlStatement(), is(EXPECTED_DELETE_USER));
        assertThat(getUserStatement.getParameters(), is(Collections.emptyList()));
    }
}