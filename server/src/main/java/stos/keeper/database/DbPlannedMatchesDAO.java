package stos.keeper.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stos.keeper.model.FootballMatch;
import stos.keeper.model.MatchType;
import stos.keeper.model.Score;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class DbPlannedMatchesDAO {

    private Logger LOG = LoggerFactory.getLogger(DbPlannedMatchesDAO.class);
    private DataSource dataSource;

    public DbPlannedMatchesDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean deleteMatchById(int id) {
        Connection connection = dataSource.getConnection();
        try {
            StatementDataObject deleteStatementData = PlannedMatchStatementDataConstructor
                    .getStatementDataFor("deleteMatchById", Optional.empty());
            PreparedStatement statement = connection.prepareStatement(deleteStatementData.getSqlStatement());
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when deleting match by Id.", e.getMessage(), e.getErrorCode());
            return false;
        } finally {
            dataSource.closeConnection();
        }
    }

    public Optional<FootballMatch> findMatchById(int id) {
        Connection connection = dataSource.getConnection();
        try {
            StatementDataObject findStatementData = PlannedMatchStatementDataConstructor
                    .getStatementDataFor("findMatchById", Optional.empty());
            PreparedStatement statement = connection.prepareStatement(findStatementData.getSqlStatement());
            statement.setInt(1, id);
            ResultSet selectedMatchResultSet = statement.executeQuery();
                        Optional<FootballMatch> matchOptional = Optional.empty();
            if (selectedMatchResultSet.next()) {
                matchOptional = footballMatchFrom(selectedMatchResultSet);
            }
            selectedMatchResultSet.close();
            return matchOptional;
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when finding match by Id.", e.getMessage(), e.getErrorCode());
            return Optional.empty();
        } finally {
            dataSource.closeConnection();
        }
    }

    public boolean addMatch(FootballMatch match) {
        Connection connection = dataSource.getConnection();
        try {
            StatementDataObject statementData = PlannedMatchStatementDataConstructor
                    .getStatementDataFor("addMatch", Optional.of(match));
            PreparedStatement statement = connection.prepareStatement(statementData.getSqlStatement());
            return executeAddStatement(statementData.getParameters(), statement);
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when inserting match.", e.getMessage(), e.getErrorCode());
            return false;
        } finally {
            dataSource.closeConnection();
        }
    }

    public int countPlannedMatchEntries() {
        Connection connection = dataSource.getConnection();
        int count = 0;
        try {
            StatementDataObject statementData = PlannedMatchStatementDataConstructor
                    .getStatementDataFor("countPlannedMatches", Optional.empty());
            PreparedStatement statement = connection.prepareStatement(statementData.getSqlStatement());
            ResultSet countMatchResultSet = statement.executeQuery();
            if (countMatchResultSet.next()) {
                count = getPlannedMatchCountFrom(countMatchResultSet);
            }
            countMatchResultSet.close();
            return count;

        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when counting planned matches.", e.getMessage(), e.getErrorCode());
            return 0;
        } finally {
            dataSource.closeConnection();
        }
    }

    public List<FootballMatch> getAllPlannedMatches() {
        List<FootballMatch> allMatches = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        try {
            StatementDataObject statementData = PlannedMatchStatementDataConstructor
                    .getStatementDataFor("fetchAllPlannedMatches", Optional.empty());
            PreparedStatement statement = connection.prepareStatement(statementData.getSqlStatement());
            ResultSet plannedMatches = statement.executeQuery();
            while (plannedMatches.next()) {
                Optional<FootballMatch> match = footballMatchFrom(plannedMatches);
                if (match.isPresent()) {
                    allMatches.add(match.get());
                }
            }
            return allMatches;
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when fetching all planned matches.", e.getMessage(), e.getErrorCode());
            return Collections.emptyList();
        } finally {
            dataSource.closeConnection();
        }
    }

    public int setFinalScoreForMatchById(int id, Score score) {
        Connection connection = dataSource.getConnection();
        try {
            StatementDataObject statementData = PlannedMatchStatementDataConstructor
                    .getStatementDataFor("updatePlannedMatchScoreById", Optional.empty());
            PreparedStatement statement = connection.prepareStatement(statementData.getSqlStatement());
            statement.setInt(1, score.getHomeScore());
            statement.setInt(2, score.getAwayScore());
            statement.setBoolean(3, true);
            statement.setInt(4, id);
            return  statement.executeUpdate();
      } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when updating match with id: {}", e.getMessage(), e.getErrorCode(), id);
            return 0;
        } finally {
            dataSource.closeConnection();
        }
    }

    private boolean executeAddStatement(List<Object> statementParameters, PreparedStatement statement) throws SQLException {
        IntStream.range(0, statementParameters.size()).forEach(index -> {
            try {
                statement.setObject((index + 1), statementParameters.get(index));
            } catch (SQLException e) {
                LOG.info("SQL exception {} with error code {} when setting statement parameters.", e.getMessage(), e.getErrorCode());
            }
        });
        return statement.execute();
    }

    private Optional<FootballMatch> footballMatchFrom(ResultSet resultSet) {
        try {
            FootballMatch footballMatch = FootballMatch.builder().id(resultSet.getInt("id"))
                    .time(ZonedDateTime.ofInstant(resultSet.getTimestamp("match_time").toInstant(), ZoneId.systemDefault()))
                    .arena(resultSet.getString("arena"))
                    .teams(resultSet.getString("home_team"), resultSet.getString("away_team"))
                    .matchType(MatchType.valueOf(resultSet.getString("matchtype")))
                    .score(new Score(resultSet.getInt("home_score"), resultSet.getInt("away_score")))
                    .fullTime(resultSet.getBoolean("fulltime")).build();
            return Optional.of(footballMatch);
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when building match from resultset.", e.getMessage(), e.getErrorCode());
            return Optional.empty();
        }
    }

    private int getPlannedMatchCountFrom(ResultSet countResult) {
        int count = 0;
        try {
            count = countResult.getInt("count");
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when building match from resultset.", e.getMessage(), e.getErrorCode());
            return count;
        }
        return count;
    }
}
