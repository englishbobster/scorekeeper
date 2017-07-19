package stos.keeper.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stos.keeper.model.FootballMatch;
import stos.keeper.model.Group;
import stos.keeper.model.MatchType;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public class DbWCMatchesDao {

    public static final String MATCHES_TABLE_NAME = "planned_matches";
    public static final String DELETE_BY_ID = "DELETE FROM " + MATCHES_TABLE_NAME + " WHERE id=?";
    public static final String SELECT_BY_ID = "SELECT * FROM " + MATCHES_TABLE_NAME + " WHERE id=?";
    public static final String INSERT_MATCH = "INSERT INTO " + MATCHES_TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            LOG.info("SQL exception {} with error code {} when deleting match by Id", e.getMessage(), e.getErrorCode());
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
            LOG.info("SQL exception {} with error code {} when finding match by Id", e.getMessage(), e.getErrorCode());
            return Optional.empty();
        }
        finally {
            dataSource.closeConnection();
        }
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
            LOG.info("SQL exception {} with error code {} when building match from resultset", e.getMessage(), e.getErrorCode());
            return Optional.empty();
        }
    }

    public boolean addMatch(FootballMatch match) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_MATCH);
            statement.setInt(1, match.getId());
            statement.setTimestamp(2,  sqlTimeStampFrom(match.getMatchTime()));
            statement.setString(3, match.getArena());
            statement.setString(4, match.getHomeTeam());
            statement.setString(5, match.getAwayTeam());
            statement.setInt(6, match.getHomeScore());
            statement.setInt(7, match.getAwayScore());
            statement.setBoolean(8, match.isFullTime());
            statement.setString(9, match.getMatchType().name());
            statement.setString(10, match.getGroup().name());
            return statement.execute();

        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when inserting match", e.getMessage(), e.getErrorCode());
            return false;
        }
        finally {
            dataSource.closeConnection();
        }
    }

    public static Timestamp sqlTimeStampFrom(ZonedDateTime matchTime) {
        return Timestamp.from(matchTime.toInstant());
    }
}
