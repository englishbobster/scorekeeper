package stos.keeper.sparkServer.security;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class PasswordServiceTest {

    @Test
    public void generates_a_salt_and_two_salts_are_not_equal() throws Exception {
        byte[] passwordSalt = PasswordService.generateSalt();
        byte[] passwordSalt_new = PasswordService.generateSalt();
        assertThat(passwordSalt.length, is(8));
        assertThat(passwordSalt_new, is(not(equalTo(passwordSalt))));

    }

    @Test
    public void encrypts_passwords_and_gets_same_results() throws Exception {
        String password = "mypassword";
        byte[] generatedSalt = PasswordService.generateSalt();
        byte [] encryptedPassword = PasswordService.encryptPassword(password, generatedSalt);
        byte [] encryptedPasswordAgain = PasswordService.encryptPassword(password, generatedSalt);
        assertThat(password, is(not(equalTo(encryptedPassword))));
        assertThat(encryptedPassword, is(equalTo(encryptedPasswordAgain)));
    }

    @Test
    public void encrypts_two_different_passwords_and_gets_different_results() throws Exception {
        String password = "mypassword";
        String anotherPassword = "notmypassword";
        byte[] generatedSalt = PasswordService.generateSalt();
        byte [] encryptedPassword = PasswordService.encryptPassword(password, generatedSalt);
        byte [] anotherEncryptedPassword = PasswordService.encryptPassword(anotherPassword, generatedSalt);
        assertThat(encryptedPassword, is(not(equalTo(anotherEncryptedPassword))));
    }

}