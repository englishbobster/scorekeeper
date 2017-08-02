package stos.keeper.database;

import stos.keeper.model.FootballMatch;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class StatementConstructor {


    public List<Object> getParametersFor(String transactionName, FootballMatch match) {
        List<Object> statementParameters = new ArrayList<>();
        statementParameters.add(match.getId());
        if (transactionName.equals("addMatch")) {
            statementParameters.add(sqlTimeStampFrom(match.getMatchTime()));
            statementParameters.add(match.getArena());
            statementParameters.add(match.getHomeTeam());
            statementParameters.add(match.getAwayTeam());
            statementParameters.add(match.getHomeScore());
            statementParameters.add(match.getAwayScore());
            statementParameters.add(match.isFullTime());
            statementParameters.add(match.getMatchType().name());
            statementParameters.add(match.getGroup().name());
        }
        return statementParameters;
    }


    public static Timestamp sqlTimeStampFrom(ZonedDateTime matchTime) {
        return Timestamp.from(matchTime.toInstant());
    }

}
