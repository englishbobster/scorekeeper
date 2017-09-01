package stos.keeper.database;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class ConversionUtils {

    public static Timestamp sqlTimeStampFrom(ZonedDateTime matchTime) {
        return Timestamp.from(matchTime.toInstant());
    }

}
