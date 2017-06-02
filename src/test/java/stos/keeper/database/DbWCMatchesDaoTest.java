package stos.keeper.database;

import org.junit.Test;

import java.sql.Connection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class DbWCMatchesDaoTest {

    private Connection connection = mock(Connection.class);

    @Test
    public void construct_DAO_with_data_source() {
        DataSource dataSource = () -> connection;
        DbWCMatchesDao dbWCMatchesDao = new DbWCMatchesDao(dataSource);
        assertThat(dbWCMatchesDao, is(notNullValue()));
    }

}