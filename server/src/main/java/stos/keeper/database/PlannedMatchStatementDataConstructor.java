package stos.keeper.database;

import stos.keeper.model.FootballMatch;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class PlannedMatchStatementDataConstructor {

    private static final String MATCHES_TABLE_NAME = "planned_matches";
    private static final String DELETE_BY_ID = "DELETE FROM " + MATCHES_TABLE_NAME + " WHERE id= ?";
    private static final String SELECT_BY_ID = "SELECT * FROM " + MATCHES_TABLE_NAME + " WHERE id= ?";
    private static final String INSERT_MATCH = "INSERT INTO " + MATCHES_TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final List<Object> EMPTY_LIST = new ArrayList<>();

    static StatementDataObject getStatementDataFor(String transactionName, Optional<FootballMatch> matchOptional) {

        if(transactionName.equals("deleteMatchById")){
            return new StatementDataObject(DELETE_BY_ID, EMPTY_LIST);
        }
        if (transactionName.equals("findMatchById")) {
            return new StatementDataObject(SELECT_BY_ID, EMPTY_LIST);
        }
        if (matchOptional.isPresent()) {
            FootballMatch match = matchOptional.get();
            if (transactionName.equals("addMatch")) {
                List<Object> statementParameters = new ArrayList<>();
                statementParameters.add(match.getId());
                statementParameters.add(sqlTimeStampFrom(match.getMatchTime()));
                statementParameters.add(match.getArena());
                statementParameters.add(match.getHomeTeam());
                statementParameters.add(match.getAwayTeam());
                statementParameters.add(match.getHomeScore());
                statementParameters.add(match.getAwayScore());
                statementParameters.add(match.isFullTime());
                statementParameters.add(match.getMatchType().name());
                statementParameters.add(match.getGroup().name());
                return new StatementDataObject(INSERT_MATCH, statementParameters);
            }
        }
        return new StatementDataObject("", EMPTY_LIST);
    }

    static Timestamp sqlTimeStampFrom(ZonedDateTime matchTime) {
        return Timestamp.from(matchTime.toInstant());
    }

}
