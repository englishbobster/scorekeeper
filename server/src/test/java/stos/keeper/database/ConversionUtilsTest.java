package stos.keeper.database;

import org.junit.Test;
import stos.keeper.database.helpers.ConversionUtils;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConversionUtilsTest {

    @Test
    public void convert_a_zoned_datetime_to_sql_time_stamp_and_back() throws Exception {
        ZonedDateTime givenTime = ZonedDateTime.of(2004, 10, 19, 10, 23, 54, 0, ZoneId.systemDefault());
        Timestamp expected = Timestamp.from(givenTime.toInstant());
        assertThat(ConversionUtils.sqlTimeStampFrom(givenTime), is(equalTo(expected)));

        ZonedDateTime convertedBack = ZonedDateTime.ofInstant(expected.toInstant(), ZoneId.systemDefault());
        assertThat(convertedBack, is(equalTo(givenTime)));
    }

}