package stos.keeper.sparkServer.security;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class PasswordServiceTest {

    @Test
    public void generate_salts_and_two_salts_are_not_equal() throws Exception {
        String passwordSalt = PasswordService.generateSalt();
        String passwordSalt_new = PasswordService.generateSalt();
        assertThat(passwordSalt_new, is(not(equalTo(passwordSalt))));
    }

    @Test
    public void encrypts_passwords_and_gets_same_results() throws Exception {
        String password = "mypassword";
        String generatedSalt = PasswordService.generateSalt();
        String encryptedPassword = PasswordService.encryptPassword(password, generatedSalt);
        String encryptedPasswordAgain = PasswordService.encryptPassword(password, generatedSalt);
        assertThat(password, is(not(equalTo(encryptedPassword))));
        assertThat(encryptedPassword, is(equalTo(encryptedPasswordAgain)));
    }

    @Test
    public void encrypts_two_different_passwords_and_gets_different_results() throws Exception {
        String password = "mypassword";
        String anotherPassword = "notmypassword";
        String generatedSalt = PasswordService.generateSalt();
        String encryptedPassword = PasswordService.encryptPassword(password, generatedSalt);
        String anotherEncryptedPassword = PasswordService.encryptPassword(anotherPassword, generatedSalt);
        assertThat(encryptedPassword, is(not(equalTo(anotherEncryptedPassword))));
    }

    @Test
    public void authenticate_a_password() throws Exception {
        String password = "mypassword";
        String generatedSalt = PasswordService.generateSalt();
        String encryptedPassword = PasswordService.encryptPassword(password, generatedSalt);
        boolean authentic = PasswordService.authenticate(password, encryptedPassword, generatedSalt);
        assertThat(authentic, is(true));
    }

    @Test
    public void fail_authentication_for_non_matching_passwords() throws Exception {
        String correctPwd = "mycorrectpassword";
        String incorrectPwd = "myincorrectpassword";
        String generatedSalt = PasswordService.generateSalt();
        String encryptedCorrectPassword = PasswordService.encryptPassword(correctPwd, generatedSalt);
        boolean authenticate =  PasswordService.authenticate(incorrectPwd, encryptedCorrectPassword, generatedSalt);
        assertThat(authenticate, is(false));
    }
}