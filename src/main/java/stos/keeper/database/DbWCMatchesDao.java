package stos.keeper.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stos.keeper.model.FootballMatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DbWCMatchesDao {

    public static final String MATCHES_TABLE_NAME = "planned_matches";
    public static final String DELETE_BY_ID = "DELETE FROM " + MATCHES_TABLE_NAME + " WHERE id=?";
    public static final String SELECT_BY_ID = "SELECT * FROM " + MATCHES_TABLE_NAME + " WHERE id=?";
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
            LOG.info("SQL exception {} with error code {}", e.getMessage(), e.getErrorCode());
        } finally {
            dataSource.closeConnection();
            return false;
        }
    }

    public Optional<FootballMatch> findMatchById(int id) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);
            statement.setInt(1, id);
            ResultSet selectedMatch = statement.executeQuery();
            return Optional.of(FootballMatch.builder().build());
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {}", e.getMessage(), e.getErrorCode());
        }
        finally {
            dataSource.closeConnection();
            return Optional.empty();
        }
    }
}
