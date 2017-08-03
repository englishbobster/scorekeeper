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

    private static final String MATCHES_TABLE_NAME = "planned_matches";
    private static final String DELETE_BY_ID = "DELETE FROM " + MATCHES_TABLE_NAME + " WHERE id= ?";
    private static final String SELECT_BY_ID = "SELECT * FROM " + MATCHES_TABLE_NAME + " WHERE id= ?";
    private static final String INSERT_MATCH = "INSERT INTO " + MATCHES_TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private Logger LOG = LoggerFactory.getLogger(DbWCMatchesDao.class);
    private DataSource dataSource;

    public DbWCMatchesDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean deleteMatchById(int id) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID);
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
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);
            statement.setInt(1, id);
            ResultSet selectedMatchResultSet = statement.executeQuery();
            Optional<FootballMatch> match = Optional.empty();
            if(selectedMatchResultSet.next()) {
                match = footballMatchFrom(selectedMatchResultSet);
            }
            selectedMatchResultSet.close();
            return match;
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when finding match by Id.", e.getMessage(), e.getErrorCode());
            return Optional.empty();
        }
        finally {
            dataSource.closeConnection();
        }
    }

    public boolean addMatch(FootballMatch match) {
        Connection connection = dataSource.getConnection();
        try {
            StatementConstructor statementConstructor = new StatementConstructor();
            List<Object> statementParameters = statementConstructor.getParametersFor("addMatch", match);
            PreparedStatement statement = connection.prepareStatement(INSERT_MATCH);
            return executeAddStatement(statementParameters, statement);
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when inserting match.", e.getMessage(), e.getErrorCode());
            return false;
        }
        finally {
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
