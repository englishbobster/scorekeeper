package stos.keeper.database.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stos.keeper.database.DataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

class AbstractDAO {

    private Logger LOG = LoggerFactory.getLogger(PlayerDAO.class);
    DataSource dataSource;

    AbstractDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    int executeAddStatement(List<Object> statementParameters, PreparedStatement statement) throws SQLException {
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
