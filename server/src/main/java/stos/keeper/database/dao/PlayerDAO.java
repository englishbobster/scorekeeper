package stos.keeper.database.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stos.keeper.database.DataSource;
import stos.keeper.database.helpers.PlayerStatementDataConstructor;
import stos.keeper.database.helpers.StatementDataObject;
import stos.keeper.model.player.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public class PlayerDAO extends AbstractDAO {
    private Logger LOG = LoggerFactory.getLogger(PlayerDAO.class);

    public PlayerDAO(DataSource dataSource) {
        super(dataSource);
    }

    public int addPlayer(Player player) {
        Connection connection = dataSource.getConnection();
        String transactionName = "addPlayer";
        try {
            StatementDataObject statementData = PlayerStatementDataConstructor
                    .getStatementDataFor(transactionName, Optional.of(player));
            PreparedStatement statement = connection.prepareStatement(statementData.getSqlStatement(), PreparedStatement.RETURN_GENERATED_KEYS);
            int result = executeAddStatement(statementData.getParameters(), statement);
            if (result > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
            throw new SQLException("No generated key returned.");
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when performing {}", e.getMessage(), e.getErrorCode(), transactionName);
            return 0;
        } finally {
            dataSource.closeConnection();
        }
    }

    public Optional<Player> getPlayerByName(String userName) {
        Connection connection = dataSource.getConnection();
        String transactionName = "getPlayer";
        try {
            StatementDataObject statementData = PlayerStatementDataConstructor
                    .getStatementDataFor(transactionName, Optional.empty());
            PreparedStatement statement = connection.prepareStatement(statementData.getSqlStatement());
            statement.setString(1, userName);
            ResultSet selectedPlayerResultSet = statement.executeQuery();
            Optional<Player> playerOptional = Optional.empty();
            if (selectedPlayerResultSet.next()) {
                playerOptional = playerFrom(selectedPlayerResultSet);
            }
            selectedPlayerResultSet.close();
            return playerOptional;
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when finding player by Name.", e.getMessage(), e.getErrorCode());
            return Optional.empty();
        } finally {
            dataSource.closeConnection();
        }
    }

    public int deletePlayerByName(String userName) {
        Connection connection = dataSource.getConnection();
        try {
            StatementDataObject deleteStatementData = PlayerStatementDataConstructor
                    .getStatementDataFor("deletePlayer", Optional.empty());
            PreparedStatement statement = connection.prepareStatement(deleteStatementData.getSqlStatement());
            statement.setString(1, userName);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when deleting player by name.", e.getMessage(), e.getErrorCode());
            return 0;
        } finally {
            dataSource.closeConnection();
        }
    }

    private Optional<Player> playerFrom(ResultSet playerResultSet) {
        try {
            Player player = Player.builder()
                    .id(playerResultSet.getInt("id"))
                    .username(playerResultSet.getString("username"))
                    .password(playerResultSet.getString("password"))
                    .passwordSalt(playerResultSet.getString("salt"))
                    .email(playerResultSet.getString("email"))
                    .hasPaid(playerResultSet.getBoolean("paid"))
                    .created(ZonedDateTime.ofInstant(playerResultSet.getTimestamp("created").toInstant(), ZoneId.systemDefault())).build();

            return Optional.of(player);
        } catch (SQLException e) {
            LOG.info("SQL exception {} with error code {} when building player from result set.", e.getMessage(), e.getErrorCode());
            return Optional.empty();
        }
    }
}
