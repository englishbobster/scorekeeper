package stos.keeper.sparkServer.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Before;
import org.junit.Test;
import stos.keeper.model.player.Player;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TokenGeneratorTest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email@email.com";
    public static final int HRS_VALID = 2;
    public static final String EMAIL_NEW = "emil@eamil.com";
    public static final int EXPIRES_NOW = 0;
    private TokenGenerator generator;
    private Player player;
    private DigestKeyChest keyChest = DigestKeyChest.getInstance();

    @Before
    public void setUp() throws Exception {
        generator = new TokenGenerator(keyChest, HRS_VALID);
        player = Player.builder().id(2).username(USERNAME).password(PASSWORD).email(EMAIL).hasPaid(true)
                .created(ZonedDateTime.now()).build();
    }

    @Test
    public void generates_a_jwt_token() throws Exception {
        String jwt = generator.generateToken(USERNAME, PASSWORD, EMAIL);
        assertThat(jwt.split("\\.").length, is(equalTo(3)));
    }

    @Test
    public void verify_the_token_claims() throws Exception {
        String token = generator.generateToken(USERNAME, PASSWORD, EMAIL);
        assertThat(generator.verifyTokenAgainstPlayer(player, token), is(true));
    }

    @Test(expected = ExpiredJwtException.class)
    public void expiry_date_reached_gives_error() throws Exception {
        TokenGenerator generator = new TokenGenerator(keyChest, EXPIRES_NOW);
        String token = generator.generateToken(USERNAME, PASSWORD, EMAIL_NEW);
        boolean verified = generator.verifyTokenAgainstPlayer(player, token);
        assertThat(verified, is(true));

    }
}