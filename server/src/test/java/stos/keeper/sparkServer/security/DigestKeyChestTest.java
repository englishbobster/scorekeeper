package stos.keeper.sparkServer.security;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DigestKeyChestTest {

    @Test
    public void singleton_instance_of_DigestKeyChest_gives_same_key() throws Exception {
        DigestKeyChest newInstance = DigestKeyChest.getInstance();
        DigestKeyChest theSameInstance = DigestKeyChest.getInstance();

        assertThat(newInstance, is(equalTo(theSameInstance)));
        assertThat(newInstance.getJwtKey(), is(equalTo(theSameInstance.getJwtKey())));
    }
}