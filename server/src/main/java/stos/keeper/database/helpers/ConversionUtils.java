package stos.keeper.database.helpers;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class ConversionUtils {

    public static Timestamp sqlTimeStampFrom(ZonedDateTime matchTime) {
        return Timestamp.from(matchTime.toInstant());
    }

}
