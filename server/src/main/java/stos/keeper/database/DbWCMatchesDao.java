package stos.keeper.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stos.keeper.model.FootballMatch;
import stos.keeper.model.Group;
import stos.keeper.model.MatchType;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class DbWCMatchesDao {

    private Logger LOG = LoggerFactory.getLogger(DbWCMatchesDao.class);
    private DataSource dataSource;

    public DbWCMatchesDao(DataSource dataSource) {
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

    private Optional<FootballMatch> footballMatchFrom(ResultSet match) {
        try {
            return Optional.of(FootballMatch.builder().id(match.getInt("id"))
                    .time(ZonedDateTime.ofInstant(match.getTimestamp("match_time").toInstant(), ZoneId.systemDefault()))
                    .arena(match.getString("arena"))
                    .teams(match.getString("home_team"), match.getString("away_team"))
                    .matchType(MatchType.valueOf(match.getString("matchtype")))
                    .group(Group.valueOf(match.getString("group_id")))
                    .build());
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when building match from resultset.", e.getMessage(), e.getErrorCode());
            return Optional.empty();
        }
    }
}
