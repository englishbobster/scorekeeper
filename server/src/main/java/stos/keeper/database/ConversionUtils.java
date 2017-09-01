package stos.keeper.database;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

class ConversionUtils {

    static Timestamp sqlTimeStampFrom(ZonedDateTime matchTime) {
        return Timestamp.from(matchTime.toInstant());
    }

}
