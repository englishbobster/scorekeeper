package stos.keeper.database;

import stos.keeper.model.planned_matches.FootballMatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class PlannedMatchStatementDataConstructor {

    private static final String MATCHES_TABLE_NAME = "planned_matches";
    private static final String DELETE_BY_ID = "DELETE FROM " + MATCHES_TABLE_NAME + " WHERE id= ?";
    private static final String SELECT_BY_ID = "SELECT * FROM " + MATCHES_TABLE_NAME + " WHERE id= ?";
    private static final String INSERT_MATCH = "INSERT INTO " + MATCHES_TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String COUNT_MATCHES = "SELECT COUNT(*) FROM " + MATCHES_TABLE_NAME;
    private static final String FETCH_ALL_PLANNED_MATCHES = "SELECT * FROM " + MATCHES_TABLE_NAME;
    private static final String SET_FINAL_SCORE_FOR_MATCH_BY_ID = "UPDATE " + MATCHES_TABLE_NAME + " SET (home_score, away_score, fulltime)=(?, ?, ?)"
            + " WHERE ID= ? and fulltime = false";


    static StatementDataObject getStatementDataFor(String transactionName, Optional<FootballMatch> matchOptional) {

        if(transactionName.equals("deleteMatchById")){
            return new StatementDataObject(DELETE_BY_ID, Collections.emptyList());
        }
        if (transactionName.equals("findMatchById")) {
            return new StatementDataObject(SELECT_BY_ID, Collections.emptyList());
        }
        if (transactionName.equals("countPlannedMatches")) {
            return new StatementDataObject(COUNT_MATCHES, Collections.emptyList());
        }
        if (transactionName.equals("fetchAllPlannedMatches")) {
            return new StatementDataObject(FETCH_ALL_PLANNED_MATCHES, Collections.emptyList());
        }
        if (transactionName.equals("updatePlannedMatchScoreById")) {
            return new StatementDataObject(SET_FINAL_SCORE_FOR_MATCH_BY_ID, Collections.emptyList());
        }
        if (matchOptional.isPresent()) {
            FootballMatch match = matchOptional.get();
            if (transactionName.equals("addMatch")) {
                List<Object> statementParameters = new ArrayList<>();
                statementParameters.add(match.getId());
                statementParameters.add(ConversionUtils.sqlTimeStampFrom(match.getMatchTime()));
                statementParameters.add(match.getArena());
                statementParameters.add(match.getHomeTeam());
                statementParameters.add(match.getAwayTeam());
                statementParameters.add(match.getScore().getHomeScore());
                statementParameters.add(match.getScore().getAwayScore());
                statementParameters.add(match.isFullTime());
                statementParameters.add(match.getMatchType().name());
                return new StatementDataObject(INSERT_MATCH, statementParameters);
            }
        }
        return new StatementDataObject("", Collections.emptyList());
    }

}
