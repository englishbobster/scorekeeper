package stos.keeper.model.player;

import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    public static final String NAME = "bob";
    public static final String PASSWORD = "passme";
    public static final ZonedDateTime TIME_CREATED = ZonedDateTime.now();
    public static final String ADDRESS = "email@an.email";
    public static final String NAME_OTHER = "nimrod";
    public static final String PASSWORD_OTHER = "wallet";
    public static final String EMAIL_OTHER = "candypants@pandycants.com";
    public static final String EXPECTED_STRING = NAME + ":" + PASSWORD + ":" + ADDRESS + ":" + "PAID" + ":"
            + TIME_CREATED.toString();
    private Player player_1;
    private Player another_player_1;
    private Player player_2;

    @Before
    public void setup() {
        player_1 = Player.builder().username(NAME).password(PASSWORD)
                .email(ADDRESS).hasPaid(true).created(TIME_CREATED).build();

        another_player_1 = Player.builder().username(NAME).password(PASSWORD)
                .email(ADDRESS).created(TIME_CREATED).build();

        player_2 = Player.builder().username(NAME_OTHER).password(PASSWORD_OTHER)
                .email(EMAIL_OTHER).created(TIME_CREATED).build();
    }

    @Test
    public void create_a_player_using_builder() throws Exception {
        assertThat(player_1.hasPaid(), is(true));
        assertThat(player_1.getUserName(), is(NAME));
        assertThat(player_1.getPassword(), is(PASSWORD));
        assertThat(player_1.getEmail(), is(ADDRESS));
        assertThat(player_1.getCreated(), is(TIME_CREATED));
    }

    @Test
    public void paid_is_default_false() {
        assertThat(another_player_1.hasPaid(), is(false));
    }

    @Test
    public void equals_symmetry() {
        assertTrue("equals is not symmetric", player_1.equals(another_player_1));
        assertTrue("equals is not symmetric", another_player_1.equals(player_1));
    }

    @Test
    public void not_equals() throws Exception {
        assertTrue(!player_1.equals(player_2));
    }

    @Test
    public void hashcode_the_same_for_equal_objects() {
        assertThat(player_1.hashCode(), is(equalTo(another_player_1.hashCode())));
    }

    @Test
    public void hashcode_different_for_unequal_objects() {
        assertThat(player_1.hashCode(), is(not(equalTo(player_2.hashCode()))));
    }

    @Test
    public void toString_returns() throws Exception {
        String result = player_1.toString();
        assertThat(result, is(equalTo(EXPECTED_STRING)));
    }
}