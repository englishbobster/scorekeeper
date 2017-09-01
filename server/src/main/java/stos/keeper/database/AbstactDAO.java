package stos.keeper.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

public class AbstactDAO {

    private Logger LOG = LoggerFactory.getLogger(PlayerDAO.class);
    protected DataSource dataSource;

    public AbstactDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected int executeAddStatement(List<Object> statementParameters, PreparedStatement statement) throws SQLException {
        IntStream.range(0, statementParameters.size()).forEach(index -> {
            try {
                statement.setObject((index + 1), statementParameters.get(index));
            } catch (SQLException e) {
                LOG.info("SQL exception {} with error code {} when setting statement parameters.", e.getMessage(), e.getErrorCode());
            }
        });
        return statement.executeUpdate();
    }
}
